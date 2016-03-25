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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bwg.iot.model.Address;
import com.bwg.iot.model.TacUserAgreement;
import com.bwg.iot.model.TermsAndConditions;
import com.bwg.iot.model.User;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IotServicesApplication.class)
@WebAppConfiguration
public class GettingStartedDocumentation extends ModelTestBase {
	
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
				.apply(documentationConfiguration(this.restDocumentation))
				.alwaysDo(document("{method-name}/{step}/"))
				.build();
	}

	@Test
	public void index() throws Exception {
		this.mockMvc.perform(get("/").accept(MediaTypes.HAL_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("_links.spas", is(notNullValue())));
	}

	@Test
    public void termsAndConditions() throws JsonProcessingException, Exception {
        clearAllData();
        List<Address> addresses = createAddresses(2);
        User owner1 = createUser("user0001", "braitt", "Bonnie", "Raitt", "dealer0001", "oem04", addresses.get(0), Arrays.asList("OWNER"));
        User owner2 = createUser("user0002", "stones", "Keith", "Richards", "dealer0001", "oem04", addresses.get(1), Arrays.asList("OWNER"));

        TermsAndConditions tac1 = createTermsAndAgreement("1.0", "The world is mine");
        TacUserAgreement agreement1 = createAgreement(owner1.get_id(), tac1.getVersion());

        login(owner1);

        findAgreement(owner1);
        dontFindAgreement(owner2);
        findMostRecentTermsAndConditions();
        agreeToTerms(owner2, tac1);

	}


	private ResultActions login(User owner) throws Exception {
		Map<String, String> login = new HashMap<String, String>();
		login.put("username", owner.getUsername());
		login.put("password", "*******");

		ResultActions loginLocation = this.mockMvc
				.perform(
						post("/auth/login").contentType(MediaTypes.HAL_JSON).content(
								objectMapper.writeValueAsString(login)))
				.andExpect(status().is2xxSuccessful());
		return loginLocation;
	}

    private ResultActions findAgreement(User user) throws Exception {
        return this.mockMvc.perform(
                        get("/tac/search/findCurrentUserAgreement?userId="+user.get_id()))
                .andExpect(status().is2xxSuccessful());
    }

    private ResultActions dontFindAgreement(User user) throws Exception {
        return this.mockMvc.perform(
                get("/tac/search/findCurrentUserAgreement?userId="+user.get_id()))
                .andExpect(status().is4xxClientError());
    }

    private ResultActions findMostRecentTermsAndConditions() throws Exception {
        return this.mockMvc.perform(
                get("/tac/search/findMostRecent"))
                .andExpect(status().isOk());
    }
    private ResultActions agreeToTerms(User user, TermsAndConditions terms) throws Exception {
        Map<String, String> agree = new HashMap<String, String>();
        agree.put("userId", user.get_id());
        agree.put("version", terms.getVersion());

        ResultActions agreement = this.mockMvc
                .perform(
                        post("/tac/agree").contentType(MediaTypes.HAL_JSON).content(
                                objectMapper.writeValueAsString(agree)))
                .andExpect(status().is2xxSuccessful());
        return agreement;
    }


	private String getLink(MvcResult result, String rel)
			throws UnsupportedEncodingException {
		return JsonPath.parse(result.getResponse().getContentAsString()).read(
				"_links." + rel + ".href");
	}
}
