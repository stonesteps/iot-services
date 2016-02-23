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

import com.bwg.iot.model.Alert;
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

import java.time.LocalDateTime;
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
public final class AlertDocumentation {

	@Rule
	public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

	@Autowired
	private AlertRepository alertRepository;

	@Autowired
	private SpaRepository spaRepository;

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
	public void alertListExample() throws Exception {
		this.alertRepository.deleteAll();


		this.mockMvc.perform(get("/alerts")).andExpect(status().isOk())
				.andDo(document("alerts-list-example",
						responseFields(
								fieldWithPath("_embedded.alerts")
										.description("An array of <<resources-alert, Alert resources>>"),
						fieldWithPath("_links").description("<<resources-alertslist-links,Links>> to other resources"),
						fieldWithPath("page").description("Page information"))));
	}

	@Test
	public void alertCreateExample() throws Exception {
		this.alertRepository.deleteAll();

        String now = LocalDateTime.now().toString();
		final Map<String, String> alert = new HashMap<>();
		alert.put("severityLevel", "yellow");
		alert.put("name", "ReplaceFilter");
		alert.put("shortDescription", "Replace Filter");
		alert.put("longDescription", "Your filter needs to be replaced. Order at www.bwg.com");
		alert.put("component", "filter1");
		alert.put("spaId", "abc00000000000000001");
		alert.put("creationDate", now);

		this.mockMvc
				.perform(post("/alerts").contentType(MediaTypes.HAL_JSON)
						.content(this.objectMapper.writeValueAsString(alert)))
				.andExpect(status().isCreated())
				.andDo(document("alert-create-example",
						requestFields(fieldWithPath("name").description("Name of the alert"),
								fieldWithPath("severityLevel").description("The Severity of the Alert (yellow, red)"),
								fieldWithPath("spaId").description("The spa associated with the alert"),
								fieldWithPath("shortDescription").description("A brief description of the alert"),
                                fieldWithPath("longDescription").description("The full text of the alert"),
                                fieldWithPath("component").description("The spa component related to the alert"),
                                fieldWithPath("creationDate").description("The timestamp the alert was created"))));
	}

	@Test
	public void alertUpdateExample() throws Exception {
		this.alertRepository.deleteAll();

		final Alert alert = createAlert("ReplaceFilter", "yellow", "Replace Filter", "The filter is old, please replace", "filter1", "mySpa001");

		final Map<String, String> alertUpdate = new HashMap<>();
		alertUpdate.put("severityLevel", "Red");

		this.mockMvc
				.perform(patch("/alerts/{0}", alert.getAlertId()).contentType(MediaTypes.HAL_JSON)
						.content(this.objectMapper.writeValueAsString(alertUpdate)))
				.andExpect(status().is2xxSuccessful())
				.andDo(document("alert-update-example",
						requestFields(fieldWithPath("severityLevel").description("Severity of the alert"))));
	}

	@Test
	public void alertGetExample() throws Exception {
        this.alertRepository.deleteAll();

        final Alert alert = createAlert("ReplaceFilter", "yellow", "Replace Filter", "The filter is old, please replace", "filter1", "mySpa001");

		this.mockMvc.perform(get("/alerts/{0}", alert.getAlertId())).andExpect(status().isOk())
				.andExpect(jsonPath("name", is(alert.getName())))
				.andExpect(jsonPath("severityLevel", is(alert.getSeverityLevel())))
				.andExpect(jsonPath("shortDescription", is(alert.getShortDescription())))
                .andExpect(jsonPath("longDescription", is(alert.getLongDescription())))
                .andExpect(jsonPath("component", is(alert.getComponent())))
                .andExpect(jsonPath("spaId", is(alert.getSpaId())))
				.andDo(document("alert-get-example",
						links(linkWithRel("self").description("This <<resources-alert,alert>>"),
								linkWithRel("alert").description("This <<resources-alert,alert>>")),
						responseFields(fieldWithPath("name").description("The name of the alert"),
								fieldWithPath("severityLevel").description("The severity of the alert (yellow, red)"),
								fieldWithPath("shortDescription").description("A brief description of the alert"),
								fieldWithPath("longDescription").description("The full text of the alert"),
                                fieldWithPath("component").description("The spa component reporting the alert"),
                                fieldWithPath("spaId").description("The spa associated with the alert"),
                                fieldWithPath("creationDate").description("The timestamp of when the alert occurred"),
                                fieldWithPath("_links")
										.description("<<resources-alert-links,Links>> to other resources"))));
	}

	private Alert createAlert(String name, String severity, String shortDesc, String fullDesc, String component, String spaId) {
        String now = LocalDateTime.now().toString();
		final Alert alert = new Alert();
		alert.setName(name);
		alert.setSeverityLevel(severity);
		alert.setShortDescription(shortDesc);
		alert.setLongDescription(fullDesc);
		alert.setComponent(component);
		alert.setSpaId(spaId);
		alert.setCreationDate(now);
		return alertRepository.save(alert);
	}
}
