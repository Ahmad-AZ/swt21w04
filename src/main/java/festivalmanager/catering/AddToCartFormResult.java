package festivalmanager.catering;

import org.salespointframework.catalog.ProductIdentifier;

/**
 * A class containing the result of the form Add to Cart.
 */
public class AddToCartFormResult {
    ProductIdentifier productId;
    String productCount;

    public AddToCartFormResult(ProductIdentifier productId, String productCount) {
        this.productId = productId;
        this.productCount = productCount;
    }
}
