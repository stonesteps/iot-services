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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;

import com.bwg.iot.model.*;
import org.hamcrest.Matcher;
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
public class ApiDocumentation {
	
	@Rule
	public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

	@Autowired
	private SpaRepository spaRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private AddressRepository addressRepository;

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
							linkWithRel("owners").description("The <<resources-owners,Owners resource>>"),
                            linkWithRel("addresses").description("The <<resources-addresses,Addresses resource>>"),
                            linkWithRel("alerts").description("The <<resources-alerts,Alert resource>>"),
                            linkWithRel("spaCommands").description("The <<resources-spaCommands,SpaCommand resource>>"),
							linkWithRel("dealers").description("The <<resources-dealers,Dealers resource>>"),
							linkWithRel("oems").description("The <<resources-oems,Oem resource>>"),
							linkWithRel("users").description("The <<resources-users,User resource>>"),
							linkWithRel("profile").description("The ALPS profile for the service")),
					responseFields(
							fieldWithPath("_links").description("<<resources-index-links,Links>> to other resources"))));

	}

	@Test
	public void spasListExample() throws Exception {
		this.spaRepository.deleteAll();

		Owner owner = createOwner("Elwood", "Elwood", "Blues");
		createSpa("01924094", "Shark", "Mako", "101");
        createSpa("01000000", "Shark", "Hammerhead", "101");
		createSpaWithState("0blah345", "Shark", "Land", "101", owner);

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
		Map<String, String> owner = new HashMap<String, String>();
		owner.put("customerName", "Mr. Blues");
        owner.put("firstName", "Elwood");
        owner.put("lastName", "Blues");

		String ownerLocation = this.mockMvc
				.perform(
						post("/owners").contentType(MediaTypes.HAL_JSON).content(
								this.objectMapper.writeValueAsString(owner)))
				.andExpect(status().isCreated()).andReturn().getResponse()
				.getHeader("Location");

		Map<String, Object> spa = new HashMap<String, Object>();
		spa.put("serialNumber", "2000");
        spa.put("productName", "Shark");
        spa.put("model", "Sand");
        spa.put("dealerId", "101");
//        spa.put("owner", ownerLocation);
//		spa.put("alerts", Arrays.asList(tagLocation));

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
//									fieldWithPath("tags").description("An array of tag resource URIs"))));
	}

	@Test
	public void spaGetExample() throws Exception {

        Owner owner = createOwner("Blue Louis", "Lou", "Maroni");
        Spa spa = createSpaWithState("0blah345", "Shark", "Blue", "101", owner);


		String spaLocation = "/spas/"+spa.getId();

		this.mockMvc.perform(get(spaLocation))
			.andExpect(status().isOk())
			.andExpect(jsonPath("serialNumber", is(spa.getSerialNumber())))
			.andExpect(jsonPath("productName", is(spa.getProductName())))
            .andExpect(jsonPath("model", is(spa.getModel())))
            .andExpect(jsonPath("dealerId", is(spa.getDealerId())))
			.andExpect(jsonPath("_links.self.href", containsString(spaLocation)))
            .andExpect(jsonPath("_links.spa.href", containsString(spaLocation)))
//			.andExpect(jsonPath("_links.tags", is(notNullValue())))
			.andDo(document("spa-get-example",
					links(
							linkWithRel("self").description("This <<resources-spa,spa>>"),
                            linkWithRel("spa").description("This <<resources-spa,spa>>")),
//							linkWithRel("tags").description("This spa's tags")),
					responseFields(
                            fieldWithPath("serialNumber").description("The serial of the spa"),
                            fieldWithPath("productName").description("The product name of the spa"),
                            fieldWithPath("model").description("The spa model"),
                            fieldWithPath("owner").description("The owner of the spa"),
							fieldWithPath("sold").description("Flag denoting if spa has been sold"),
                            fieldWithPath("alerts").description("Current Issues with the spa"),
                            fieldWithPath("dealerId").description("The dealer assigned to the spa"),
                            fieldWithPath("oemId").description("The manufacturer that built spa"),
                            fieldWithPath("currentState").description("Latest readings from the spa"),
                            fieldWithPath("manufacturedDate").description("The date the spa was made"),
                            fieldWithPath("registrationDate").description("The date the spa was sold"),
                            fieldWithPath("p2pAPSSID").description("Wifi address"),
                            fieldWithPath("p2pAPPassword").description("Wifi password"),
							fieldWithPath("_links").description("<<resources-spa-links,Links>> to other resources"))));
	}

	@Test
	public void spasFindByDealerExample() throws Exception {
		this.spaRepository.deleteAll();

		createSpa("01924094", "Shark", "Mako", "101");
		createSpa("01000000", "Shark", "Hammerhead", "101");
		createSpa("013t43tt", "Shark", "Nurse", "101");
		createSpa("0blah345", "Shark", "Land", "101");

		this.mockMvc.perform(get("/spas/search/findByDealerId?dealerId=101"))
				.andExpect(status().isOk())
				.andDo(document("spas-findbyDealer-example",
						responseFields(
								fieldWithPath("_embedded.spas").description("An array of <<resources-spa, Spa resources>>"),
								fieldWithPath("_links").description("<<resources-spaslist-links,Links>> to other resources"),
								fieldWithPath("page").description("Page information"))));
	}

