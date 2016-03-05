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

import com.bwg.iot.model.Address;
import com.bwg.iot.model.User;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IotServicesApplication.class)
@WebAppConfiguration
public final class TermsAndAgreementsDocumentation extends ModelTestBase{

	@Rule
	public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

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
	public void createTermsAndConditionsExample() throws Exception {

		final Map<String, String> terms = new HashMap<>();
		terms.put("text","These are the terms and conditions.\n Blah, blah.");
        terms.put("version","1.0");

		this.mockMvc
				.perform(post("/tac/").contentType(MediaTypes.HAL_JSON)
						.content(this.objectMapper.writeValueAsString(terms)))
				.andExpect(status().is2xxSuccessful())
				.andDo(document("tac-create-example",
						requestFields(fieldWithPath("text").description("The text of the terms and conditions"),
								fieldWithPath("version").description("A version number"))))
				.andDo(document("tac-create-response-example",
						responseFields(fieldWithPath("_id").description("Unique Id of the control request"),
                                fieldWithPath("text").description("Unique Id for the spa"),
								fieldWithPath("version").description("The version number of these Terms"),
								fieldWithPath("createdTimestamp").description("Timestamp the Terms were created"),
								fieldWithPath("current").description("True if the most recent. False if old"),
								fieldWithPath("_links").description("<<resources-index-links,Links>> to other resources"))));
	}

	@Test
	public void getCurrentTermsExample() throws Exception {

		final Map<String, String> terms = new HashMap<>();
		terms.put("text","These are the terms and conditions. Blah, blah.");
		terms.put("version","1.0");

		this.mockMvc
				.perform(post("/tac/").contentType(MediaTypes.HAL_JSON)
						.content(this.objectMapper.writeValueAsString(terms)))
				.andExpect(status().is2xxSuccessful());

        this.mockMvc.perform(get("/tac/search/findMostRecent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("current", is(true)))
                .andDo(document("tac-findMostRecent-example",
                    responseFields(fieldWithPath("_id").description("Unique Id of the control request"),
                            fieldWithPath("text").description("Unique Id for the spa"),
                            fieldWithPath("version").description("The version number of these Terms"),
                            fieldWithPath("createdTimestamp").description("Timestamp the Terms were created"),
                            fieldWithPath("current").description("True if the most recent. False if old"),
							fieldWithPath("_links").description("<<resources-index-links,Links>> to other resources"))));
	}

	@Test
	public void agreeToTermsExample() throws Exception {

        this.userRepository.deleteAll();
        this.addressRepository.deleteAll();

        Address address = createAddress();
        User user = createUser("Murphy", "Matt", "25111", "222", address, Arrays.asList("USER"), new Date().toString());

        final Map<String, String> agree = new HashMap<>();
		agree.put("userId", user.get_id());
		agree.put("version","0.0.4");

		this.mockMvc
				.perform(post("/tac/agree").contentType(MediaTypes.HAL_JSON)
						.content(this.objectMapper.writeValueAsString(agree)))
				.andExpect(status().is2xxSuccessful())
				.andDo(document("tac-agree-example",
						requestFields(fieldWithPath("userId").description("The user Id"),
								fieldWithPath("version").description("The version of the terms and conditions"))))
				.andDo(document("tac-agree-example",
						responseFields(fieldWithPath("_id").description("Unique Id"),
								fieldWithPath("userId").description("Unique Id for the User"),
								fieldWithPath("version").description("The version number of the Terms agreed to"),
								fieldWithPath("dateAgreed").description("Timestamp of the agreement"),
								fieldWithPath("current").description("Is this agreement current"),
								fieldWithPath("_links").description("<<resources-index-links,Links>> to other resources"))));
	}

	@Test
    public void getUserAgreement() throws Exception {

        this.userRepository.deleteAll();
        this.addressRepository.deleteAll();

        Address address = createAddress();
        User user = createUser("Murphy", "Matt", "25111", "222", address, Arrays.asList("USER"), new Date().toString());

        final Map<String, String> agree = new HashMap<>();
		agree.put("userId",user.get_id());
		agree.put("version","0.0.4");

        this.mockMvc
                .perform(post("/tac/agree").contentType(MediaTypes.HAL_JSON)
                        .content(this.objectMapper.writeValueAsString(agree)))
                .andExpect(status().is2xxSuccessful());

		this.mockMvc
				.perform(get("/tac/search/findCurrentUserAgreement?userId="+user.get_id()).contentType(MediaTypes.HAL_JSON))
				.andExpect(status().is2xxSuccessful())
				.andDo(document("tac-findAgreement-example",
						responseFields(fieldWithPath("_id").description("Unique Id"),
								fieldWithPath("userId").description("Unique Id for the User"),
								fieldWithPath("version").description("The version number of the Terms agreed to"),
								fieldWithPath("dateAgreed").description("Timestamp of the agreement"),
								fieldWithPath("current").description("Is this agreement current"),
								fieldWithPath("_links").description("<<resources-index-links,Links>> to other resources"))));

	}

}
