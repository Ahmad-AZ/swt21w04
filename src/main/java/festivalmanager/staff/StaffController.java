package festivalmanager.staff;

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

	public StaffController(StaffManagement staffManagement) {
		Assert.notNull(staffManagement, "StaffManagement must not be null!");
		this.staffManagement = staffManagement;
	}

	@GetMapping("/staff")
	@PreAuthorize("hasRole('ADMIN')")
	public String getStaffInfo(Model model) {
		model.addAttribute("entries", staffManagement.findAll());

		return "staff.html";
	}

	@GetMapping("/staff/create")
	@PreAuthorize("hasRole('ADMIN')")
	public String getCreateStaffDialog(Model model) {
		model.addAttribute("entries", staffManagement.findAll());
		model.addAttribute("dialog", "create_staff");

		return "staff.html";
	}

	@GetMapping("/staff/remove/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public String getRemoveStaffDialog(@PathVariable("id") long id, Model model) {
		model.addAttribute("entries", staffManagement.findAll());
		model.addAttribute("currentId", id);
		model.addAttribute("dialog", "remove_staff");

		Optional<Person> user = staffManagement.findById(id);
		if (user.isPresent()) {
			model.addAttribute("currentName", user.get().getName());
		} else {
			model.addAttribute("currentName", "");
		}

		return "staff.html";
	}

	@PostMapping("/staff/create")
	@PreAuthorize("hasRole('ADMIN')")
	public String createStaff(CreateStaffForm form) {
		this.staffManagement.createPerson(form);

		return "staff.html";
	}

	@PostMapping("/staff/remove")
	@PreAuthorize("hasRole('ADMIN')")
	public String removeStaff(RemoveStaffForm form) {
		this.staffManagement.removePerson(form);

		return "staff.html";
	}
}