//	@Test
//	public void tagsListExample() throws Exception {
//		this.spaRepository.deleteAll();
//		this.tagRepository.deleteAll();
//
//		createTag("REST");
//		createTag("Hypermedia");
//		createTag("HTTP");
//
//		this.mockMvc.perform(get("/tags"))
//			.andExpect(status().isOk())
//			.andDo(document("tags-list-example",
//					responseFields(
//							fieldWithPath("_embedded.tags").description("An array of <<resources-tag,Tag resources>>"))));
//	}
//
//	@Test
//	public void tagsCreateExample() throws Exception {
//		Map<String, String> tag = new HashMap<String, String>();
//		tag.put("name", "REST");
//
//		this.mockMvc.perform(
//				post("/tags").contentType(MediaTypes.HAL_JSON).content(
//						this.objectMapper.writeValueAsString(tag)))
//				.andExpect(status().isCreated())
//				.andDo(document("tags-create-example",
//						requestFields(
//								fieldWithPath("name").description("The name of the tag"))));
//	}

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
//				.andExpect(jsonPath("_links.tags", is(notNullValue())));

//		Map<String, String> tag = new HashMap<String, String>();
//		tag.put("name", "REST");
//
//		String tagLocation = this.mockMvc
//				.perform(
//						post("/tags").contentType(MediaTypes.HAL_JSON).content(
//								this.objectMapper.writeValueAsString(tag)))
//				.andExpect(status().isCreated()).andReturn().getResponse()
//				.getHeader("Location");

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
//								fieldWithPath("tags").description("An array of tag resource URIs").optional())));
	}

