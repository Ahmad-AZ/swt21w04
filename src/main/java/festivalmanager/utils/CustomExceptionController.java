package festivalmanager.utils;


import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;


@Controller
@ControllerAdvice
public class CustomExceptionController {


	CustomErrorController customErrorController;


	CustomExceptionController(CustomErrorController customErrorController) {
		this.customErrorController = customErrorController;
	}


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