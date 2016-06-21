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
import com.bwg.iot.model.Attachment;
import com.bwg.iot.model.Oem;
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
public final class OemDocumentation extends ModelTestBase{

	@Rule
	public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private OemRepository oemRepository;

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
	public void oemListExample() throws Exception {
		this.oemRepository.deleteAll();


		this.mockMvc.perform(get("/oems")).andExpect(status().isOk())
				.andDo(document("oems-list-example",
						responseFields(
								fieldWithPath("_embedded.oems")
										.description("An array of <<resources-oem, Oem resources>>"),
						fieldWithPath("_links").description("<<resources-oemslist-links,Links>> to other resources"),
						fieldWithPath("page").description("Page information"))));
	}

	@Test
	public void oemCreateExample() throws Exception {
		this.oemRepository.deleteAll();
		this.addressRepository.deleteAll();

		Address address = createAddress();

		final Map<String, Object> oem = new HashMap<>();
		oem.put("name", "South Coast Spas");
		oem.put("customerNumber", 15215);
		oem.put("address", address);
		oem.put("email", "matteo@sardinaspas.com");
		oem.put("phone", "800-BUBBLES");

		this.mockMvc
				.perform(post("/oems").contentType(MediaTypes.HAL_JSON)
						.content(this.objectMapper.writeValueAsString(oem)))
				.andExpect(status().isCreated())
				.andDo(document("oem-create-example",
						requestFields(fieldWithPath("name").description("Name of the oem"),
//								fieldWithPath("id").description("Unique identifier for the oem"),
								fieldWithPath("customerNumber").description("Balboa identifier for the oem"),
								fieldWithPath("address").description("The address of the oem"),
								fieldWithPath("email").description("The Manufacturer's email address").optional().type("String"),
								fieldWithPath("phone").description("Phone number for the Manufacturer").optional().type("String"),
								fieldWithPath("logo").description("Logo image file for the Manufacturer").optional().type(Attachment.class))));
	}

	@Test
	public void oemUpdateExample() throws Exception {
		this.oemRepository.deleteAll();
        this.addressRepository.deleteAll();

        Attachment logo = createLogoAttachment(mockMvc);
        Address address = createAddress();
		Oem oem = createOem("Backyard Beach", 201450, address, null);

        oem.setName("Spalicious");
        oem.setLogo(logo);
		this.mockMvc
				.perform(patch("/oems/{0}", oem.get_id()).contentType(MediaTypes.HAL_JSON)
						.content(this.objectMapper.writeValueAsString(oem)))
				.andExpect(status().is2xxSuccessful())
				.andDo(document("oem-update-example",
						requestFields(fieldWithPath("name").description("Business name of the oem"),
								fieldWithPath("_id").description("Unique identifier for the oem"),
                                fieldWithPath("customerNumber").description("Balboa identifier for the oem"),
                                fieldWithPath("address").description("The address of the oem"),
                                fieldWithPath("email").description("The Manufacturer's email address").optional().type("String"),
                                fieldWithPath("phone").description("Phone number for the Manufacturer").optional().type("String"),
                                fieldWithPath("logo").description("Logo image file for the Manufacturer").optional().type(Attachment.class))));
	}

	@Test
	public void oemGetExample() throws Exception {
        this.oemRepository.deleteAll();
        this.addressRepository.deleteAll();

        Address address = createAddress();
        Oem oem = createOem("Backyard Beach", 201450, address, null);


        this.mockMvc.perform(get("/oems/{0}", oem.get_id())).andExpect(status().isOk())
				.andExpect(jsonPath("name", is(oem.getName())))
//				.andExpect(jsonPath("address", is(oem.getAddress())))
				.andDo(document("oem-get-example",
						links(linkWithRel("self").description("This <<resources-oem,oem>>"),
								linkWithRel("oem").description("This <<resources-oem,oem>>")),
						responseFields(
								fieldWithPath("_id").description("Object Id"),
								fieldWithPath("name").description("The name of the Manufacturer"),
								fieldWithPath("customerNumber").description("Balboa identifier for the Manufacturer"),
								fieldWithPath("address").description("The address of the Manufacturer"),
								fieldWithPath("email").description("The Manufacturer's email address").optional().type("String"),
								fieldWithPath("phone").description("Phone number for the Manufacturer").optional().type("String"),
								fieldWithPath("logo").description("Logo image file for the Manufacturer").optional().type(Attachment.class),
                                fieldWithPath("_links")
										.description("<<resources-oem-links,Links>> to other resources"))));
	}
}
