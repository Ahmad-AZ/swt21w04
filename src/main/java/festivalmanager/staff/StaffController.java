package festivalmanager.staff;

import festivalmanager.festival.Festival;
import festivalmanager.festival.FestivalManagement;
import festivalmanager.location.Location;
import festivalmanager.staff.forms.*;
import festivalmanager.utils.UtilsManagement;
import org.salespointframework.useraccount.Role;
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

/**
 * Controller for the staff management tab
 * @author Georg Kunze
 */
@Controller
public class StaffController {
	private final StaffManagement staffManagement;
	private final FestivalManagement festivalManagement;
	private final UtilsManagement utilsManagement;

	/**
	 * constructor for the controller
	 * @param staffManagement		the {@link StaffManagement}, must not be {@literal null}
	 * @param festivalManagement	the {@link FestivalManagement}, must not be {@literal null}
	 * @param utilsManagement		the {@link UtilsManagement}, must not be {@literal null}
	 */
	public StaffController(StaffManagement staffManagement, FestivalManagement festivalManagement,
						   UtilsManagement utilsManagement) {
		Assert.notNull(staffManagement, "StaffManagement must not be null!");
		Assert.notNull(festivalManagement, "FestivalManagement must not be null!");
		Assert.notNull(utilsManagement, "UtilsManagement must not be null!");

		this.staffManagement = staffManagement;
		this.festivalManagement = festivalManagement;
		this.utilsManagement = utilsManagement;
	}

	/**
	 * function to set the "title" model attribute for use in the header.
	 * @return the title to use
	 */
	@ModelAttribute("title")
	public String getTitle() {
		return "Personal";
	}

	/**
	 * function to set the staff list model attribute of the current festival.
	 * @param festivalId		the id of the current festival
	 * @return					a list of all the staff for the specified festival
	 */
	@ModelAttribute("entries")
	public Streamable<Person> getStaffList(@PathVariable("festivalId") long festivalId) {
		return staffManagement.findByFestivalId(festivalId);
	}

	/**
	 * function to set the "festival" model attribute.
	 * @param festivalId		the id of the current festival
	 * @return					the festival
	 */
	@ModelAttribute("festival")
	public Festival getFestival(@PathVariable("festivalId") long festivalId) {
		return festivalManagement.findById(festivalId).orElse(null);
	}

	/**
	 * function to set an "infoMessage" model attribute,
	 * which is used to show whether security staff is missing for the specified festival.
	 * @param festivalId		the id of the current festival
	 * @return
	 */
	@ModelAttribute("infoMessage")
	public String getInfoMessage(@PathVariable("festivalId") long festivalId) {
		Optional<Festival> festival = festivalManagement.findById(festivalId);
		if (festival.isPresent()) {
			long requiredSecurity = festival.get().getRequiredSecurityCount();
			long availableSecurity = staffManagement.getAvailableSecurityCount(festivalId);

			if (availableSecurity < requiredSecurity) {
				return "Es werden mehr Sicherheitskräfte benötigt!";
			}
		}
		return null;
	}

	/**
	 * mapping for the main staff site, displays the staff list for the current festival
	 * @param model				the model for the website template
	 * @param festivalId		the id of the current festival
	 * @return					the template to use for the website
	 */
	@GetMapping("/staff/{festivalId}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public String getStaffInfo(Model model, @PathVariable("festivalId") Long festivalId) {
		utilsManagement.prepareModel(model, festivalId);
		return "staff.html";
	}

	/**
	 * mapping for the detail view of a person
	 * @param userId			the id of the selected person
	 * @param model				the model for the website template
	 * @param festivalId		the id of the current festival
	 * @return					the template to use for the website
	 */
	@GetMapping("/staff/{festivalId}/detail/{userId}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public String getPersonDetailView(@PathVariable("userId") long userId, Model model,
									  @PathVariable("festivalId") Long festivalId) {
		Optional<Person> user = staffManagement.findById(userId);
		model.addAttribute("person", user.orElse(null));

		utilsManagement.prepareModel(model, festivalId);
		return "staff.html";
	}

	/**
	 * mapping for showing the create-staff dialog
	 * @param error				an error message from a previous attempt
	 * @param model				the model for the website template
	 * @param festivalId		the id of the current festival
	 * @return					the template to use for the website
	 */
	@GetMapping(value = {"/staff/{festivalId}/create", "/staff/{festivalId}/create/{error}"})
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public String getCreateStaffDialog(@PathVariable("error") Optional<String> error, Model model,
									   @PathVariable("festivalId") Long festivalId) {
		model.addAttribute("dialog", "create_staff");
		model.addAttribute("error", error.orElse(""));

		utilsManagement.prepareModel(model, festivalId);
		return "staff.html";
	}

	/**
	 * mapping for showing the dialog to delete a user
	 * @param userId			the id of the selected user
	 * @param model				the model for the website template
	 * @param festivalId		the id of the current festival
	 * @return					the template to use for the website
	 */
	@GetMapping("/staff/{festivalId}/remove/{userId}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public String getRemoveStaffDialog(@PathVariable("userId") long userId, Model model,
									   @PathVariable("festivalId") Long festivalId) {
		model.addAttribute("dialog", "remove_staff");

		Optional<Person> user = staffManagement.findById(userId);
		model.addAttribute("person", user.orElse(null));

		utilsManagement.prepareModel(model, festivalId);
		return "staff.html";
	}

