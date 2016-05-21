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
public final class ComponentDocumentation extends ModelTestBase{

	@Rule
	public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

	@Autowired
	private ComponentRepository componentRepository;

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
	public void componentListExample() throws Exception {
		this.componentRepository.deleteAll();


		this.mockMvc.perform(get("/components")).andExpect(status().isOk())
				.andDo(document("components-list-example",
						responseFields(
								fieldWithPath("_embedded.components").description("An array of <<resources-component, Component resources>>"),
								fieldWithPath("_links").description("<<resources-componentslist-links,Links>> to other resources"),
								fieldWithPath("page").description("Page information"))));
	}

	@Test
	public void componentCreateExample() throws Exception {
		this.componentRepository.deleteAll();

		Address address = createAddress();

		final Map<String, Object> component = new HashMap<>();
		component.put("spaId", "spa001");
		component.put("oemId", "oem001");
		component.put("dealerId", "dealer001");
		component.put("ownerId", "owner001");
		component.put("componentType", Component.ComponentType.LIGHT);
		component.put("serialNumber", "1503071080");
		component.put("port", "0");
		component.put("name", "Main Light");

		this.mockMvc
				.perform(post("/components").contentType(MediaTypes.HAL_JSON)
						.content(this.objectMapper.writeValueAsString(component)))
				.andExpect(status().isCreated())
				.andDo(document("component-create-example",
						requestFields(fieldWithPath("name").description("Friendly name of the component"),
								fieldWithPath("serialNumber").description("Component Serial Number"),
								fieldWithPath("port").description("Spa port component is attached."),
                                fieldWithPath("componentType").description("The type of component"),
								fieldWithPath("oemId").description("Id of the spa manufacturer"),
								fieldWithPath("dealerId").description("Id of the spa dealer"),
								fieldWithPath("spaId").description("Id of the spa"),
								fieldWithPath("ownerId").description("Owner of the spa"))));
	}

	@Test
	public void componentUpdateExample() throws Exception {
		this.componentRepository.deleteAll();

		Component pump1 = createComponent(Component.ComponentType.PUMP.name(), "0", "Jets", "1502119991", "spa0001");

		final Map<String, String> componentUpdate = new HashMap<>();
		componentUpdate.put("name", "Captain's Chair");

		this.mockMvc
				.perform(patch("/components/{0}", pump1.get_id()).contentType(MediaTypes.HAL_JSON)
						.content(this.objectMapper.writeValueAsString(componentUpdate)))
				.andExpect(status().is2xxSuccessful())
				.andDo(document("component-update-example",
						requestFields(fieldWithPath("name").description("Friendly Name of the component"))));
	}

	@Test
	public void componentGetExample() throws Exception {
		this.componentRepository.deleteAll();

		Component pump1 = createComponent(Component.ComponentType.PUMP.name(), "0", "Jets", "1502119991", "spa0001");

        this.mockMvc.perform(get("/components/{0}", pump1.get_id())).andExpect(status().isOk())
				.andExpect(jsonPath("name", is(pump1.getName())))
				.andDo(document("component-get-example",
						links(linkWithRel("self").description("This <<resources-component, component>>"),
								linkWithRel("component").description("This <<resources-component, component>>")),
						responseFields(
//								fieldWithPath("_id").description("Object Id"),
								fieldWithPath("name").description("Friendly name of the component"),
                                fieldWithPath("serialNumber").description("Component Serial Number"),
                                fieldWithPath("port").description("Spa port component is attached."),
                                fieldWithPath("componentType").description("The type of component"),
                                fieldWithPath("oemId").description("Id of the spa manufacturer").optional().type(String.class),
                                fieldWithPath("dealerId").description("Id of the spa dealer").optional().type(String.class),
                                fieldWithPath("spaId").description("Id of the spa").optional().type(String.class),
                                fieldWithPath("ownerId").description("Owner of the spa").optional().type(String.class),
								fieldWithPath("factoryInit").description("Boolean flag identifying components created during factory test").type(Boolean.class),
                                fieldWithPath("_links")
										.description("<<resources-oem-links,Links>> to other resources"))));
	}