//	@Test
//	public void tagGetExample() throws Exception {
//		Map<String, String> tag = new HashMap<String, String>();
//		tag.put("name", "REST");
//
//		String tagLocation = this.mockMvc
//				.perform(
//						post("/tags").contentType(MediaTypes.HAL_JSON).content(
//								this.objectMapper.writeValueAsString(tag)))
//				.andExpect(status().isCreated()).andReturn().getResponse()
//				.getHeader("Location");
//
//		this.mockMvc.perform(get(tagLocation))
//			.andExpect(status().isOk())
//			.andExpect(jsonPath("name", is(tag.get("name"))))
//			.andDo(document("tag-get-example",
//					links(
//							linkWithRel("self").description("This <<resources-tag,tag>>"),
//							linkWithRel("spas").description("The <<resources-tagged-spas,spas>> that have this tag")),
//					responseFields(
//							fieldWithPath("name").description("The name of the tag"),
//							fieldWithPath("_links").description("<<resources-tag-links,Links>> to other resources"))));
//	}
//
//	@Test
//	public void tagUpdateExample() throws Exception {
//		Map<String, String> tag = new HashMap<String, String>();
//		tag.put("name", "REST");
//
//		String tagLocation = this.mockMvc
//				.perform(
//						post("/tags").contentType(MediaTypes.HAL_JSON).content(
//								this.objectMapper.writeValueAsString(tag)))
//				.andExpect(status().isCreated()).andReturn().getResponse()
//				.getHeader("Location");
//
//		Map<String, Object> tagUpdate = new HashMap<String, Object>();
//		tagUpdate.put("name", "RESTful");
//
//		this.mockMvc.perform(
//				patch(tagLocation).contentType(MediaTypes.HAL_JSON).content(
//						this.objectMapper.writeValueAsString(tagUpdate)))
//				.andExpect(status().isNoContent())
//				.andDo(document("tag-update-example",
//						requestFields(
//								fieldWithPath("name").description("The name of the tag"))));
//	}

	private void createSpa(String serialNumber, String productName, String model, String dealerId) {
		Spa spa = new Spa();
        spa.setSerialNumber(serialNumber);
        spa.setProductName(productName);
        spa.setModel(model);
        spa.setDealerId(dealerId);

		this.spaRepository.save(spa);
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

	private Spa createSpaWithState(String serialNumber, String productName, String model, String dealerId, Owner owner) {
		SpaState spaState = new SpaState();
		spaState.setRunMode("Ready");
		spaState.setCurrentTemp("99");
		spaState.setDesiredTemp("102");
		spaState.setAlert("green");
		spaState.setPump1("on");
        spaState.setPump2("off");
        spaState.setLights1("off");
        spaState.setLights2("off");
        spaState.setFilter1("ok");
        spaState.setFilter2("ok");
        spaState.setAux1("on");
        spaState.setMicrosilk("ok");
        spaState.setPanelLock("off");
        spaState.setOzone("ok");
        spaState.setUplinkTimestamp(LocalDateTime.now().toString());


        Spa spa = new Spa();
		spa.setSerialNumber(serialNumber);
		spa.setProductName(productName);
		spa.setModel(model);
		spa.setDealerId(dealerId);
		spa.setOwner(owner);
		spa.setCurrentState(spaState);
        spa.setOemId("cab335");
        spa.setManufacturedDate(LocalDate.now().toString());
        spa.setRegistrationDate(LocalDate.now().toString());
        spa.setP2pAPSSID("myWifi");
        spa.setP2pAPPassword("*******");

		spa = this.spaRepository.save(spa);

		Alert alert1 = new Alert();
		alert1.setName("Replace Filter");
		alert1.setLongDescription("The filter is old and needs to be replaced");
		alert1.setSeverityLevel("yellow");
		alert1.setComponent("filter1");
		alert1.setShortDescription("Replace Filter");
		alert1.setCreationDate(LocalDateTime.now().toString());
		alert1.setSpaId(spa.getId());
		alertRepository.save(alert1);

		spa.setAlerts(Arrays.asList(alert1));
		spa = this.spaRepository.save(spa);
		return spa;
	}

	private Owner createOwner(String customerName, String firstName, String lastName) {
        Address address = new Address();
        address.setAddress1("1060 W Addison St");
        address.setAddress2("Apt. C");
        address.setCity("Chicago");
        address.setState("IL");
        address.setZip("60613");
        address.setCountry("US");
        address.setEmail("lou@blues.org");
        address.setPhone("800-123-4567");
        addressRepository.save(address);

        Owner owner = new Owner();
		owner.setCustomerName(customerName);
		owner.setLastName(lastName);
		owner.setFirstName(firstName);
        owner.setAddress(address);
		this.ownerRepository.save(owner);
		return owner;
	}
}
