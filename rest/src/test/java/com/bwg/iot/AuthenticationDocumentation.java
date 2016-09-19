/*
 * Copyright 2014-2015 the original author or authors.
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

package com.bwg.iot;

import com.bwg.iot.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.hateoas.MediaTypes;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IotServicesApplication.class)
@WebAppConfiguration
public final class AuthenticationDocumentation extends ModelTestBase{

	@Rule
	public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

	@Autowired
	private UserRepository Repository;

	@Autowired
	private OemRepository oemRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
				.apply(documentationConfiguration(this.restDocumentation)).build();
	}


	@Test
	public void whoamiExample() throws Exception {
		this.userRepository.deleteAll();

		List<Address> addresses = createAddresses(4);
		Oem oem1 = createOem("Sundance Spas", "103498", addresses.get(1), "oem001");
		Dealer dealer1 = createDealer("Pt. Loma Spas", addresses.get(2), oem1.get_id(), "dealer001");
		Attachment logo = createLogoAttachment(mockMvc);
		dealer1.setLogo(logo);
		dealerRepository.save(dealer1);

		User owner5 = createUser("user0005", "chynde", "Chrissie", "Hynde", "dealer001", "oem001", addresses.get(0), Arrays.asList("OWNER"), null);
		Spa spa27 = createDemoSpa("160315", "Whale", "Beluga","oem02", "dealer0002", owner5, "spa000027", null);

		this.mockMvc.perform(get("/auth/whoami")
				.header("remote_user", "chynde@mailinator.com"))
				.andExpect(status().isOk())
				.andDo(document("whoami-example",
						responseFields(
								fieldWithPath("_id").description("Object Id"),
								fieldWithPath("username").description("Unique string for the user"),
								fieldWithPath("firstName").description("First name of the user"),
								fieldWithPath("lastName").description("Last name of the user"),
								fieldWithPath("fullName").description("First and Last name"),
								fieldWithPath("dealerId").description("Dealer id"),
								fieldWithPath("oemId").description("Manufacturer id"),
								fieldWithPath("spaId").description("If the user owns a spa, the Id of the spa").optional().type("String"),
								fieldWithPath("roles").description("User roles. Supported role values: OWNER, ASSOCIATE, TECHNICIAN, DEALER, OEM, BWG, ADMIN").type("List<String>"),
								fieldWithPath("email").description("The user's email address"),
								fieldWithPath("phone").description("The user's phone number"),
								fieldWithPath("address").description("User's address"),
								fieldWithPath("createdDate").description("User creation date").type("Date"),
								fieldWithPath("modifiedDate").description("Date of last update").optional().type("Date"),
								fieldWithPath("_links")
										.description("<<resources-user-links,Links>> to other resources"),
								fieldWithPath("notes").description("User's notes"))));
	}

	@Test
	public void tokenEndpointExample() throws Exception {
		this.mockMvc
				.perform(get("/idm/tokenEndpoint"))
				.andExpect(status().is2xxSuccessful())
				.andDo(document("token-endpoint-example",
						responseFields(fieldWithPath("mobileClientId").description("Gluu Client ID, used to create basic "
								+ "Authentication http header to include when calling the tokenEndpoint to authenticate "
								+ "a mobile end user. This value is used as the username when creating the basic auth header."),
							fieldWithPath("mobileClientSecret").description("Gluu Client Secret, used to create basic "
								+ "Authentication http header to include when calling the tokenEndpoint to authenticate "
								+ "a mobile end user. This value is used as the password when creating the basic auth header."),
							fieldWithPath("_links.tokenEndpoint").description("Link to the Identity Manager "
								+ "Token Endpoint REST URL used to authenticate a mobile app end user"),
							fieldWithPath("_links.refreshEndpoint").description("Link to the Identity Manager "
								+ "Token Refresh REST URL used to get a fresh token when a mobile user token expires."),
								fieldWithPath("_links.whoami").description("Link to obtain User Details"))));
	}
}
