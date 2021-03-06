/*
 * Copyright 2014-2019 the original author or authors.
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
package festivalmanager;

import java.io.File;
import java.nio.file.Paths;

import org.salespointframework.EnableSalespoint;
import org.salespointframework.SalespointSecurityConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableSalespoint
@EnableScheduling
public class Application {

	public final static String LOCATION_UPLOAD_DIR = "locationImages";
	public final static String QR_UPLOAD_DIR = "qrCodes";
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Configuration
	static class WebSecurityConfiguration extends SalespointSecurityConfiguration {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable();  // for lab purposes, that's ok!
			http.authorizeRequests().antMatchers("/**").permitAll().and()
					.formLogin().loginProcessingUrl("/login").and()
					.logout().logoutUrl("/logout").logoutSuccessUrl("/");
		}
	}
	
	@Configuration
	static class WebConfiguration implements WebMvcConfigurer {
				
		@Override
		public void addResourceHandlers(ResourceHandlerRegistry registry) {
			
			// create Folder for locationImages
			new File(Paths.get(Application.LOCATION_UPLOAD_DIR).toAbsolutePath().toString()+ "/").mkdir();
			
			// create Folder for qrCodes
			new File(Paths.get(Application.QR_UPLOAD_DIR).toAbsolutePath().toString()+ "/").mkdir();
			
			registry.addResourceHandler("/locationImages/**").addResourceLocations("file:./"+ LOCATION_UPLOAD_DIR + "/");
			registry.addResourceHandler("/qrCodes/**").addResourceLocations("file:./"+ QR_UPLOAD_DIR + "/");
		}
	}
}
