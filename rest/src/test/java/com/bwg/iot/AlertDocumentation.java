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
import com.bwg.iot.model.Spa;
import com.bwg.iot.model.User;
import org.junit.Assert;
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

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
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
public final class AlertDocumentation extends ModelTestBase {

	@Rule
	public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

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

		final Map<String, String> alert = new HashMap<>();
		alert.put("severityLevel", Alert.SeverityLevelEnum.ERROR.name());
		alert.put("name", "ReplaceFilter");
		alert.put("shortDescription", "Replace Filter");
		alert.put("longDescription", "Your filter needs to be replaced. Order at www.bwg.com");
		alert.put("component", "filter1");
		alert.put("spaId", "abc00000000000000001");
		alert.put("oemId", "oem001");
		alert.put("dealerId", "dealer001");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		alert.put("creationDate", simpleDateFormat.format(new Date()));

		this.mockMvc
				.perform(post("/alerts").contentType(MediaTypes.HAL_JSON)
						.content(this.objectMapper.writeValueAsString(alert)))
				.andExpect(status().isCreated())
				.andDo(document("alert-create-example",
						requestFields(fieldWithPath("name").description("Name of the alert"),
								fieldWithPath("severityLevel").description("The Severity of the Alert (INFO, WARNING, ERROR, SEVERE)"),
								fieldWithPath("spaId").description("The spa associated with the alert"),
								fieldWithPath("oemId").description("The manufacturer of this spa"),
								fieldWithPath("dealerId").description("The dealer that sold this spa"),
								fieldWithPath("shortDescription").description("A brief description of the alert"),
                                fieldWithPath("longDescription").description("The full text of the alert"),
                                fieldWithPath("component").description("The spa component related to the alert"),
                                fieldWithPath("creationDate").description("The timestamp the alert was created"))));
	}

	@Test
	public void alertUpdateExample() throws Exception {
		this.alertRepository.deleteAll();

		final Alert alert = createAlert("ReplaceFilter", Alert.SeverityLevelEnum.ERROR, "Replace Filter", "The filter is old, please replace", "filter1", "mySpa001");

		final Map<String, String> alertUpdate = new HashMap<>();
		alertUpdate.put("severityLevel", Alert.SeverityLevelEnum.SEVERE.name());

		this.mockMvc
				.perform(patch("/alerts/{0}", alert.get_id()).contentType(MediaTypes.HAL_JSON)
						.content(this.objectMapper.writeValueAsString(alertUpdate)))
				.andExpect(status().is2xxSuccessful())
				.andDo(document("alert-update-example",
						requestFields(fieldWithPath("severityLevel").description("Severity of the alert"))));
	}

	@Test
	public void alertClearExample() throws Exception {
		this.alertRepository.deleteAll();
        this.spaRepository.deleteAll();
        this.userRepository.deleteAll();

        User owner = createUser("eblues", "Elwood", "Blues", "dealer2509", "oem001", createAddress(), Arrays.asList("OWNER"), null);
        Spa spa = createFullSpaWithState("0blah345", "Shark", "Blue", "oem0000001", "101", owner, "mySpa001");
		Alert alert = createAlert("ReplaceFilter", Alert.SeverityLevelEnum.ERROR, "Replace Filter", "The filter is old, please replace", "filter1", spa.get_id());
        spa.getCurrentState().setAlertState(Alert.SeverityLevelEnum.ERROR.name());
        spaRepository.save(spa);

		this.mockMvc
				.perform(post("/alerts/{0}/clear", alert.get_id())
						.contentType(MediaTypes.HAL_JSON).header("remote_user", "userId"))
				.andExpect(status().is2xxSuccessful())
				.andDo(document("alert-clear-example"));

        spa = spaRepository.findByUsername("eblues");
        Assert.assertNotNull(spa);
        Assert.assertEquals(Alert.SeverityLevelEnum.NONE.name(), spa.getCurrentState().getAlertState());
    }

	@Test
	public void alertGetExample() throws Exception {
        this.alertRepository.deleteAll();

        final Alert alert = createAlert("ReplaceFilter", Alert.SeverityLevelEnum.ERROR, "Replace Filter", "The filter is old, please replace", "filter1", "mySpa001");

		this.mockMvc.perform(get("/alerts/{0}", alert.get_id())).andExpect(status().isOk())
				.andExpect(jsonPath("name", is(alert.getName())))
				.andExpect(jsonPath("severityLevel", is(alert.getSeverityLevel())))
				.andExpect(jsonPath("shortDescription", is(alert.getShortDescription())))
                .andExpect(jsonPath("longDescription", is(alert.getLongDescription())))
                .andExpect(jsonPath("component", is(alert.getComponent())))
                .andExpect(jsonPath("spaId", is(alert.getSpaId())))
				.andDo(document("alert-get-example",
						links(linkWithRel("self").description("This <<resources-alert,alert>>"),
								linkWithRel("alert").description("This <<resources-alert,alert>>")),
						responseFields(
								fieldWithPath("_id").description("Object Id"),
								fieldWithPath("name").description("The name of the alert"),
								fieldWithPath("severityLevel").description("The severity of the alert (INFO, WARNING, ERROR, SEVERE)"),
								fieldWithPath("shortDescription").description("A brief description of the alert"),
								fieldWithPath("longDescription").description("The full text of the alert"),
                                fieldWithPath("component").description("The spa component reporting the alert"),
                                fieldWithPath("spaId").description("The spa associated with the alert"),
								fieldWithPath("oemId").description("The manufacturer of this spa"),
								fieldWithPath("dealerId").description("The dealer that sold this spa"),
                                fieldWithPath("creationDate").description("The timestamp of when the alert occurred"),
                                fieldWithPath("_links")
										.description("<<resources-alert-links,Links>> to other resources"))));
	}


	@Test
	public void alertsFindByDealerExample() throws Exception {
		this.alertRepository.deleteAll();

		final Alert alert1 = createAlert("ReplaceFilter", Alert.SeverityLevelEnum.ERROR, "Replace Filter", "The filter is old, please replace", "filter1", "mySpa001");
		final Alert alert2 = createAlert("ReplaceFilter", Alert.SeverityLevelEnum.ERROR, "Replace Filter", "The filter is old, please replace", "filter1", "mySpa001");

		this.mockMvc.perform(get("/alerts/search/findByDealerId?dealerId=dealer001&sort=creationDate,desc"))
				.andExpect(status().isOk())
				.andDo(document("alerts-findbydealer-example",
						responseFields(
								fieldWithPath("_embedded.alerts").description("An array of <<resources-alert, Alert resources>>"),
								fieldWithPath("_links").description("<<resources-alertslist-links,Links>> to other resources"),
								fieldWithPath("page").description("Page information"))));
	}



	@Test
	public void alertsFindByOemExample() throws Exception {
		this.alertRepository.deleteAll();

		final Alert alert1 = createAlert("ReplaceFilter", Alert.SeverityLevelEnum.ERROR, "Replace Filter", "The filter is old, please replace", "filter1", "mySpa001");
		final Alert alert2 = createAlert("ReplaceFilter", Alert.SeverityLevelEnum.ERROR, "Replace Filter", "The filter is old, please replace", "filter1", "mySpa001");

		this.mockMvc.perform(get("/alerts/search/findByOemId?oemId=oem001&sort=creationDate,desc"))
				.andExpect(status().isOk())
				.andDo(document("alerts-findbyoem-example",
						responseFields(
								fieldWithPath("_embedded.alerts").description("An array of <<resources-alert, Alert resources>>"),
								fieldWithPath("_links").description("<<resources-alertslist-links,Links>> to other resources"),
								fieldWithPath("page").description("Page information"))));
	}

	@Test
	public void alertsFindBySpaExample() throws Exception {
		this.alertRepository.deleteAll();

		final Alert alert1 = createAlert("ReplaceFilter", Alert.SeverityLevelEnum.ERROR, "Replace Filter", "The filter is old, please replace", "filter1", "mySpa001");
		final Alert alert2 = createAlert("ReplaceFilter", Alert.SeverityLevelEnum.ERROR, "Replace Filter", "The filter is old, please replace", "filter1", "mySpa001");

		this.mockMvc.perform(get("/alerts/search/findBySpaId?spaId=mySpa001&sort=creationDate,desc"))
				.andExpect(status().isOk())
				.andDo(document("alerts-findbyspa-example",
						responseFields(
								fieldWithPath("_embedded.alerts").description("An array of <<resources-alert, Alert resources>>"),
								fieldWithPath("_links").description("<<resources-alertslist-links,Links>> to other resources"),
								fieldWithPath("page").description("Page information"))));
	}

	private Alert createAlert(String name, Alert.SeverityLevelEnum severity, String shortDesc, String fullDesc, String component, String spaId) {
		final Alert alert = new Alert();
		alert.setName(name);
		alert.setSeverityLevel(severity.name());
		alert.setShortDescription(shortDesc);
		alert.setLongDescription(fullDesc);
		alert.setComponent(component);
		alert.setSpaId(spaId);
		alert.setDealerId("dealer001");
		alert.setOemId("oem001");
		alert.setCreationDate(new Date());
		return alertRepository.save(alert);
	}
}
