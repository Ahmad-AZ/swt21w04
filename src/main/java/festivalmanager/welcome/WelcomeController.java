/*
 * Copyright 2014-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package festivalmanager.welcome;

import festivalmanager.staff.Person;
import festivalmanager.staff.StaffManagement;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class WelcomeController {
	private final StaffManagement staffManagement;

	public WelcomeController(StaffManagement staffManagement) {
		Assert.notNull(staffManagement, "StaffManagement must not be null!");

		this.staffManagement = staffManagement;
	}

	@GetMapping("/")
	public String index(Model model, @LoggedIn Optional<UserAccount> account) {
		if (account.isPresent()) {
			if (account.get().hasRole(Role.of("ADMIN")) ||
				account.get().hasRole(Role.of("MANAGER")) ||
				account.get().hasRole(Role.of("PLANNER"))) {
				return "index";
			} else {
				// staff logged in therefore show festival overview page
				Optional<Person> person = staffManagement.findByUserAccount(account.get());
				if (person.isPresent()) {
					return "redirect:/festivalOverview/" + person.get().getFestivalId();
				}
			}
		}

		// visitor page
		return "redirect:/festivalOverview/";
	}
}
