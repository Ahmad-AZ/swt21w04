package festivalmanager.festival;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
@AutoConfigureMockMvc
class FestivalControllerWebIntegrationTests {

	@Autowired MockMvc mvc;
	@Autowired FestivalController controller;


	@Test
	@WithMockUser(roles = "ADMIN")
	void addFestivalIsAccessableForAdmin() throws Exception {

		mvc.perform(get("/newFestival")).andExpect(status().isOk())
										.andExpect(model().attributeExists("dateNow"));
	}
	
	@Test
	void preventsPublicAccessForAddFestival() throws Exception {

		mvc.perform(get("/newFestival")).andExpect(status().isFound()) 
					.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));//
	}
	
//	@Test
//	@WithMockUser(roles = "PLANNER")
//	void preventsPlannerAccessForDeleteFestival() throws Exception {
//
//		mvc.perform(get("/festivalOverview/remove/4")).andExpect(status().isFound()) 
//					.andExpect(header().string(HttpHeaders.LOCATION, endsWith("/login")));//
//	}
}