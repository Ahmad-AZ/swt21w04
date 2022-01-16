package festivalmanager.catering;

import festivalmanager.messaging.MessageManagement;
import festivalmanager.messaging.forms.SendGroupMessageForm;
import org.salespointframework.order.Cart;
import org.salespointframework.order.CartItem;
import org.salespointframework.quantity.Quantity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
//import org.springframework.web.bind.annotation.ModelAttribute;
import org.salespointframework.catalog.ProductIdentifier;
import festivalmanager.festival.*;
import festivalmanager.utils.*;
import org.springframework.web.server.ResponseStatusException;

//import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

/**
 * @author Robert Menzel
 */
@Controller
@SessionAttributes("cart")
public class CateringController {
	private UtilsManagement utilsManagement;
	private FestivalManagement festivalManagement;
	private CateringProductCatalog catalog;
	private CateringStock stock;
	private CateringSales sales;
	private MessageManagement messageManagement;

	public CateringController(
			CateringProductCatalog catalog,
			CateringStock stock,
			CateringSales sales,
			UtilsManagement utilsManagement,
			FestivalManagement festivalManagement,
			MessageManagement messageManagement) {
		this.catalog = catalog;
		this.stock = stock;
		this.utilsManagement = utilsManagement;
		this.festivalManagement = festivalManagement;
		this.sales = sales;
		this.messageManagement = messageManagement;
	}

	@ModelAttribute("title")
	public String getTitle() {
		return "Catering Verkäufe";
	}

	@ModelAttribute("cart")
	Cart initializeCart() {
		return new Cart();
	}

	public Iterable<CateringProduct> getBoughtProducts(long festivalid) {
		Iterable<CateringStockItem> iCSI = stock.findByFestivalId(festivalid);
		LinkedList<CateringProduct> lCP = new LinkedList<CateringProduct>();
		Quantity qZero = Quantity.of(0);
		for (CateringStockItem csi : iCSI) {
			if (csi.getQuantity().isGreaterThan(qZero)) {
				if (!lCP.contains(csi.getProduct()) && (!csi.getProduct().isHidden())) {
					lCP.add(csi.getProduct());
				}
			}
		}
		return lCP;
	}

	public Map<CateringProduct, Quantity> getProductCounts(long festivalId) {
		Iterable<CateringStockItem> iCSI = stock.findByFestivalId(festivalId);
		HashMap<CateringProduct, Quantity> mCP = new HashMap<CateringProduct, Quantity>();
		Quantity qAll;
		CateringProduct product;
		for (CateringStockItem csi : iCSI) {
			product = csi.getProduct();
			qAll = mCP.get(product);
			if (qAll == null) {
				qAll = Quantity.of(0);
			}
			qAll = qAll.add(csi.getQuantity());
			// System.out.println("getProductCounts: " + product.getName() + "=" + qAll);
			mCP.put(product, qAll);
		}
		return mCP;
	}

	/** Reduces the products in the cart to its maximum in the stock. */
	public Cart chopCart(Cart cart, long festivalId) {
		Map<CateringProduct, Quantity> mProdCnts = getProductCounts(festivalId);
		for (CartItem cartItem : cart) {
			CateringProduct product = (CateringProduct) cartItem.getProduct();
			Quantity qStock = mProdCnts.get(product);
			System.out.println("chopCart:" + qStock + "<" + cartItem.getQuantity());
			if (cartItem.getQuantity().isGreaterThan(qStock)) {
				Quantity qDifference = qStock.subtract(cartItem.getQuantity());
				cart.addOrUpdateItem(product, qDifference);
			}
		}
		return cart;
	}

	public void bookOut(CateringProduct product, Quantity quantity, long festivalId) {
		Iterable<CateringStockItem> iCSI = stock.findByFestivalId(festivalId, CateringStock.BBD_SORT);
		for (CateringStockItem csi : iCSI) {
			CateringProduct csiProduct = csi.getProduct();
			if (csiProduct.equals(product)) {
				// the quantity in stockitem minus the quantity in the cart
				Quantity qStockItem = csi.getQuantity();
				Quantity qDiff = qStockItem.subtract(quantity);

				if (qDiff.isNegative()) {
					// the case the cart qunatity is greater than the stockitem is
					quantity = quantity.subtract(qStockItem);
					qStockItem = Quantity.of(0);
				} else {
					// the normal case
					qStockItem = qStockItem.subtract(quantity);
					quantity = Quantity.of(0);
				}
				csi.decreaseQuantity(csi.getQuantity().subtract(qStockItem));
				stock.save(csi);
			}
		}
	}

	@GetMapping("/catering/{festivalId}")
	String sales(Model model, @ModelAttribute Cart cart, @PathVariable Long festivalId) {

		Optional<Festival> festivalOptional = festivalManagement.findById(festivalId);
		if (festivalOptional.isEmpty()) {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "entity not found");
		}

		// Festival currentFestival = festivalOptional.get();

		model.addAttribute("productcatalog", getBoughtProducts(festivalId));
		model.addAttribute("productid", null);

		utilsManagement.prepareModel(model, festivalId);
		cart = chopCart(cart, festivalId);
		model.addAttribute("cart", cart);
		return "catering";
	}

	@PostMapping("/catering/addToCart")
	String addToCart(Model model, AddToCartFormResult formResult, @ModelAttribute Cart cart,
			@RequestParam("festivalId") Long festivalId) {
		cart = chopCart(cart, festivalId);
		if (formResult.productId != null) {
			Optional<CateringProduct> oProduct = catalog.findById(formResult.productId);

			if (oProduct.isPresent()) {
				CateringProduct product = oProduct.get();
				cart.addOrUpdateItem(product, Quantity.of(formResult.productCount));
			}
		}
		return "redirect:/catering/" + festivalId;
	}

	@PostMapping("/catering/checkout")
	String checkout(Model model, @ModelAttribute Cart cart, @RequestParam("festivalId") Long festivalId) {
		cart = chopCart(cart, festivalId);
		Map<CateringProduct, Quantity> mapBefore = getProductCounts(festivalId);
		for (CartItem item : cart) {
			CateringSalesItem salesItem = new CateringSalesItem(
					(CateringProduct) item.getProduct(),
					item.getQuantity(),
					festivalId,
					item.getPrice());
			sales.save(salesItem);
			bookOut((CateringProduct) item.getProduct(), item.getQuantity(), festivalId);
		}
		Map<CateringProduct, Quantity> mapAfter = getProductCounts(festivalId);
		for (CateringProduct cp : mapBefore.keySet()) {
			Quantity qBefore = mapBefore.get(cp);
			Quantity qAfter = mapAfter.get(cp);
			Quantity qMin = cp.getMinimumStock();
			if ((qBefore.isGreaterThan(qMin)) && (qAfter.isLessThan(qMin))) {
				System.out.println("CateringStock Minimum: " + qBefore + "->" + qAfter + "<=" + qMin);
				messageManagement.sendMessage(new SendGroupMessageForm(-1, festivalId, "FESTIVAL_LEADER", "Der Lagerbestand von " cp.getName() + " ist unter dem Minimum, bitte nachbestellen!", ""));
			}
		}

		cart.clear();
		return "redirect:/catering/" + festivalId;
	}

	class AddToCartFormResult {
		ProductIdentifier productId;
		long productCount;

		public AddToCartFormResult(ProductIdentifier productId, long productCount) {
			this.productId = productId;
			this.productCount = productCount;
		}
	}

	public CateringSales getCateringSales() {
		return sales;
	}

	public CateringProductCatalog getCateringProductCatalog() {
		return catalog;
	}

	public CateringStock getStock() {
		return stock;
	}
}
