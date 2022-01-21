package festivalmanager.utils;


import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;


/**
 * A class used to catch exceptions and pass them on to an instance of {@link CustomErrorController},
 * where an error page for the user is generated
 * @author Jan Biedermann
 */
@Controller
@ControllerAdvice
public class CustomExceptionController {


	CustomErrorController customErrorController;


	/**
	 * Creates a new instance of CustomExceptionController
	 * @param customErrorController an instance of {@link CustomErrorController}
	 */
	CustomExceptionController(CustomErrorController customErrorController) {
		this.customErrorController = customErrorController;
	}


	/**
	 * If an exception is thrown anywhere in the software, it is passed on to this function,
	 * which then either: <p>
	 * a) Rethrows it if the exception is declared with the annotation @ResponseStatus,
	 * in which case the exception is handled by the spring framework <p>
	 * b) Passes it on to a {@link CustomErrorController}, where an error page is created
	 * @param httpRequest the http request which caused the exception
	 * @param exception the exception that was caught
	 * @return an error page for the exception, unless <p>
	 * a) exception is an instance of {@link AccessDeniedException}, in which case the login page is returned <p>
	 * b) exception is an instance of {@link MaxUploadSizeExceededException}
	 * and occurred on a newLocation or saveLocation page from {@link festivalmanager.location.LocationController},
	 * in which case the user is redirect to an "Image too large" page
	 * @throws Exception if the caught exception is declared with the annotation @ResponseStatus,
	 * it is rethrown and handled by the spring framework
	 */
	@ExceptionHandler(value = Exception.class)
	public String catchException(HttpServletRequest httpRequest, Exception exception,
								 RedirectAttributes redirectAttributes) throws Exception {

		String uri = httpRequest.getRequestURI();

		if (AnnotationUtils.findAnnotation(exception.getClass(), ResponseStatus.class) != null) {
			throw exception;
		}

		if (exception.getClass().equals(AccessDeniedException.class)) {
			return "redirect:/login";
		}

		if (exception.getClass().equals(MaxUploadSizeExceededException.class) &&
				(uri.endsWith("/saveLocation") || uri.endsWith("/newLocation"))) {

			String redirectString = "";
			redirectAttributes.addFlashAttribute("message", "Bilder sind zu groß.");

			if (uri.endsWith("/saveLocation")) {
				redirectString = uri.replace("/saveLocation", "");
			} else {
				redirectString = "/newLocation";
			}

			return "redirect:" + redirectString;
		}

		return customErrorController.catchError(httpRequest);
	}

}