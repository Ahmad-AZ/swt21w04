package festivalmanager.utils;


import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.Optional;


/**
 * Pages that show information on one specific festival need some information about that festival
 * in their model in order to display the page header.
 * This information is added to the model by an instance of UtilsManagement.
 * @author Jan Biedermann
 */
@Service
@Transactional
public class UtilsManagement {


	FestivalManagement festivalManagement;


	/**
	 * Creates a new instance of UtilsManagement
	 * @param festivalManagement an instance of {@link festivalmanager.festival.FestivalManagement}
	 */
	public UtilsManagement(FestivalManagement festivalManagement) {
		this.festivalManagement = festivalManagement;
	}


	/**
	 * Adds model attributes for the id, name and location
	 * of the festival belonging to festivalId to the given model.
	 * If festivalId is null, no attributes are added.
	 * @param model the spring model to be populated with attributes
	 * @param festivalId the id of the festival from which the attributes are to be taken
	 */
	public void prepareModel(Model model, Long festivalId) {

		if (festivalId == null) {
			return;
		}

		Optional<Festival> festivalOptional = festivalManagement.findById(festivalId);
		if (festivalOptional.isPresent()) {

			Festival currentFestival = festivalOptional.get();

			model.addAttribute("festivalId", festivalId);
			model.addAttribute("festivalName", currentFestival.getName());
			model.addAttribute("festivalLocation", currentFestival.getLocation());
		}
	}

}
