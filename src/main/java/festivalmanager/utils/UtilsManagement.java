package festivalmanager.utils;


import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.Optional;


@Service
@Transactional
public class UtilsManagement {


	FestivalManagement festivalManagement;
	// TODO: Remove this
	Festival currentFestival;


	public UtilsManagement(FestivalManagement festivalManagement) {
		this.festivalManagement = festivalManagement;
		// TODO: Remove this
		this.currentFestival = null;
	}


	// TODO: Remove this
	@Deprecated
	public void setCurrentFestival(long currentFestivalId) {

		Optional<Festival> festivalOptional = festivalManagement.findById(currentFestivalId);
		if (festivalOptional.isPresent()) {
			this.currentFestival = festivalOptional.get();
		}
	}


	// TODO: Remove this
	@Deprecated
	public Long getCurrentFestivalId() {
		if (currentFestival != null) {
			return currentFestival.getId();
		}

		return null;
	}


	// TODO: Remove this
	public void prepareModel(Model model) {

		Long currentFestivalId;

		if (currentFestival != null) {
			currentFestivalId = getCurrentFestivalId();
		} else {
			currentFestivalId = null;
		}

		prepareModel(model, currentFestivalId);
	}


	public void prepareModel(Model model, Long festivalId) {

		Festival currentFestival = null;

		if (festivalId != null) {

			Optional<Festival> festivalOptional = festivalManagement.findById(festivalId);
			if (festivalOptional.isPresent()) {
				currentFestival = festivalOptional.get();
			}
		}

		if (currentFestival != null) {
			model.addAttribute("festivalLocation", currentFestival.getLocation());
			model.addAttribute("festivalId", currentFestival.getId());
			model.addAttribute("festivalName", currentFestival.getName());
		}
	}

}
