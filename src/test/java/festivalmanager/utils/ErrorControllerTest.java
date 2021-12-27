package festivalmanager.utils;


import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import static org.assertj.core.api.Assertions.assertThat;


public class ErrorControllerTest {


	@Test
	void testErrorController() {

		CustomErrorController customErrorController = new CustomErrorController();
		MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
		mockHttpServletRequest.setAttribute("javax.servlet.error.message", "testMessage");
		mockHttpServletRequest.setAttribute("javax.servlet.error.request_uri", "/someUnknownPage");

		Model testModel = new ExtendedModelMap();
		customErrorController.catchError(mockHttpServletRequest);
		customErrorController.errorPage(testModel, true);

		assertThat(testModel.getAttribute("message")).isEqualTo("testMessage");
		assertThat(testModel.getAttribute("path")).isEqualTo("/someUnknownPage");
		assertThat(testModel.getAttribute("trace")).isEqualTo("-");
	}
}
