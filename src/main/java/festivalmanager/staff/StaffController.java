package festivalmanager.staff;

import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.staff.forms.*;
import festivalmanager.utils.UtilsManagement;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.data.util.Streamable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class StaffController {
	private final StaffManagement staffManagement;
	private final FestivalManagement festivalManagement;
	private final UtilsManagement utilsManagement;

	public StaffController(StaffManagement staffManagement, FestivalManagement festivalManagement,
						   UtilsManagement utilsManagement) {
		Assert.notNull(staffManagement, "StaffManagement must not be null!");
		Assert.notNull(festivalManagement, "FestivalManagement must not be null!");
		this.staffManagement = staffManagement;
		this.festivalManagement = festivalManagement;
		this.utilsManagement = utilsManagement;
	}

	@ModelAttribute("title")
	public String getTitle() {
		return "Personal";
	}

	@ModelAttribute("entries")
	public Streamable<Person> getStaffList(@PathVariable("festivalId") long festivalId) {
		return staffManagement.findByFestivalId(festivalId);
	}

	@ModelAttribute("festival")
	public Festival getFestival(@PathVariable("festivalId") long festivalId) {
		return festivalManagement.findById(festivalId).orElse(null);
	}

	@GetMapping("/staff/{festivalId}")
	@PreAuthorize("hasRole('ADMIN')")
	public String getStaffInfo(@PathVariable("festivalId") long festivalId, Model model) {
		utilsManagement.setCurrentPageLowerHeader("staff");
		utilsManagement.prepareModel(model);
		return "staff.html";
	}

	@GetMapping("/staff/{festivalId}/detail/{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	public String getPersonDetailView(@PathVariable("festivalId") long festivalId, @PathVariable("userId") long userId, Model model) {
		Optional<Person> user = staffManagement.findById(userId);
		model.addAttribute("person", user.orElse(null));

		utilsManagement.prepareModel(model);
		return "staff.html";
	}

	@GetMapping(value = {"/staff/{festivalId}/create", "/staff/{festivalId}/create/{error}"})
	@PreAuthorize("hasRole('ADMIN')")
	public String getCreateStaffDialog(@PathVariable("festivalId") long festivalId, @PathVariable("error") Optional<String> error, Model model) {
		model.addAttribute("dialog", "create_staff");
		model.addAttribute("error", error.orElse(""));

		utilsManagement.prepareModel(model);
		return "staff.html";
	}

	@GetMapping("/staff/{festivalId}/remove/{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	public String getRemoveStaffDialog(@PathVariable("festivalId") long festivalId, @PathVariable("userId") long userId, Model model) {
		model.addAttribute("dialog", "remove_staff");

		Optional<Person> user = staffManagement.findById(userId);
		model.addAttribute("person", user.orElse(null));

		utilsManagement.prepareModel(model);
		return "staff.html";
	}

	@GetMapping("/staff/{festivalId}/change_role/{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	public String getChangeRoleDialog(@PathVariable("festivalId") long festivalId, @PathVariable("userId") long userId, Model model) {
		model.addAttribute("dialog", "change_role");

		Optional<Person> user = staffManagement.findById(userId);
		model.addAttribute("person", user.orElse(null));

		utilsManagement.prepareModel(model);
		return "staff.html";
	}

	@GetMapping("/staff/{festivalId}/change_password/{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	public String getChangePasswordDialog(@PathVariable("festivalId") long festivalId, @PathVariable("userId") long userId, Model model) {
		model.addAttribute("dialog", "change_password");

		Optional<Person> user = staffManagement.findById(userId);
		model.addAttribute("person", user.orElse(null));

		utilsManagement.prepareModel(model);
		return "staff.html";
	}

	@PostMapping("/staff/{festivalId}/create")
	@PreAuthorize("hasRole('ADMIN')")
	public String createStaff(@PathVariable("festivalId") long festivalId, CreateStaffForm form) {
		try {
			this.staffManagement.createPerson(festivalId, form);
		} catch (IllegalArgumentException exception) {
			return "redirect:/staff/" + festivalId + "/create/" + exception.getLocalizedMessage();
		}

		return "redirect:/staff/" + festivalId;
	}

	@PostMapping("/staff/{festivalId}/remove")
	@PreAuthorize("hasRole('ADMIN')")
	public String removeStaff(@PathVariable("festivalId") long festivalId, RemoveStaffForm form) {
		this.staffManagement.removePerson(form);

		return "redirect:/staff/" + festivalId;
	}

	@PostMapping("/staff/{festivalId}/change_role/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public String changeRole(@PathVariable("festivalId") long festivalId, @PathVariable("id") long id, ChangeRoleForm form) {
		this.staffManagement.changeRole(form);

		return "redirect:/staff/" + festivalId + "/detail/" + id;
	}

	@PostMapping("/staff/{festivalId}/change_password/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public String changePassword(@PathVariable("festivalId") long festivalId, @PathVariable("id") long id, ChangePasswordForm form) {
		this.staffManagement.changePassword(form);

		return "redirect:/staff/" + festivalId + "/detail/" + id;
	}
}
