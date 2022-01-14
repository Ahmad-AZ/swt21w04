package festivalmanager.utils;


import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


/**
 * This class is an implementation of @link{springframework.boot.web.servlet.error.ErrorController}
 * that replaces the whitelable error page.
 * In case an error occurs, it displays a page informing the user about the details of the error.
 * @author Jan Biedermann
 */
@Controller
public class CustomErrorController implements ErrorController {


	private ErrorAttributes errorAttributes;
	private Map<String, Object> errorAttributesMap;
	private final boolean debugPrints = true;


	CustomErrorController() {
		errorAttributes = new DefaultErrorAttributes();
	}


	/**
	 * Used to pass on the title of the error page to the page header
	 * @return title attribute for the error page
	 */
	@ModelAttribute("title")
	public String getTitle() {
		return "Error";
	}


	@GetMapping("/error")
	public String catchError(HttpServletRequest httpRequest)  {

		ErrorAttributeOptions options = ErrorAttributeOptions.defaults();
		options = options.including(
				ErrorAttributeOptions.Include.STACK_TRACE,
				ErrorAttributeOptions.Include.MESSAGE);

		errorAttributesMap =
				errorAttributes.getErrorAttributes(new ServletWebRequest(httpRequest), options);

		return "redirect:/errorPage/false";
	}


	@GetMapping("/errorPage/{showDetails}")
	public String errorPage(Model model, @PathVariable boolean showDetails) {

		attributeHelper(model, "error", "Unbekannter Fehler");
		attributeHelper(model, "message", "-");
		attributeHelper(model, "path", "-");
		attributeHelper(model, "trace", "-");

		model.addAttribute("showDetails", showDetails);
		return "error";
	}


	private void attributeHelper(Model model, String name, String defaultValue) {

		if (errorAttributesMap.containsKey(name)) {

			if (name == "error" && errorAttributesMap.get("error") == "None") {
				model.addAttribute(name, "-");
				return;
			}

			if (name == "message" &&
					errorAttributesMap.get("message") == "No message available") {
				model.addAttribute(name, defaultValue);
				return;
			}

			if (debugPrints) {
				System.out.println(name + ": " + errorAttributesMap.get(name));
			}

			model.addAttribute(name, errorAttributesMap.get(name));

		} else {

			model.addAttribute(name, defaultValue);
		}
	}

}
