package festivalmanager.utils;

import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Arrays;
import java.util.Optional;


// Class used to highlight the navigation bar button for the currently used page
// and tell the model whether the current festival has a location,
// so that the navigation bar can be adjusted accordingly
@Service
@Transactional
public class UtilsManagement {


	List<String> pagesUpperHeader;
	List<String> pagesLowerHeader;

	String currentPageUpperHeader;
	String currentPageLowerHeader;
	FestivalManagement festivalManagement;
	// TODO: Remove this
	Festival currentFestival;


	public UtilsManagement(FestivalManagement festivalManagement) {
		this.festivalManagement = festivalManagement;
		this.currentPageUpperHeader = null;
		this.currentPageLowerHeader = null;
		// TODO: Remove this
		this.currentFestival = null;


		// Add a name for your page here if it can be accessed through the upper navigation bar
		pagesUpperHeader = Arrays.asList("festivals", "locations", "artists", "messages", "financesCompany");
		// Add a name for your page here if it can be accessed through the lower navigation bar
		pagesLowerHeader = Arrays.asList("festivalDetail", "staff", "location", "equipment",
				"artistOverview", "finances", "catering", "tickets", "ticketShop",
				"schedule", "program", "map", "cateringSales");
	}


	// In the @GetMapping method for your page that will be called first,
	// add "updateCurrentPageUpperHeader(<Name of the Page in navigation bar that should be highlighted>)"
	public void setCurrentPageUpperHeader(String currentPage) {
		this.currentPageUpperHeader = currentPage;
	}


	// In the @GetMapping method for your page that will be called first,
	// add "updateCurrentPageLowerHeader(<Name of the Page in navigation bar that should be highlighted>)"
	public void setCurrentPageLowerHeader(String currentPage) {
		this.currentPageLowerHeader = currentPage;
	}


	// TODO: Remove this
	public void setCurrentFestival(long currentFestivalId) {

		Optional<Festival> festivalOptional = festivalManagement.findById(currentFestivalId);
		if (festivalOptional.isPresent()) {
			this.currentFestival = festivalOptional.get();
		}
	}


	// TODO: Remove this
	public Long getCurrentFestivalId() {
		if (currentFestival != null) {
			return currentFestival.getId();
		}

		return null;
	}


	// TODO: Remove this
	public void prepareModel(Model model) {

		// Update currentFestival
		if (currentFestival != null) {
			setCurrentFestival(getCurrentFestivalId());
		}

		if (currentPageUpperHeader != null) {

			if (pagesUpperHeader.contains(currentPageUpperHeader)) {
				model.addAttribute(currentPageUpperHeader + "Current", "current");
			} else {
				System.out.println("DEBUG Warning: CurrentPageManagement does not " +
						"know the name of your page; Name was: " + currentPageUpperHeader);
			}
		}

		if (currentPageLowerHeader != null) {

			if (pagesLowerHeader.contains(currentPageLowerHeader)) {
				model.addAttribute(currentPageLowerHeader + "Current", "current");
			} else {
				System.out.println("DEBUG Warning: CurrentPageManagement does not " +
						"know the name of your page; Name was: " + currentPageUpperHeader);
			}
		}

		if (currentFestival != null) {
			model.addAttribute("festivalLocation", currentFestival.getLocation());
			model.addAttribute("festivalId", currentFestival.getId());
			model.addAttribute("festivalName", currentFestival.getName());
		}
	}


	public void prepareModel(Model model, Long festivalId) {

		Festival currentFestival = null;

		if (festivalId != null) {

			Optional<Festival> festivalOptional = festivalManagement.findById(festivalId);
			if (festivalOptional.isPresent()) {
				currentFestival = festivalOptional.get();
			}
		}

		if (currentPageUpperHeader != null) {

			if (pagesUpperHeader.contains(currentPageUpperHeader)) {
				model.addAttribute(currentPageUpperHeader + "Current", "current");
			} else {
				System.out.println("DEBUG Warning: CurrentPageManagement does not " +
						"know the name of your page; Name was: " + currentPageUpperHeader);
			}
		}

		if (currentPageLowerHeader != null) {

			if (pagesLowerHeader.contains(currentPageLowerHeader)) {
				model.addAttribute(currentPageLowerHeader + "Current", "current");
			} else {
				System.out.println("DEBUG Warning: CurrentPageManagement does not " +
						"know the name of your page; Name was: " + currentPageUpperHeader);
			}
		}

		if (currentFestival != null) {
			model.addAttribute("festivalLocation", currentFestival.getLocation());
			model.addAttribute("festivalId", currentFestival.getId());
			model.addAttribute("festivalName", currentFestival.getName());
		}
	}

}
