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
public final class TermsAndAgreementsDocumentation {

	@Rule
	public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

	@Autowired
	private TermsAndConditionsRepository termsAndConditionsRepository;

	@Autowired
	private TacUserAgreementRepository tacUserAgreementRepository;

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
						responseFields(fieldWithPath("id").description("Unique Id of the control request"),
                                fieldWithPath("text").description("Unique Id for the spa"),
								fieldWithPath("version").description("The version number of these Terms"),
								fieldWithPath("createdTimestamp").description("Timestamp the Terms were created"),
								fieldWithPath("current").description("True if the most recent. False if old"))));
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
                    responseFields(fieldWithPath("id").description("Unique Id of the control request"),
                            fieldWithPath("text").description("Unique Id for the spa"),
                            fieldWithPath("version").description("The version number of these Terms"),
                            fieldWithPath("createdTimestamp").description("Timestamp the Terms were created"),
                            fieldWithPath("current").description("True if the most recent. False if old"))));
	}

	@Test
	public void agreeToTermsExample() throws Exception {

		final Map<String, String> agree = new HashMap<>();
		agree.put("userId","The User Identifier");
		agree.put("version","The version of the Terms and Conditions that the user agreed to.");

		this.mockMvc
				.perform(post("/tac/agree").contentType(MediaTypes.HAL_JSON)
						.content(this.objectMapper.writeValueAsString(agree)))
				.andExpect(status().is2xxSuccessful())
				.andDo(document("tac-agree-example",
						requestFields(fieldWithPath("userId").description("The user Id"),
								fieldWithPath("version").description("The version of the terms and conditions"))))
				.andDo(document("tac-agree-example",
						responseFields(fieldWithPath("id").description("Unique Id"),
								fieldWithPath("userId").description("Unique Id for the User"),
								fieldWithPath("version").description("The version number of the Terms agreed to"),
								fieldWithPath("dateAgreed").description("Timestamp of the agreement"),
								fieldWithPath("current").description("Is this agreement current"))));
	}


}
