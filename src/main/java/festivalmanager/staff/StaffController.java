package festivalmanager.staff;

import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.staff.forms.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class StaffController {
	private final StaffManagement staffManagement;
	private final FestivalManagement festivalManagement;

	public StaffController(StaffManagement staffManagement, FestivalManagement festivalManagement) {
		Assert.notNull(staffManagement, "StaffManagement must not be null!");
		Assert.notNull(festivalManagement, "FestivalManagement must not be null!");
		this.staffManagement = staffManagement;
		this.festivalManagement = festivalManagement;
	}

	@GetMapping("/staff/{festivalId}")
	@PreAuthorize("hasRole('ADMIN')")
	public String getStaffInfo(@PathVariable("festivalId") long festivalId, Model model) {
		model.addAttribute("entries", staffManagement.findByFestivalId(festivalId));

		Optional<Festival> festival = festivalManagement.findById(festivalId);
		if (festival.isPresent()) {
			model.addAttribute("festival", festival.get());
		}

		return "staff.html";
	}

	@GetMapping("/staff/{festivalId}/create")
	@PreAuthorize("hasRole('ADMIN')")
	public String getCreateStaffDialog(@PathVariable("festivalId") long festivalId, Model model) {
		model.addAttribute("entries", staffManagement.findByFestivalId(festivalId));
		model.addAttribute("dialog", "create_staff");

		Optional<Festival> festival = festivalManagement.findById(festivalId);
		if (festival.isPresent()) {
			model.addAttribute("festival", festival.get());
		}

		return "staff.html";
	}

	@GetMapping("/staff/{festivalId}/detail/{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	public String getPersonDetailView(@PathVariable("festivalId") long festivalId, @PathVariable("userId") long userId, Model model) {
		model.addAttribute("entries", staffManagement.findByFestivalId(festivalId));

		Optional<Person> user = staffManagement.findById(userId);
		if (user.isPresent()) {
			model.addAttribute("person", user.get());
		}

		Optional<Festival> festival = festivalManagement.findById(festivalId);
		if (festival.isPresent()) {
			model.addAttribute("festival", festival.get());
		}

		return "person.html";
	}

	@GetMapping("/staff/{festivalId}/remove/{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	public String getRemoveStaffDialog(@PathVariable("festivalId") long festivalId, @PathVariable("userId") long userId, Model model) {
		model.addAttribute("entries", staffManagement.findByFestivalId(festivalId));
		model.addAttribute("currentId", userId);
		model.addAttribute("dialog", "remove_staff");

		Optional<Person> user = staffManagement.findById(userId);
		if (user.isPresent()) {
			model.addAttribute("currentName", user.get().getName());
		} else {
			model.addAttribute("currentName", "");
		}

		Optional<Festival> festival = festivalManagement.findById(festivalId);
		if (festival.isPresent()) {
			model.addAttribute("festival", festival.get());
		}

		return "staff.html";
	}

	@PostMapping("/staff/{festivalId}/create")
	@PreAuthorize("hasRole('ADMIN')")
	public String createStaff(@PathVariable("festivalId") long festivalId, CreateStaffForm form) {
		this.staffManagement.createPerson(festivalId, form);

		return "redirect:/staff/" + festivalId;
	}

	@PostMapping("/staff/{festivalId}/remove")
	@PreAuthorize("hasRole('ADMIN')")
	public String removeStaff(@PathVariable("festivalId") long festivalId, RemoveStaffForm form) {
		this.staffManagement.removePerson(form);

		return "redirect:/staff/" + festivalId;
	}

	@GetMapping("/staff/{festivalId}/change_role/{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	public String getChangeRoleDialog(@PathVariable("festivalId") long festivalId, @PathVariable("userId") long userId, Model model) {
		model.addAttribute("dialog", "change_role");

		Optional<Person> user = staffManagement.findById(userId);
		if (user.isPresent()) {
			model.addAttribute("person", user.get());
		}

		Optional<Festival> festival = festivalManagement.findById(festivalId);
		if (festival.isPresent()) {
			model.addAttribute("festival", festival.get());
		}

		return "person.html";
	}

	@GetMapping("/staff/{festivalId}/change_password/{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	public String getChangePasswordDialog(@PathVariable("festivalId") long festivalId, @PathVariable("userId") long userId, Model model) {
		model.addAttribute("dialog", "change_password");

		Optional<Person> user = staffManagement.findById(userId);
		if (user.isPresent()) {
			model.addAttribute("person", user.get());
		}

		Optional<Festival> festival = festivalManagement.findById(festivalId);
		if (festival.isPresent()) {
			model.addAttribute("festival", festival.get());
		}

		return "person.html";
	}

	@PostMapping("/staff/{festivalId}/change_role/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public String changeName(@PathVariable("festivalId") long festivalId, @PathVariable("id") long id, ChangeRoleForm form) {
		this.staffManagement.changeRole(form);

		return "redirect:/staff/" + festivalId + "/detail/" + id;
	}

	@PostMapping("/staff/{festivalId}/change_password/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public String changeName(@PathVariable("festivalId") long festivalId, @PathVariable("id") long id, ChangePasswordForm form) {
		this.staffManagement.changePassword(form);

		return "redirect:/staff/" + festivalId + "/detail/" + id;
	}
}
