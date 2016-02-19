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

import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

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

import com.bwg.iot.model.Address;
import com.bwg.iot.model.Owner;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IotServicesApplication.class)
@WebAppConfiguration
public final class OwnersDocumentation {

	@Rule
	public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

	@Autowired
	private OwnerRepository ownerRepository;

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
	public void ownersListExample() throws Exception {
		this.ownerRepository.deleteAll();

		createOwner("Joliet Jake", "Jake", "Blues");

		this.mockMvc.perform(get("/owners")).andExpect(status().isOk())
				.andDo(document("owners-list-example",
						responseFields(
								fieldWithPath("_embedded.owners")
										.description("An array of <<resources-owner, Owner resources>>"),
						fieldWithPath("_links").description("<<resources-ownerslist-links,Links>> to other resources"),
						fieldWithPath("page").description("Page information"))));
	}

	@Test
	public void ownerCreateExample() throws Exception {
		this.ownerRepository.deleteAll();

		final Map<String, String> address = new HashMap<>();
		address.put("address1", "Line 1");
		address.put("address2", "Line 2");
		address.put("city", "city");
		address.put("state", "state");
		address.put("country", "country");
		address.put("zip", "zip");
		address.put("phone", "phone");
		address.put("email", "email");

		final Map<String, Object> owner = new HashMap<>();
		owner.put("customerName", "Mr. Blues");
		owner.put("firstName", "Elwood");
		owner.put("lastName", "Blues");
		owner.put("address", address);

		this.mockMvc
				.perform(post("/owners").contentType(MediaTypes.HAL_JSON)
						.content(this.objectMapper.writeValueAsString(owner)))
				.andExpect(status().isCreated())
				.andDo(document("owner-create-example",
						requestFields(fieldWithPath("customerName").description("Customer name"),
								fieldWithPath("firstName").description("First name of the owner"),
								fieldWithPath("lastName").description("Last name of the owner"),
								fieldWithPath("address").description("Address of the owner"))));
	}

	@Test
	public void ownerUpdateExample() throws Exception {
		this.ownerRepository.deleteAll();

		final Owner owner = createOwner("Joliet Jake", "Jake", "Blues");

		final Map<String, String> address = new HashMap<>();
		address.put("address1", "Line 1");
		address.put("address2", "Line 2");
		address.put("city", "city");
		address.put("state", "state");
		address.put("country", "country");
		address.put("zip", "zip");
		address.put("phone", "phone");
		address.put("email", "email");

		final Map<String, Object> ownerUpdate = new HashMap<>();
		ownerUpdate.put("customerName", "Mr. Blues");
		ownerUpdate.put("firstName", "Mr. Blues");
		ownerUpdate.put("lastName", "Mr. Blues");
		ownerUpdate.put("address", address);

		this.mockMvc
				.perform(patch("/owners/{0}", owner.getId()).contentType(MediaTypes.HAL_JSON)
						.content(this.objectMapper.writeValueAsString(ownerUpdate)))
				.andExpect(status().is2xxSuccessful())
				.andDo(document("owner-update-example",
						requestFields(fieldWithPath("customerName").description("Customer name"),
								fieldWithPath("firstName").description("First name of the owner"),
								fieldWithPath("lastName").description("Last name of the owner"),
								fieldWithPath("address").description("Address of the owner"))));
	}

	@Test
	public void ownerGetExample() throws Exception {
		this.ownerRepository.deleteAll();

		final Owner owner = createOwner("Joliet Jake", "Jake", "Blues");

		this.mockMvc.perform(get("/owners/{0}", owner.getId())).andExpect(status().isOk())
				.andExpect(jsonPath("customerName", is(owner.getCustomerName())))
				.andExpect(jsonPath("firstName", is(owner.getFirstName())))
				.andExpect(jsonPath("lastName", is(owner.getLastName())))
				.andDo(document("owner-get-example",
						links(linkWithRel("self").description("This <<resources-owner,owner>>"),
								linkWithRel("owner").description("This <<resources-owner,owner>>")),
						responseFields(fieldWithPath("customerName").description("The customer name"),
								fieldWithPath("firstName").description("The owner's first name"),
								fieldWithPath("lastName").description("The owner's last name"),
								fieldWithPath("address").description("The owner's address"), fieldWithPath("_links")
										.description("<<resources-owner-links,Links>> to other resources"))));
	}

	private Owner createOwner(final String customerName, final String firstName, final String lastName) {
		final Owner owner = new Owner();
		owner.setCustomerName(customerName);
		owner.setLastName(lastName);
		owner.setFirstName(firstName);
		owner.setAddress(createAddress());
		return this.ownerRepository.save(owner);
	}

	private Address createAddress() {
		final Address address = new Address();
		address.setAddress1("5671");
		address.setAddress2("Honey Apple Crest");
		address.setCity("Village Five");
		address.setState("CA");
		address.setZip("E6L-4J4");
		address.setCountry("US");
		address.setEmail("gordon@gordon.com");
		address.setPhone("(506) 471-2382");
		return address;
	}
}
