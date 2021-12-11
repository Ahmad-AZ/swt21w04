package festivalmanager.utils;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Arrays;


// Class used to highlight the navigation bar button for the currently opened page
@Service
@Transactional
public class CurrentPageManagement {


	List<String> pages;


	public CurrentPageManagement() {
		// Add a name for your page here if it can be accessed through the navigation bar
		pages = Arrays.asList("festivalDetail", "staff", "location", "equipment",
				"artists", "finances", "catering", "tickets", "ticketShop",
				"schedule", "program", "map");
	}


	// In the @GetMapping method for your page,
	// add "updateCurrent(model, <Name of the Page in navigation bar that should be highlighted>)"
	public void updateCurrentPage(Model model, String currentPage) {

		for (String page : pages) {
			model.addAttribute(page + "Current", "notCurrent");
		}

		if (currentPage != null && pages.contains(currentPage)) {
			model.addAttribute(currentPage + "Current", "current");
		}
		else if(currentPage != null) {
			System.out.println("DEBUG Warning: CurrentPageManagement does not " +
					"know the name of your page; Name was: " + currentPage);
		}
	}


}
