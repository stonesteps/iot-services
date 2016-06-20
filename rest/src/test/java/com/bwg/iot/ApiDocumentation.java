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

import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.reflect.Field;
import java.util.*;

import javax.servlet.RequestDispatcher;

import com.bwg.iot.model.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.hateoas.MediaTypes;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IotServicesApplication.class)
@WebAppConfiguration
public class ApiDocumentation extends ModelTestBase{
	
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
	public void errorExample() throws Exception {
		this.mockMvc
				.perform(get("/error")
						.requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 400)
						.requestAttr(RequestDispatcher.ERROR_REQUEST_URI,
								"/spas")
						.requestAttr(RequestDispatcher.ERROR_MESSAGE,
								"The owner 'http://localhost:8080/owners/123' does not exist"))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("error", is("Bad Request")))
				.andExpect(jsonPath("timestamp", is(notNullValue())))
				.andExpect(jsonPath("status", is(400)))
				.andExpect(jsonPath("path", is(notNullValue())))
				.andDo(document("error-example",
						responseFields(
								fieldWithPath("error").description("The HTTP error that occurred, e.g. `Bad Request`"),
								fieldWithPath("message").description("A description of the cause of the error"),
								fieldWithPath("path").description("The path to which the request was made"),
								fieldWithPath("status").description("The HTTP status code, e.g. `400`"),
								fieldWithPath("timestamp").description("The time, in milliseconds, at which the error occurred"))));
	}

	@Test
	public void indexExample() throws Exception {
		this.mockMvc.perform(get("/"))
			.andExpect(status().isOk())
			.andDo(document("index-example",
					links(
							linkWithRel("spas").description("The <<resources-spas,Spa resource>>"),
                            linkWithRel("addresses").description("The <<resources-addresses,Addresses resource>>"),
                            linkWithRel("alerts").description("The <<resources-alerts,Alert resource>>"),
                            linkWithRel("spaCommands").description("The <<resources-spaCommands,SpaCommand resource>>"),
							linkWithRel("dealers").description("The <<resources-dealers,Dealers resource>>"),
							linkWithRel("oems").description("The <<resources-oems,Oem resource>>"),
							linkWithRel("users").description("The <<resources-users,User resource>>"),
							linkWithRel("components").description("The <<resources-components,Component resource>>"),
							linkWithRel("materials").description("The <<resources-materials, Material resource>>"),
							linkWithRel("spaTemplates").description("The <<resources-spaTemplates, SpaTemplate resource>>"),
							linkWithRel("materials").description("The <<resources-materials,Material resource>>"),
							linkWithRel("profile").description("The ALPS profile for the service"),
							linkWithRel("attachments").description("An endpoint for handling attachments")),
					responseFields(
							fieldWithPath("_links").description("<<resources-index-links,Links>> to other resources"))));

	}

	@Test
	public void spasListExample() throws Exception {
		this.spaRepository.deleteAll();

		User owner = createUser("eblues", "Elwood", "Blues", null, null, createAddress(), Arrays.asList("OWNER"), null);
		createUnsoldSpa("01924094", "Shark", "Mako", "oem000001", "101");
        createUnsoldSpa("01000000", "Shark", "Hammerhead", "oem000003", "101");
		createFullSpaWithState("0blah345", "Shark", "Land", "oem0000001", "101", owner);

		this.mockMvc.perform(get("/spas"))
			.andExpect(status().isOk())
			.andDo(document("spas-list-example",
					responseFields(
							fieldWithPath("_embedded.spas").description("An array of <<resources-spa, Spa resources>>"),
                            fieldWithPath("_links").description("<<resources-spaslist-links,Links>> to other resources"),
                            fieldWithPath("page").description("Page information"))));
	}

	@Test
	public void spasCreateExample() throws Exception {
		Map<String, Object> spa = new HashMap<String, Object>();
		spa.put("serialNumber", "2000");
        spa.put("productName", "Shark");
        spa.put("model", "Sand");
        spa.put("dealerId", "101");

		this.mockMvc.perform(
				post("/spas").contentType(MediaTypes.HAL_JSON).content(
						this.objectMapper.writeValueAsString(spa))).andExpect(
				status().isCreated())
				.andDo(document("spas-create-example",
						requestFields(
									fieldWithPath("serialNumber").description("The serial of the spa"),
									fieldWithPath("productName").description("The product name of the spa"),
                                    fieldWithPath("model").description("The spa model"),
                                    fieldWithPath("dealerId").description("The dealer assigned to the spa"))));
	}

	@Test
	public void spaGetExample() throws Exception {

		User owner = createUser("eblues", "Elwood", "Blues", "dealer2509", "oem001", createAddress(), Arrays.asList("OWNER"), null);
		User sales1 = createUser("user0010", "nfinn", "Neil", "Finn", "dealer2509", "oem001", createAddress(), Arrays.asList(User.Role.ASSOCIATE.name()), null);
		User tech1  = createUser("user0013", "wgates", "William", "Gates", "dealer2509", "oem001", createAddress(), Arrays.asList(User.Role.TECHNICIAN.name()), null);
        Spa spa = createFullSpaWithState("0blah345", "Shark", "Blue", "oem0000001", "101", owner, null, sales1, null, tech1);
        spa = this.addLowFlowYellowAlert(spa);

		String spaLocation = "/spas/"+spa.get_id();

		this.mockMvc.perform(get(spaLocation))
			.andExpect(status().isOk())
			.andExpect(jsonPath("serialNumber", is(spa.getSerialNumber())))
			.andExpect(jsonPath("productName", is(spa.getProductName())))
            .andExpect(jsonPath("model", is(spa.getModel())))
            .andExpect(jsonPath("dealerId", is(spa.getDealerId())))
			.andExpect(jsonPath("_links.self.href", containsString(spaLocation)))
            .andExpect(jsonPath("_links.spa.href", containsString(spaLocation)))
			.andDo(document("spa-get-example",
					links(
							linkWithRel("self").description("This <<resources-spa,spa>>"),
                            linkWithRel("spa").description("This <<resources-spa,spa>>"),
							linkWithRel("owner").description("This <<resources-user,user>>"),
							linkWithRel("faultLogs").description("This <<resources-faultLog,faultLog>>"),
							linkWithRel("wifiStats").description("This <<resources-wifiStat,wifiStat>>"),
							linkWithRel("events").description("This <<resources-event,event>>"),
							linkWithRel("AC Current measurements").description("This <<resources-acCurrentMeasurements,measurementReading>>"),
							linkWithRel("Ambient Temp measurements").description("This <<resources-ambientTempMeasurements,measurementReading>>"),
							linkWithRel("turnOffSpa").description("Shut Down Spa command"),
							linkWithRel("recipes").description("This <<resources-recipes, recipe>>")),
					responseFields(
							fieldWithPath("_id").description("Object Id"),
                            fieldWithPath("serialNumber").description("The serial of the spa"),
                            fieldWithPath("productName").description("The product name of the spa"),
                            fieldWithPath("model").description("The spa model"),
                            fieldWithPath("owner").description("The owner of the spa").optional().type(User.class),
							fieldWithPath("associate").description("Person who sold the spa").optional().type(User.class),
							fieldWithPath("technician").description("The person assigned to service the spa").optional().type(User.class),
							fieldWithPath("transactionCode").description("Optional ticket number of the sale").optional().type("String"),
							fieldWithPath("salesDate").description("Date this spa was sold").optional().type(Date.class),
							fieldWithPath("sold").description("Flag denoting if spa has been sold"),
							fieldWithPath("online").description("Boolean, is spa communicating with cloud"),
                            fieldWithPath("alerts").description("Current Issues with the spa"),
                            fieldWithPath("dealerId").description("The dealer assigned to the spa"),
                            fieldWithPath("oemId").description("The manufacturer that built spa"),
                            fieldWithPath("currentState").description("Latest readings from the spa"),
                            fieldWithPath("manufacturedDate").description("The date the spa was made"),
                            fieldWithPath("registrationDate").description("The date the spa was sold"),
                            fieldWithPath("p2pAPSSID").description("Wifi address"),
							fieldWithPath("location").description("Array of doubles representing location of spa [longitude, latitude], can be null"),
							fieldWithPath("_links").description("<<resources-spa-links,Links>> to other resources"))));
	}

	@Test
	public void spasFindByDealerExample() throws Exception {
		this.spaRepository.deleteAll();

		createUnsoldSpa("01924094", "Shark", "Mako", "oem00001", "101");
		createUnsoldSpa("01000000", "Shark", "Hammerhead", "oem00001", "101");
		createUnsoldSpa("013t43tt", "Shark", "Nurse", "oem00001", "101");
		createUnsoldSpa("0blah345", "Shark", "Land", "oem00001", "101");

		this.mockMvc.perform(get("/spas/search/findByDealerId?dealerId=101"))
				.andExpect(status().isOk())
				.andDo(document("spas-findbyDealer-example",
						responseFields(
								fieldWithPath("_embedded.spas").description("An array of <<resources-spa, Spa resources>>"),
								fieldWithPath("_links").description("<<resources-spaslist-links,Links>> to other resources"),
								fieldWithPath("page").description("Page information"))));
	}

	@Test
	public void spasFindByOemExample() throws Exception {
		this.spaRepository.deleteAll();

		createUnsoldSpa("01924094", "Shark", "Mako", "oem00001", "101");
		createUnsoldSpa("01000000", "Shark", "Hammerhead", "oem00001", "101");
		createUnsoldSpa("013t43tt", "Shark", "Nurse", "oem00001", "101");
		createUnsoldSpa("0blah345", "Shark", "Land", "oem00001", "101");

		this.mockMvc.perform(get("/spas/search/findByOemId?oemId=oem00001"))
				.andExpect(status().isOk())
				.andDo(document("spas-findbyOem-example",
						responseFields(
								fieldWithPath("_embedded.spas").description("An array of <<resources-spa, Spa resources>>"),
								fieldWithPath("_links").description("<<resources-spaslist-links,Links>> to other resources"),
								fieldWithPath("page").description("Page information"))));
	}

	@Test
	public void spasFindByUsername() throws Exception {

		clearAllData();

		List<Address> addresses = createAddresses(1);
		List<String> ownerRole = Arrays.asList("OWNER");
		User owner1 = createUser("braitt", "Bonnie", "Raitt", null, null, addresses.get(0), ownerRole, null);
		Spa spa24 = createSmallSpaWithState("160104", "Shark", "Tiger", "oem00003", "101", owner1);

		this.mockMvc.perform(get("/spas/search/findByUsername?username=braitt"))
				.andExpect(status().isOk())
				.andDo(document("spas-findbyUsername-example",
					links(
							linkWithRel("self").description("This <<resources-spa,spa>>"),
							linkWithRel("spa").description("This <<resources-spa,spa>>"),
							linkWithRel("owner").description("This <<resources-user,user>>"),
							linkWithRel("faultLogs").description("This <<resources-faultLog,faultLog>>"),
							linkWithRel("wifiStats").description("This <<resources-wifiStat,wifiStat>>"),
							linkWithRel("events").description("This <<resources-event,event>>"),
							linkWithRel("AC Current measurements").description("This <<resources-acCurrentMeasurements,measurementReading>>"),
							linkWithRel("Ambient Temp measurements").description("This <<resources-ambientTempMeasurements,measurementReading>>"),
							linkWithRel("turnOffSpa").description("Shut Down Spa command"),
							linkWithRel("recipes").description("This <<resources-recipes, recipe>>")),
					responseFields(
							fieldWithPath("_id").description("Object Id"),
							fieldWithPath("serialNumber").description("The serial of the spa"),
							fieldWithPath("productName").description("The product name of the spa"),
							fieldWithPath("model").description("The spa model"),
							fieldWithPath("owner").description("The owner of the spa"),
							fieldWithPath("associate").description("Person who sold the spa").optional().type(User.class),
							fieldWithPath("technician").description("The person assigned to service the spa").optional().type(User.class),
							fieldWithPath("transactionCode").description("Optional ticket number of the sale").optional().type("String"),
							fieldWithPath("salesDate").description("Date this spa was sold").optional().type(Date.class),
							fieldWithPath("sold").description("Flag denoting if spa has been sold"),
							fieldWithPath("location").description("Contains longitude and latitude of spa location").optional().type("Map<double, double>"),
							fieldWithPath("online").description("Boolean, is spa communicating with cloud"),
							fieldWithPath("alerts").description("Current Issues with the spa").optional().type(Alert.class),
							fieldWithPath("dealerId").description("The dealer assigned to the spa"),
							fieldWithPath("oemId").description("The manufacturer that built spa").optional().type(String.class),
							fieldWithPath("currentState").description("Latest readings from the spa"),
							fieldWithPath("manufacturedDate").description("The date the spa was made"),
							fieldWithPath("registrationDate").description("The date the spa was sold"),
							fieldWithPath("p2pAPSSID").description("Wifi address"),
							fieldWithPath("_links").description("<<resources-spa-links,Links>> to other resources"))));
	}

	@Test
	public void spaUpdateExample() throws Exception {
		Map<String, Object> spa = new HashMap<String, Object>();
        spa.put("serialNumber", "2000");
        spa.put("productName", "Shark");
        spa.put("model", "Sand");

		String spaLocation = this.mockMvc
				.perform(
						post("/spas").contentType(MediaTypes.HAL_JSON).content(
								this.objectMapper.writeValueAsString(spa)))
				.andExpect(status().isCreated()).andReturn().getResponse()
				.getHeader("Location");

		this.mockMvc.perform(get(spaLocation)).andExpect(status().isOk())
                .andExpect(jsonPath("serialNumber", is(spa.get("serialNumber"))))
                .andExpect(jsonPath("productName", is(spa.get("productName"))))
                .andExpect(jsonPath("model", is(spa.get("model"))))
				.andExpect(jsonPath("_links.self.href", is(spaLocation)));


		Map<String, Object> spaUpdate = new HashMap<String, Object>();
		spaUpdate.put("dealerId", "101");

		this.mockMvc.perform(
				put(spaLocation).contentType(MediaTypes.HAL_JSON).content(
						this.objectMapper.writeValueAsString(spaUpdate)))
				.andExpect(status().isNoContent())
				.andDo(document("spa-update-example",
						requestFields(
                                fieldWithPath("serialNumber").description("The serial of the spa").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("productName").description("The product name of the spa").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("model").description("The spa model").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("dealerId").description("The dealer assigned to the spa").type(JsonFieldType.STRING).optional())));
	}

	@Test
	public void faultLogListExample() throws Exception {
		this.spaRepository.deleteAll();
		this.faultLogRepository.deleteAll();
		this.faultLogDescriptionRepository.deleteAll();

		User owner = createUser("eblues", "Elwood", "Blues", null, null, createAddress(), Arrays.asList("OWNER"), null);
		final Spa spa = createFullSpaWithState("0blah345", "Shark", "Land", "oem0000001", "101", owner);
		createSpaFaultLogAndDescription(spa.get_id());

		this.mockMvc.perform(get("/spas/"+spa.get_id()+"/faultLogs"))
				.andExpect(status().isOk())
				.andDo(document("faultlog-list-example",
						responseFields(
								fieldWithPath("_embedded.faultLogs").description("An array of <<resources-spa, Spa resources>>"),
								fieldWithPath("_links").description("<<resources-spaslist-links,Links>> to other resources"),
								fieldWithPath("page").description("Page information"))));
	}

	@Test
	public void wifiStatListExample() throws Exception {
		this.spaRepository.deleteAll();
		this.wifiStatRepository.deleteAll();

		User owner = createUser("eblues", "Elwood", "Blues", null, null, createAddress(), Arrays.asList("OWNER"), null);
		final Spa spa = createFullSpaWithState("0blah345", "Shark", "Land", "oem0000001", "101", owner);
		createSpaWifiStat(spa.get_id(), WifiConnectionHealth.AVG);
		createSpaWifiStat(spa.get_id(), WifiConnectionHealth.DISCONNECTED);
		createSpaWifiStat(spa.get_id(), WifiConnectionHealth.STRONG);
		createSpaWifiStat(spa.get_id(), WifiConnectionHealth.UNKONWN);
		createSpaWifiStat(spa.get_id(), WifiConnectionHealth.WEAK);

		this.mockMvc.perform(get("/spas/"+spa.get_id()+"/wifiStats"))
				.andExpect(status().isOk())
				.andDo(document("wifistat-list-example",
						responseFields(
								fieldWithPath("_embedded.wifiStats").description("An array of wifi stats"),
								fieldWithPath("_links").description("links to other resources"),
		                        fieldWithPath("page").description("Page information"))));
	}

	@Test
	public void eventListExample() throws Exception {
		this.spaRepository.deleteAll();
		this.eventRepository.deleteAll();

		User owner = createUser("eblues", "Elwood", "Blues", null, null, createAddress(), Arrays.asList("OWNER"), null);
		final Spa spa = createFullSpaWithState("0blah345", "Shark", "Land", "oem0000001", "101", owner);
		createSpaEvent(spa.get_id(), "ALERT");
		createSpaEvent(spa.get_id(), "MEASUREMENT");
		createSpaEvent(spa.get_id(), "NOTIFICATION");
		createSpaEvent(spa.get_id(), "REQEUST");

		this.mockMvc.perform(get("/spas/"+spa.get_id()+"/events"))
				.andExpect(status().isOk())
				.andDo(document("event-list-example",
						responseFields(
								fieldWithPath("_embedded.events").description("An array of events"),
								fieldWithPath("_links").description("Links to other resources"),
								fieldWithPath("page").description("Page information"))));
	}

	@Test
	public void acCurrentMeasurementsExample() throws Exception {
		this.spaRepository.deleteAll();
		this.measurementReadingRepository.deleteAll();

		User owner = createUser("eblues", "Elwood", "Blues", null, null, createAddress(), Arrays.asList("OWNER"), null);
		final Spa spa = createFullSpaWithState("0blah345", "Shark", "Land", "oem0000001", "101", owner);
		createMeasurementReading(spa.get_id(), "AC_CURRENT", "milliamps");
		createMeasurementReading(spa.get_id(), "AC_CURRENT", "milliamps");
		createMeasurementReading(spa.get_id(), "AC_CURRENT", "milliamps");

		this.mockMvc.perform(get("/spas/"+spa.get_id()+"/measurements?measurementType="+"AC_CURRENT"))
				.andExpect(status().isOk())
				.andDo(document("acCurrentMeasurements-list-example",
						responseFields(
								fieldWithPath("_embedded.measurementReadings").description("An array of measurements"),
								fieldWithPath("_links").description("Links to other resources"),
								fieldWithPath("page").description("Page information"))));
	}

	@Test
	public void ambientTempMeasurementsExample() throws Exception {
		this.spaRepository.deleteAll();
		this.measurementReadingRepository.deleteAll();

		User owner = createUser("eblues", "Elwood", "Blues", null, null, createAddress(), Arrays.asList("OWNER"), null);
		final Spa spa = createFullSpaWithState("0blah345", "Shark", "Land", "oem0000001", "101", owner);
		createMeasurementReading(spa.get_id(), "AMBIENT_TEMP", "celsius");
		createMeasurementReading(spa.get_id(), "AMBIENT_TEMP", "celsius");
		createMeasurementReading(spa.get_id(), "AMBIENT_TEMP", "celsius");

		this.mockMvc.perform(get("/spas/"+spa.get_id()+"/measurements?measurementType="+"AMBIENT_TEMP"))
				.andExpect(status().isOk())
				.andDo(document("ambientTempMeasurements-list-example",
						responseFields(
								fieldWithPath("_embedded.measurementReadings").description("An array of measurements"),
								fieldWithPath("_links").description("Links to other resources"),
								fieldWithPath("page").description("Page information"))));
	}

	@Test
	public void sellSpaExample() throws Exception {
		clearAllData();

		List<Address> addresses = createAddresses(3);
		List<String> ownerRole = Arrays.asList("OWNER");
		User owner1 = createUser("npeart", "Neal", "Peart", null, null, addresses.get(0), ownerRole, null);
		User associate = createUser("nwilson", "Nancy", "Wilson", "101", "oem001", addresses.get(1), Arrays.asList(User.Role.ASSOCIATE.toString()), null);
		User tech = createUser("awilson", "Ann", "Wilson", "101", "oem001", addresses.get(2), Arrays.asList(User.Role.TECHNICIAN.toString()), null);
		Spa myNewSpa = createSmallSpaWithState("160104", "Shark", "Tiger", "oem001", "101", null);

		SellSpaRequest request = new SellSpaRequest();
		request.setOwnerId(owner1.get_id());
		request.setAssociateId(associate.get_id());
		request.setTechnicianId(tech.get_id());
		request.setTransactionCode("2750295290-3");

		this.mockMvc.perform(post("/spas/" + myNewSpa.get_id() + "/sellSpa")
				.contentType(MediaTypes.HAL_JSON)
				.content(this.objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andDo(document("spas-sell-example",
						requestFields(
								fieldWithPath("ownerId").description("The ID of the new owner of the spa"),
								fieldWithPath("associateId").description("The ID of the sales associate who sold the spa"),
								fieldWithPath("technicianId").description("Optional ID of the technician who will service the spa").optional(),
								fieldWithPath("transactionCode").description("Optional dealer sales transaction code").optional(),
								fieldWithPath("salesDate").description("Optional date and time the spa was sold. (if not provided, the current system time is used)").optional().type(Date.class))))
				.andDo(document("spas-sell-example",
						responseFields(
								fieldWithPath("owner").description("The new owner of the spa, <<User resources>>").type(User.class),
								fieldWithPath("associate").description("The associate who sold spa, <<User resources>>").type(User.class),
								fieldWithPath("technician").description("The technician assigned to service the spa, <<User resources>>").type(User.class).optional(),
								fieldWithPath("salesDate").description("The date the spa was sold").type(Date.class),
								fieldWithPath("transactionCode").description("An optional dealer transaction code for the sale.").type(String.class).optional(),
								fieldWithPath("sold").description("Flag for sorting").type(Boolean.class),
								fieldWithPath("_id").description("Id of this spa"),
								fieldWithPath("productName").description("Spa product line"),
								fieldWithPath("model").description("Model name of the spa"),
								fieldWithPath("serialNumber").description("Serial Number of this spa"),
								fieldWithPath("dealerId").description("ID of the dealer that sold the spa"),
								fieldWithPath("oemId").description("ID of the spa manufacturer"),
								fieldWithPath("currentState").ignored(),
						 		fieldWithPath("p2pAPSSID").ignored(),
								fieldWithPath("registrationDate").ignored(),
								fieldWithPath("manufacturedDate").ignored(),
								fieldWithPath("online").ignored())));
	}


	@Test
	public void buildSpaExample() throws Exception {
		clearAllData();

		// create set of materials
		Material t1Panel = createSpaTemplateMaterial("Panel", "6600-760");
		Material t1Controller = createSpaTemplateMaterial("Controller", "6600-761");
		Material t1Pump = createSpaTemplateMaterial("Captain's Chair", "DJAYGB-9173D");
		Material t1Gateway = createSpaTemplateMaterial("Gateway", "17092-83280-1b");
		List<Material> spaTemplate1List = Arrays.asList(t1Panel, t1Controller, t1Pump, t1Gateway);

		// create spaTemplates
		SpaTemplate st1 = createSpaTemplate("J-500 Luxury Collection", "J-585", "109834-1525-585", "oem001", spaTemplate1List);

		List<Address> addresses = createAddresses(3);
		User bwg = createUser("jpage", "Jimmy", "Page", null, "oem001", addresses.get(0), Arrays.asList(User.Role.OEM.toString()), null);

		Spa spa = new Spa();
		spa.setTemplateId(st1.get_id());
		spa.setSerialNumber("1001001001");

		List<Component> components = new ArrayList<>();
		Component c1 = new Component();
		c1.setName("Jets");
		c1.setSerialNumber("p001");
		c1.setSku("sku001001");
		c1.setComponentType(Component.ComponentType.PUMP.name());
		c1.setPort("0");
		components.add(c1);

		Component c2 = new Component();
		c2.setName("Controller");
		c2.setSerialNumber("c001");
		c2.setSku("525151");
		c2.setComponentType(Component.ComponentType.CONTROLLER.name());
		c2.setPort("0");
		components.add(c2);

		BuildSpaRequest request = new BuildSpaRequest();
		request.setSpa(spa);
		request.setComponents(components);

		this.mockMvc.perform(post("/spas/buildSpa")
				.header("remote_user", "jpage")
				.contentType(MediaTypes.HAL_JSON)
				.content(this.objectMapper.writeValueAsString(request)))
				.andExpect(status().is2xxSuccessful())
				.andDo(document("spas-build-example",
						requestFields(
								fieldWithPath("spa").description("A spa shell containing templateId and SerialNumber. May also include optional manufacturedDate."),
								fieldWithPath("components").description("A list of components used to build the spa"))))
				.andDo(document("spas-build-example",
						responseFields(
								fieldWithPath("_id").description("Unique Identifier for the newly created spa"),
								fieldWithPath("productName").description("The productName of the Spa (obtained by the SpaTemplate)").type(User.class),
								fieldWithPath("model").description("Model name of the Spa").type(User.class).optional(),
								fieldWithPath("serialNumber").description("Serial Number of this spa"),
								fieldWithPath("oemId").description("The ID of the manufacturer that built this spa").type(Date.class),
								fieldWithPath("templateId").description("A reference to the template used to build this spa").type(String.class).optional(),
								fieldWithPath("manufacturedDate").description("The date the spa was created").type(Date.class),
								fieldWithPath("sold").ignored(),
								fieldWithPath("online").ignored())));
	}

	@Test
	public void createRecipeExample() throws Exception {
		clearAllData();

		// create an owner
		// create a spa with a few components
		List<Address> addresses = createAddresses(3);
		List<String> ownerRole = Arrays.asList("OWNER");
		User owner1 = createUser("npeart", "Neal", "Peart", null, null, addresses.get(0), ownerRole, null);
		User associate = createUser("nwilson", "Nancy", "Wilson", "101", "oem001", addresses.get(1), Arrays.asList(User.Role.ASSOCIATE.toString()), null);
		User tech = createUser("awilson", "Ann", "Wilson", "101", "oem001", addresses.get(2), Arrays.asList(User.Role.TECHNICIAN.toString()), null);
		Spa myNewSpa = createSmallSpaWithState("160104", "Shark", "Tiger", "oem001", "101", null);

		// Build up SaveSettingsRequest
		HashMap<String, HashMap<String,String>> settings = new HashMap<>();
		HashMap<String,String> setTempValues = new HashMap<String, String>();
		setTempValues.put(SpaCommand.REQUEST_DESIRED_TEMP, "101");
		settings.put("HEATER",setTempValues);

		HashMap<String,String> setPumpValues = new HashMap<String, String>();
		setPumpValues.put(SpaCommand.REQUEST_DEVICE_NUMBER, "0");
		setPumpValues.put(SpaCommand.REQUEST_DESIRED_STATE, "HIGH");
		settings.put("PUMP", setPumpValues);

		HashMap<String,String> setLightValues = new HashMap<String, String>();
		setLightValues.put(SpaCommand.REQUEST_DEVICE_NUMBER, "0");
		setLightValues.put(SpaCommand.REQUEST_DESIRED_STATE, "HIGH");
		settings.put("LIGHT", setLightValues);

		JobSchedule sched = new JobSchedule();
		sched.setStartDate(new Date());
		sched.setEndDate(new Date(2016,12,12));
		sched.setCronExpression("0 0 12 ? * WED");
		sched.setDurationMinutes(60);
		sched.setTimeZone(TimeZone.getTimeZone("Pacific/Kwajalein"));
		sched.setEnabled(true);

		RecipeDTO request = new RecipeDTO();
		request.setName("Test Recipe");
		request.setNotes("This is a test.");
		request.setSettings(settings);
		request.setSchedule(sched);

		this.mockMvc.perform(post("/spas/" + myNewSpa.get_id() + "/recipes")
				.header("remote_user", "npeart")
				.contentType(MediaTypes.HAL_JSON)
				.content(this.objectMapper.writeValueAsString(request)))
				.andExpect(status().is2xxSuccessful())
				.andDo(document("spas-create-recipe-example",
						requestFields(
								fieldWithPath("name").description("A friendly name for this group of settings"),
								fieldWithPath("schedule").description("Job Sheduling Metadata, start date, end date, frequency, timezone").optional().type(JobSchedule.class),
                                fieldWithPath("notes").description("Text field for notes about these settings"),
								fieldWithPath("system").description("If true, recipe is may not be edited or deleted").type("boolean"),
								fieldWithPath("settings").description("A list of apa settings. Each enty is is the format <String, Map<String,String>  RequestType, Values"))))
				.andDo(document("spas-create-recipe-example",
						responseFields(
								fieldWithPath("_id").description("Unique Identifier for these settings"),
								fieldWithPath("name").description("The friendly name of these settings").type("String"),
								fieldWithPath("spaId").description("The spa associated with these settings. (or FACTORY for factory settings)").type("String"),
								fieldWithPath("settings").description("The set of commands to put the spa in the desired state"),
								fieldWithPath("schedule").description("Job Sheduling Metadata, start date, end date, frequency, timezone").optional().type(JobSchedule.class),
								fieldWithPath("notes").description("Text field for miscellaneous use").type("String").optional(),
								fieldWithPath("creationDate").description("The date the spa was created").type(Date.class).optional(),
								fieldWithPath("system").description("If true, recipe is may not be edited or deleted").type("boolean"),
								fieldWithPath("_links").description("<<resources-spa-links,Links>> to other resources"))));
	}

	@Test
	public void recipeListExample() throws Exception {
		clearAllData();

		// create an owner
		// create a spa with a few components
		List<Address> addresses = createAddresses(3);
		List<String> ownerRole = Arrays.asList("OWNER");
		User owner1 = createUser("npeart", "Neal", "Peart", null, null, addresses.get(0), ownerRole, null);
		User associate = createUser("nwilson", "Nancy", "Wilson", "101", "oem001", addresses.get(1), Arrays.asList(User.Role.ASSOCIATE.toString()), null);
		User tech = createUser("awilson", "Ann", "Wilson", "101", "oem001", addresses.get(2), Arrays.asList(User.Role.TECHNICIAN.toString()), null);
		Spa myNewSpa = createSmallSpaWithState("160104", "Shark", "Tiger", "oem001", "101", null);

		createSpaRecipe(myNewSpa.get_id(), "TGIF", "Some like it hot!");
        createSpaRecipe(myNewSpa.get_id(), "Kid Time", null);

		this.mockMvc.perform(get("/spas/" + myNewSpa.get_id() + "/recipes"))
				.andExpect(status().isOk())
				.andDo(document("recipe-list-example",
						responseFields(
								fieldWithPath("_embedded.recipeDToes").description("An array of <<resources-recipe, Recipe resources>>"),
								fieldWithPath("_links").description("<<resources-spaslist-links,Links>>"))));
	}

    @Test
    public void recipeGetExample() throws Exception {
        clearAllData();

        // create an owner
        // create a spa with a few components
        List<Address> addresses = createAddresses(3);
        List<String> ownerRole = Arrays.asList("OWNER");
        User owner1 = createUser("npeart", "Neal", "Peart", null, null, addresses.get(0), ownerRole, null);
        User associate = createUser("nwilson", "Nancy", "Wilson", "101", "oem001", addresses.get(1), Arrays.asList(User.Role.ASSOCIATE.toString()), null);
        User tech = createUser("awilson", "Ann", "Wilson", "101", "oem001", addresses.get(2), Arrays.asList(User.Role.TECHNICIAN.toString()), null);
        Spa myNewSpa = createSmallSpaWithState("160104", "Shark", "Tiger", "oem001", "101", null);

        Recipe recipe = createSpaRecipe(myNewSpa.get_id(), "TGIF", "Some like it hot!");

        this.mockMvc.perform(get("/spas/" + myNewSpa.get_id() + "/recipes/" + recipe.get_id()))
                .andExpect(status().isOk())
                .andDo(document("recipe-get-example",
                    responseFields(
                        fieldWithPath("_id").description("Unique Identifier for these settings"),
                        fieldWithPath("name").description("The friendly name of these settings").type("String"),
                        fieldWithPath("spaId").description("The spa associated with these settings. (or FACTORY for factory settings)").type("String"),
                        fieldWithPath("settings").description("The set of commands to put the spa in the desired state"),
						fieldWithPath("schedule").description("Job Sheduling Metadata, start date, end date, frequency, timezone").optional().type(JobSchedule.class),
                        fieldWithPath("notes").description("Text field for miscellaneous use").type("String").optional(),
                        fieldWithPath("creationDate").description("The date the spa was created").type(Date.class).optional(),
						fieldWithPath("system").description("If true, recipe is may not be edited or deleted").type("boolean"),
                        fieldWithPath("_links").description("<<resources-spa-links,Links>> to other resources"))));
    }

    @Test
    public void recipePutExample() throws Exception {
        clearAllData();

        // create an owner
        // create a spa with a few components
        List<Address> addresses = createAddresses(3);
        List<String> ownerRole = Arrays.asList("OWNER");
        User owner1 = createUser("npeart", "Neal", "Peart", null, null, addresses.get(0), ownerRole, null);
        User associate = createUser("nwilson", "Nancy", "Wilson", "101", "oem001", addresses.get(1), Arrays.asList(User.Role.ASSOCIATE.toString()), null);
        User tech = createUser("awilson", "Ann", "Wilson", "101", "oem001", addresses.get(2), Arrays.asList(User.Role.TECHNICIAN.toString()), null);
        Spa myNewSpa = createSmallSpaWithState("160104", "Shark", "Tiger", "oem001", "101", null);

        Recipe recipe = createSpaRecipe(myNewSpa.get_id(), "TGIF", "Some like it hot!");
		RecipeDTO dto = RecipeDTO.fromRecipe(recipe);
        Map<String, Object> update = new HashMap<String, Object>();
        update.put("_id", recipe.get_id());
        update.put("name", "FridayNightLights");
        update.put("notes", "Are you ready for some football?");
        update.put("settings", dto.getSettings());
        update.put("spaId", recipe.getSpaId());

        this.mockMvc
                .perform(put("/spas/" + myNewSpa.get_id() + "/recipes/" + recipe.get_id())
                        .contentType(MediaTypes.HAL_JSON)
                        .content(this.objectMapper.writeValueAsString(update)))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("recipe-put-example",
                        responseFields(
                                fieldWithPath("_id").description("Unique Identifier for these settings"),
                                fieldWithPath("name").description("The friendly name of these settings").type("String"),
                                fieldWithPath("spaId").description("The spa associated with these settings. (or FACTORY for factory settings)").type("String"),
                                fieldWithPath("settings").description("The set of commands to put the spa in the desired state"),
								fieldWithPath("schedule").description("Job Sheduling Metadata, start date, end date, frequency, timezone").optional().type(JobSchedule.class),
                                fieldWithPath("notes").description("Text field for miscellaneous use").type("String").optional(),
                                fieldWithPath("creationDate").description("The date the spa was created").type(Date.class).optional(),
								fieldWithPath("system").description("If true, recipe is may not be edited or deleted").type("boolean"),
                                fieldWithPath("_links").description("<<resources-spa-links,Links>> to other resources").optional().type("Links"))));
    }

    @Test
    public void recipeDeleteExample() throws Exception {
        clearAllData();

        // create an owner
        // create a spa with a few components
        List<Address> addresses = createAddresses(3);
        List<String> ownerRole = Arrays.asList("OWNER");
        User owner1 = createUser("npeart", "Neal", "Peart", null, null, addresses.get(0), ownerRole, null);
        User associate = createUser("nwilson", "Nancy", "Wilson", "101", "oem001", addresses.get(1), Arrays.asList(User.Role.ASSOCIATE.toString()), null);
        User tech = createUser("awilson", "Ann", "Wilson", "101", "oem001", addresses.get(2), Arrays.asList(User.Role.TECHNICIAN.toString()), null);
        Spa myNewSpa = createSmallSpaWithState("160104", "Shark", "Tiger", "oem001", "101", null);

        Recipe recipe = createSpaRecipe(myNewSpa.get_id(), "TGIF", "Some like it hot!");

        Map<String, Object> update = new HashMap<String, Object>();
        update.put("_id", recipe.get_id());
        update.put("name", "FridayNightLights");
        update.put("notes", "Are you ready for some football?");
        update.put("settings", recipe.getSettings());
        update.put("spaId", recipe.getSpaId());

        this.mockMvc
                .perform(delete("/spas/" + myNewSpa.get_id() + "/recipes/" + recipe.get_id()))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("recipe-delete-example"));
    }

	@Test
	public void runRecipeExample() throws Exception {
		clearAllData();

		// create an owner
		// create a spa with a few components
		List<Address> addresses = createAddresses(3);
		List<String> ownerRole = Arrays.asList("OWNER");
		User owner1 = createUser("npeart", "Neal", "Peart", null, null, addresses.get(0), ownerRole, null);
		User associate = createUser("nwilson", "Nancy", "Wilson", "101", "oem001", addresses.get(1), Arrays.asList(User.Role.ASSOCIATE.toString()), null);
		User tech = createUser("awilson", "Ann", "Wilson", "101", "oem001", addresses.get(2), Arrays.asList(User.Role.TECHNICIAN.toString()), null);
		Spa myNewSpa = createSmallSpaWithState("160104", "Shark", "Tiger", "oem001", "101", null);

		Recipe recipe = createSpaRecipe(myNewSpa.get_id(), "TGIF", "Some like it hot!");

		this.mockMvc
				.perform(post("/spas/" + myNewSpa.get_id()
						+ "/recipes/" + recipe.get_id() + "/run"))
				.andExpect(status().is2xxSuccessful())
				.andDo(document("recipe-run-example"));
	}

	private Spa createSpa(HashMap<String,Object> attributes) throws Exception {
		Spa spa = new Spa();
		attributes.forEach((k,v) -> {
			Class c = v.getClass();
			System.out.println(c.toGenericString());
			try {
				Field f = c.getField(k);
				f.set(spa, v);
				System.out.println("Setting field:"+k+" to "+v);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		spaRepository.save(spa);
		return spa;
	}


}