    @Test
    public void findBySerialNumberExample() throws Exception {
        this.componentRepository.deleteAll();

        Component pump1 = createComponent(Component.ComponentType.PUMP.name(), "0", "Jets", "150211", "spa0001");

        this.mockMvc.perform(get("/components/search/findBySerialNumber?serialNumber=" + pump1.getSerialNumber()))
                .andExpect(status().isOk())
                .andDo(document("components-findbySerialNumber-example",
                        responseFields(
//                                fieldWithPath("_id").description("Object Id"),
                                fieldWithPath("name").description("Friendly name of the component"),
                                fieldWithPath("serialNumber").description("Component Serial Number"),
                                fieldWithPath("port").description("Spa port component is attached."),
                                fieldWithPath("componentType").description("The type of component"),
                                fieldWithPath("oemId").description("Id of the spa manufacturer").optional().type(String.class),
                                fieldWithPath("dealerId").description("Id of the spa dealer").optional().type(String.class),
                                fieldWithPath("spaId").description("Id of the spa").optional().type(String.class),
                                fieldWithPath("ownerId").description("Owner of the spa").optional().type(String.class),
								fieldWithPath("factoryInit").description("Boolean flag identifying components created during factory test").type(Boolean.class),
                                fieldWithPath("_links")
                                        .description("<<resources-oem-links,Links>> to other resources"))));
    }


	@Test
	public void findBySpaIdExample() throws Exception {
		clearAllData();

		List<String> ownerRole = Arrays.asList("OWNER");
		List<Address> addresses = createAddresses(20);
		Oem oem2 = createOem("Rockers Ltd.", 1001001, addresses.get(0), "oem002");
		Dealer dealer2 = createDealer("Pt. Loma Spa Outlet", addresses.get(1), oem2.get_id(), "dealer002");
		User owner4 = createUser("lgaga", "Lady", "Gaga", dealer2.get_id(), oem2.get_id(), addresses.get(3), ownerRole, null);
		Spa spa26 = createSmallSpaWithState("160229", "Shark", "Tiger", oem2.get_id(), dealer2.get_id(), owner4);

		this.mockMvc.perform(get("/components/search/findBySpaIdOrderByComponentType?spaId=" + spa26.get_id()))
				.andExpect(status().isOk())
				.andDo(document("components-findbySpaId-example",
						responseFields(
								fieldWithPath("_embedded.components").description("An array of <<resources-component, Component resources>>"),
								fieldWithPath("_links").description("<<resources-componentslist-links,Links>> to other resources"),
								fieldWithPath("page").description("Page information"))));
	}

	@Test
	public void findBySpaIdAndComponentTypeExample() throws Exception {
		clearAllData();

		List<String> ownerRole = Arrays.asList("OWNER");
		List<Address> addresses = createAddresses(20);
		Oem oem2 = createOem("Rockers Ltd.", 1001001, addresses.get(0), "oem002");
		Dealer dealer2 = createDealer("Pt. Loma Spa Outlet", addresses.get(1), oem2.get_id(), "dealer002");
		User owner4 = createUser("lgaga", "Lady", "Gaga", dealer2.get_id(), oem2.get_id(), addresses.get(3), ownerRole, null);
		Spa spa26 = createSmallSpaWithState("160229", "Shark", "Tiger", oem2.get_id(), dealer2.get_id(), owner4);

		this.mockMvc.perform(get("/components/search/findBySpaIdAndComponentTypeOrderByPortAsc?spaId=" + spa26.get_id() + "&componentType=PUMP"))
				.andExpect(status().isOk())
				.andDo(document("components-findbySpaIdAndComponentType-example",
						responseFields(
								fieldWithPath("_embedded.components").description("An array of <<resources-component, Component resources>>"),
								fieldWithPath("_links").description("<<resources-componentslist-links,Links>> to other resources"),
								fieldWithPath("page").description("Page information"))));
	}
}