	/**
	 * mapping for showing the dialog to change the role of a user
	 * @param userId			the id of the selected user
	 * @param error				an error message from a previous attempt
	 * @param model				the model for the website template
	 * @param festivalId		the id of the current festival
	 * @return					the template to use for the website
	 */
	@GetMapping(value = {"/staff/{festivalId}/change_role/{userId}", "/staff/{festivalId}/change_role/{userId}/{error}"})
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public String getChangeRoleDialog(@PathVariable("userId") long userId, @PathVariable("error") Optional<String> error, Model model,
									  @PathVariable("festivalId") Long festivalId) {
		model.addAttribute("dialog", "change_role");
		model.addAttribute("error", error.orElse(""));

		Optional<Person> user = staffManagement.findById(userId);
		model.addAttribute("person", user.orElse(null));

		utilsManagement.prepareModel(model, festivalId);
		return "staff.html";
	}

	/**
	 * mapping for showing the dialog to change the password of a user
	 * @param userId			the id of the selected user
	 * @param model				the model for the website template
	 * @param festivalId		the id of the current festival
	 * @return					the template to use for the website
	 */
	@GetMapping("/staff/{festivalId}/change_password/{userId}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public String getChangePasswordDialog(@PathVariable("userId") long userId, Model model,
										  @PathVariable("festivalId") Long festivalId) {
		model.addAttribute("dialog", "change_password");

		Optional<Person> user = staffManagement.findById(userId);
		model.addAttribute("person", user.orElse(null));

		utilsManagement.prepareModel(model, festivalId);
		return "staff.html";
	}

	/**
	 * mapping for showing the dialog to change the salary of a user
	 * @param userId			the id of the selected user
	 * @param model				the model for the website template
	 * @param festivalId		the id of the current festival
	 * @return					the template to use for the website
	 */
	@GetMapping("/staff/{festivalId}/change_salary/{userId}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public String getChangeSalaryDialog(@PathVariable("userId") long userId, Model model,
										  @PathVariable("festivalId") Long festivalId) {
		model.addAttribute("dialog", "change_salary");

		Optional<Person> user = staffManagement.findById(userId);
		model.addAttribute("person", user.orElse(null));

		utilsManagement.prepareModel(model, festivalId);
		return "staff.html";
	}

	/**
	 * mapping to submit the CreateStaffForm and create the user
	 * @param festivalId		the id of the current festival
	 * @param form				the submitted form
	 * @param userAccount		the account of the user creating the new user to check for permissions
	 * @return					a redirect to the staff page or the create-staff dialog if an error occurred
	 */
	@PostMapping("/staff/{festivalId}/create")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public String createStaff(@PathVariable("festivalId") long festivalId, CreateStaffForm form, UserAccount userAccount) {
		if (form.getRole().equals("ADMIN") && !userAccount.hasRole(Role.of("ADMIN"))) {
			return "redirect:/staff/" + festivalId + "/create/Only other Admins can create a user with the role to ADMIN";
		}

		try {
			this.staffManagement.createPerson(festivalId, form);
		} catch (IllegalArgumentException exception) {
			return "redirect:/staff/" + festivalId + "/create/" + exception.getLocalizedMessage();
		}

		return "redirect:/staff/" + festivalId;
	}

	/**
	 * mapping to submit the RemoveStaffForm and remove the user
	 * @param festivalId		the id of the current festival
	 * @param form				the submitted form
	 * @return					a redirect to the staff page
	 */
	@PostMapping("/staff/{festivalId}/remove")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public String removeStaff(@PathVariable("festivalId") long festivalId, RemoveStaffForm form) {
		this.staffManagement.removePerson(form, festivalManagement.findById(festivalId).orElse(null));

		return "redirect:/staff/" + festivalId;
	}

	/**
	 * mapping to submit the ChangeRoleForm and change the role of the user
	 * @param festivalId		the id of the current festival
	 * @param id 				the id of the selected user
	 * @param form				the submitted form
	 * @param userAccount		the account of the user changing the role to check permissions
	 * @return					a redirect to the staff page or to the change-role dialog if an error occurred
	 */
	@PostMapping("/staff/{festivalId}/change_role/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public String changeRole(@PathVariable("festivalId") long festivalId, @PathVariable("id") long id, ChangeRoleForm form, UserAccount userAccount) {
		if (form.getRole().equals("ADMIN") && !userAccount.hasRole(Role.of("ADMIN"))) {
			return "redirect:/staff/" + festivalId + "/detail/" + id + "/Only other Admins can change the role to ADMIN";
		} else {
			this.staffManagement.changeRole(form);
			return "redirect:/staff/" + festivalId + "/detail/" + id;
		}
	}

	/**
	 * mapping to submit the ChangePasswordForm and change the password of the user
	 * @param festivalId		the id of the current festival
	 * @param id 				the id of the selected user
	 * @param form				the submitted form
	 * @return					a redirect to the staff page
	 */
	@PostMapping("/staff/{festivalId}/change_password/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public String changePassword(@PathVariable("festivalId") long festivalId, @PathVariable("id") long id, ChangePasswordForm form) {
		this.staffManagement.changePassword(form);

		return "redirect:/staff/" + festivalId + "/detail/" + id;
	}

	/**
	 * mapping to submit the ChangeSalaryForm and change the salary of the user
	 * @param festivalId		the id of the current festival
	 * @param id 				the id of the selected user
	 * @param form				the submitted form
	 * @return					a redirect to the staff page
	 */
	@PostMapping("/staff/{festivalId}/change_salary/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public String changeSalary(@PathVariable("festivalId") long festivalId, @PathVariable("id") long id, ChangeSalaryForm form) {
		this.staffManagement.changeSalary(form);

		return "redirect:/staff/" + festivalId + "/detail/" + id;
	}
}
