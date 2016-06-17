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
import com.bwg.iot.model.Dealer;
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
public final class DealerDocumentation extends ModelTestBase{

	@Rule
	public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private DealerRepository dealerRepository;

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
	public void dealerListExample() throws Exception {
		this.dealerRepository.deleteAll();


		this.mockMvc.perform(get("/dealers")).andExpect(status().isOk())
				.andDo(document("dealers-list-example",
						responseFields(
								fieldWithPath("_embedded.dealers")
										.description("An array of <<resources-dealer, Dealer resources>>"),
						fieldWithPath("_links").description("<<resources-dealerslist-links,Links>> to other resources"),
						fieldWithPath("page").description("Page information"))));
	}

	@Test
	public void dealerCreateExample() throws Exception {
		this.dealerRepository.deleteAll();
		this.addressRepository.deleteAll();

		Address address = createAddress();

		final Map<String, Object> dealer = new HashMap<>();
		dealer.put("name", "South Coast Spas");
		dealer.put("address", address);
		dealer.put("email", "service@riot.com");
		dealer.put("phone", "800-iot-spas");

		this.mockMvc
				.perform(post("/dealers").contentType(MediaTypes.HAL_JSON)
						.content(this.objectMapper.writeValueAsString(dealer)))
				.andExpect(status().isCreated())
				.andDo(document("dealer-create-example",
						requestFields(fieldWithPath("name").description("Name of the dealer"),
//							fieldWithPath("id").description("Unique identifier for the dealer"),
                            fieldWithPath("address").description("The address of the dealer"),
	            			fieldWithPath("email").description("The dealer's email address").optional().type("String"),
	            			fieldWithPath("phone").description("Phone number for the dealership").optional().type("String"),
	            			fieldWithPath("logo").description("Logo image file for the Dealer").optional().type(Attachment.class))));
	}

	@Test
	public void dealerUpdateExample() throws Exception {
		this.dealerRepository.deleteAll();
        this.addressRepository.deleteAll();

        Attachment logo = createLogoAttachment(mockMvc);
        Address address = createAddress();
		Dealer dealer = createDealer("Backyard Beach", address, null);

        dealer.setName("Spalicious");
        dealer.setLogo(logo);

		this.mockMvc
				.perform(patch("/dealers/{0}", dealer.get_id()).contentType(MediaTypes.HAL_JSON)
						.content(this.objectMapper.writeValueAsString(dealer)))
				.andExpect(status().is2xxSuccessful())
				.andDo(document("dealer-update-example",
						requestFields(fieldWithPath("name").description("Business name of the dealer"),
							fieldWithPath("_id").description("Unique identifier for the dealer"),
                fieldWithPath("address").description("The address of the dealer"),
                fieldWithPath("email").description("The dealer's email address").optional().type("String"),
                fieldWithPath("phone").description("Phone number for the dealership").optional().type("String"),
                fieldWithPath("logo").description("Logo image file for the Dealer").optional().type(Attachment.class))));
	}

	@Test
	public void dealerGetExample() throws Exception {
        this.dealerRepository.deleteAll();
        this.addressRepository.deleteAll();

        Address address = createAddress();
        Dealer dealer = createDealer("Backyard Beach", address, "oem00001");


        this.mockMvc.perform(get("/dealers/{0}", dealer.get_id())).andExpect(status().isOk())
				.andExpect(jsonPath("name", is(dealer.getName())))
				.andExpect(jsonPath("oemId", is(dealer.getOemId())))
//				.andExpect(jsonPath("address", is(dealer.getAddress())))
				.andDo(document("dealer-get-example",
						links(linkWithRel("self").description("This <<resources-dealer,dealer>>"),
								linkWithRel("dealer").description("This <<resources-dealer,dealer>>")),
						responseFields(
								fieldWithPath("_id").description("Object Id"),
								fieldWithPath("name").description("The name of the dealer"),
								fieldWithPath("address").description("Contact info for the dealer"),
								fieldWithPath("oemId").description("Manufacturer"),
                                fieldWithPath("email").description("The dealer's email address").optional().type("String"),
                                fieldWithPath("phone").description("Phone number for the dealership").optional().type("String"),
                                fieldWithPath("logo").description("Logo image file for the Dealer").optional().type(Attachment.class),
                                fieldWithPath("_links")
										.description("<<resources-dealer-links,Links>> to other resources"))));
	}
}
