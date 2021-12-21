package festivalmanager.utils;


import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Arrays;


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
	Festival currentFestival;
	FestivalManagement festivalManagement;


	public UtilsManagement(FestivalManagement festivalManagement) {
		this.festivalManagement = festivalManagement;
		this.currentFestival = null;
		this.currentPageUpperHeader = null;
		this.currentPageLowerHeader = null;

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


	public void setCurrentFestival(long currentFestivalId) {
		this.currentFestival = festivalManagement.findById(currentFestivalId).get();
	}

	public Long getCurrentFestivalId() {

		if (currentFestival != null) {
			return currentFestival.getId();
		}

		return null;
	}


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

}
