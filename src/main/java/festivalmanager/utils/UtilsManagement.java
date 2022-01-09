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


	public UtilsManagement(FestivalManagement festivalManagement) {
		this.festivalManagement = festivalManagement;
	}


	public void prepareModel(Model model, Long festivalId) {

		if (festivalId == null) {
			return;
		}

		Optional<Festival> festivalOptional = festivalManagement.findById(festivalId);
		if (festivalOptional.isPresent()) {

			Festival currentFestival = festivalOptional.get();

			model.addAttribute("festivalLocation", currentFestival.getLocation());
			model.addAttribute("festivalId", currentFestival.getId());
			model.addAttribute("festivalName", currentFestival.getName());
		}
	}

}
