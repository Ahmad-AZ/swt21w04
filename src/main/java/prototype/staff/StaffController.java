package prototype.staff;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class StaffController {
	private final StaffRepository staffRepository;
	private final UserDetailsManager userDetailsManager;

	public StaffController(StaffRepository staffRepository, UserDetailsManager userDetailsManager) {
		this.staffRepository = staffRepository;
		this.userDetailsManager = userDetailsManager;
	}

	@GetMapping("/staff")
	@PreAuthorize("hasRole('ADMIN')")
	public String getStaffInfo(Model model) {
		model.addAttribute("entries", staffRepository.findAll());

		return "staff.html";
	}

	@GetMapping("/staff/create")
	@PreAuthorize("hasRole('ADMIN')")
	public String getCreateStaffDialog(Model model) {
		model.addAttribute("entries", staffRepository.findAll());
		model.addAttribute("dialog", "create_staff");

		return "staff.html";
	}

	@GetMapping("/staff/remove/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public String getRemoveStaffDialog(@PathVariable("id") long id, Model model) {
		model.addAttribute("entries", staffRepository.findAll());
		model.addAttribute("currentId", id);
		model.addAttribute("dialog", "remove_staff");

		Optional<Person> user = staffRepository.findById(id);
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
		this.staffRepository.save(new Person(form.getName()));
		userDetailsManager.createUser(
				User.withDefaultPasswordEncoder()
				.username(form.getName())
				.password(form.getPassword())
				.roles("USER")
				.build()
		);

		return "staff.html";
	}

	@PostMapping("/staff/remove")
	@PreAuthorize("hasRole('ADMIN')")
	public String removeStaff(RemoveStaffForm form) {
		Optional<Person> user = this.staffRepository.findById(form.getId());
		if (user.isPresent()) {
			userDetailsManager.deleteUser(user.get().getName());
		}
		this.staffRepository.deleteById(form.getId());

		return "staff.html";
	}
}
