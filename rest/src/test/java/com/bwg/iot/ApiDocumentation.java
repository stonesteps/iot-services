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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
							linkWithRel("profile").description("The ALPS profile for the service")),
					responseFields(
							fieldWithPath("_links").description("<<resources-index-links,Links>> to other resources"))));

	}

	@Test
	public void spasListExample() throws Exception {
		this.spaRepository.deleteAll();

		User owner = createUser("eblues", "Elwood", "Blues", null, null, createAddress(), Arrays.asList("OWNER"), LocalDateTime.now().toString());
		createUnsoldSpa("01924094", "Shark", "Mako", "101");
        createUnsoldSpa("01000000", "Shark", "Hammerhead", "101");
		createFullSpaWithState("0blah345", "Shark", "Land", "101", owner);

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

		User owner = createUser("eblues", "Elwood", "Blues", null, null, createAddress(), Arrays.asList("OWNER"), LocalDateTime.now().toString());
        Spa spa = createFullSpaWithState("0blah345", "Shark", "Blue", "101", owner);

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
							linkWithRel("owner").description("This <<resources-user,user>>")),
					responseFields(
							fieldWithPath("_id").description("Object Id"),
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

		createUnsoldSpa("01924094", "Shark", "Mako", "101");
		createUnsoldSpa("01000000", "Shark", "Hammerhead", "101");
		createUnsoldSpa("013t43tt", "Shark", "Nurse", "101");
		createUnsoldSpa("0blah345", "Shark", "Land", "101");

		this.mockMvc.perform(get("/spas/search/findByDealerId?dealerId=101"))
				.andExpect(status().isOk())
				.andDo(document("spas-findbyDealer-example",
						responseFields(
								fieldWithPath("_embedded.spas").description("An array of <<resources-spa, Spa resources>>"),
								fieldWithPath("_links").description("<<resources-spaslist-links,Links>> to other resources"),
								fieldWithPath("page").description("Page information"))));
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
