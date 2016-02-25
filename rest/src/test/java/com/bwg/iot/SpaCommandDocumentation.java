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
import com.bwg.iot.model.Owner;
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
public final class SpaCommandDocumentation {

	@Rule
	public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

	@Autowired
	private SpaCommandRepository spaCommandRepository;

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
	public void setDesiredTempExample() throws Exception {
		this.spaCommandRepository.deleteAll();

		final Map<String, String> command = new HashMap<>();
		command.put("desiredTemp","101");
        command.put("originatorId","myspacommand000001");

		this.mockMvc
				.perform(post("/control/56c7f020c2e65656ab93db17/setDesiredTemp").contentType(MediaTypes.HAL_JSON)
						.content(this.objectMapper.writeValueAsString(command)))
				.andExpect(status().is2xxSuccessful())
				.andDo(document("control-desired-temp-example",
						requestFields(fieldWithPath("desiredTemp").description("The desired temperatue in degrees Fahrenheit"),
								fieldWithPath("originatorId").description("Origination ID").optional())))
				.andDo(document("control-desired-temp-response-example",
						responseFields(fieldWithPath("id").description("Unique Id of the control request"),
                                fieldWithPath("spaId").description("Unique Id for the spa"),
								fieldWithPath("requestTypeId").description("The type of request"),
								fieldWithPath("originatorId").description("A unique id for this request"),
								fieldWithPath("sentTimestamp").description("The time the command was sent"),
								fieldWithPath("processedTimestamp").description("The time the command was processed"),
								fieldWithPath("ackTimestamp").description("The time the spa acknowledged the command"),
								fieldWithPath("values").description("The set temperature"))));
	}

}