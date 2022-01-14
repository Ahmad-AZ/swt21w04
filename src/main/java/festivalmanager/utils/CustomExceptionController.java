package festivalmanager.utils;


import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

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
	 * @return an error page for the exception,
	 * unless exception is an instance of {@link AccessDeniedException}, in which case the login page is returned
	 * @throws Exception if the caught exception is declared with the annotation @ResponseStatus,
	 * it is rethrown and handled by the spring framework
	 */
	@ExceptionHandler(value = Exception.class)
	public String catchException(HttpServletRequest httpRequest, Exception exception) throws Exception {

		if (AnnotationUtils.findAnnotation(exception.getClass(), ResponseStatus.class) != null) {
			throw exception;
		}

		if (exception.getClass().equals(AccessDeniedException.class)) {
			return "redirect:/login";
		}

		return customErrorController.catchError(httpRequest);
	}

}