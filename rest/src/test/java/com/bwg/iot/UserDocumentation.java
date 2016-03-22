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
import com.bwg.iot.model.User;
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
public final class UserDocumentation extends ModelTestBase {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Autowired
    private UserRepository userRepository;

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
    public void userListExample() throws Exception {
        this.userRepository.deleteAll();

        this.mockMvc.perform(get("/users")).andExpect(status().isOk())
                .andDo(document("users-list-example",
                        responseFields(
                                fieldWithPath("_embedded.users")
                                        .description("An array of <<resources-user, User resources>>"),
                                fieldWithPath("_links").description("<<resources-userslist-links,Links>> to other resources"),
                                fieldWithPath("page").description("Page information"))));
    }

    @Test
    public void userCreateExample() throws Exception {
        this.userRepository.deleteAll();
        this.addressRepository.deleteAll();

        Address address = createAddress();

        final Map<String, Object> user = new HashMap<>();
        user.put("firstName", "Mo");
        user.put("lastName", "Eddy");
        user.put("dealerId", "111");
        user.put("oemId", "222");
        user.put("address", address);
        user.put("createdDate", new Date());
        user.put("roles", Arrays.asList("USER"));

        this.mockMvc
                .perform(post("/users").contentType(MediaTypes.HAL_JSON)
                        .content(this.objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andDo(document("user-create-example",
                        requestFields(fieldWithPath("firstName").description("First name of the user"),
                                fieldWithPath("lastName").description("Last name of the user"),
                                fieldWithPath("dealerId").description("Dealer id"),
                                fieldWithPath("oemId").description("Oem id"),
                                fieldWithPath("address").description("The address of the dealer"),
                                fieldWithPath("createdDate").description("Created date"),
                                fieldWithPath("roles").description("User roles"))));
    }

    @Test
    public void userUpdateExample() throws Exception {
        this.userRepository.deleteAll();
        this.addressRepository.deleteAll();

        Address address = createAddress();
        User user = createUser("meddy", "Mo", "Eddy", "111", "222", address, Arrays.asList("USER"));

        final Map<String, Object> userUpdate = new HashMap<>();
        userUpdate.put("firstName", "Moebius");
        userUpdate.put("lastName", "Eddy");
        userUpdate.put("dealerId", "111");
        userUpdate.put("oemId", "222");
        userUpdate.put("address", address);
        userUpdate.put("modifiedDate", new Date());
        userUpdate.put("roles", Arrays.asList("USER"));

        this.mockMvc
                .perform(patch("/users/{0}", user.get_id()).contentType(MediaTypes.HAL_JSON)
                        .content(this.objectMapper.writeValueAsString(userUpdate)))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("user-update-example",
                        requestFields(fieldWithPath("firstName").description("First name of the user"),
                                fieldWithPath("lastName").description("Last name of the user"),
                                fieldWithPath("dealerId").description("Dealer id"),
                                fieldWithPath("oemId").description("Oem id"),
                                fieldWithPath("address").description("Address"),
                                fieldWithPath("modifiedDate").description("Modified date"),
                                fieldWithPath("roles").description("User roles"))));
    }

    @Test
    public void userGetExample() throws Exception {
        this.userRepository.deleteAll();
        this.addressRepository.deleteAll();

        Address address = createAddress();
        User user = createUser("meddy", "Mo", "Eddy", "111", "222", address, Arrays.asList("USER"));

        this.mockMvc.perform(get("/users/{0}", user.get_id())).andExpect(status().isOk())
                .andExpect(jsonPath("firstName", is(user.getFirstName())))
                .andDo(document("user-get-example",
                        links(linkWithRel("self").description("This <<resources-user,user>>"),
                                linkWithRel("user").description("This <<resources-user,user>>")),
                        responseFields(
                                fieldWithPath("_id").description("Object Id"),
                                fieldWithPath("username").description("Unique string for the user"),
                                fieldWithPath("firstName").description("First name of the user"),
                                fieldWithPath("lastName").description("Last name of the user"),
                                fieldWithPath("dealerId").description("dealer id"),
                                fieldWithPath("oemId").description("Oem id"),
                                fieldWithPath("roles").description("Roles of this user"),
                                fieldWithPath("address").description("User's address"),
                                fieldWithPath("createdDate").description("User creation date"),
                                fieldWithPath("modifiedDate").description("Date of update").optional().type("String"),
                                fieldWithPath("_links")
                                        .description("<<resources-user-links,Links>> to other resources"))));
    }

    @Test
    public void userFindByUsername() throws Exception {
        this.userRepository.deleteAll();
        this.addressRepository.deleteAll();

        Address address = createAddress();
        User user = createUser("veddy", "Eddie", "Vedder", "111", "222", address, Arrays.asList("OWNER"));

        this.mockMvc.perform(get("/users/search/findByUsername?username={0}", user.getUsername())).andExpect(status().isOk())
                .andExpect(jsonPath("firstName", is(user.getFirstName())))
                .andDo(document("user-findByUsername-example",
                        links(linkWithRel("self").description("This <<resources-user,user>>"),
                                linkWithRel("user").description("This <<resources-user,user>>")),
                        responseFields(
                                fieldWithPath("_id").description("Object Id"),
                                fieldWithPath("username").description("Unique string for the user"),
                                fieldWithPath("firstName").description("First name of the user"),
                                fieldWithPath("lastName").description("Last name of the user"),
                                fieldWithPath("dealerId").description("Dealer id"),
                                fieldWithPath("oemId").description("Oem id"),
                                fieldWithPath("roles").description("Roles of this user"),
                                fieldWithPath("address").description("User's address"),
                                fieldWithPath("createdDate").description("User creation date"),
                                fieldWithPath("modifiedDate").description("Date of update").optional().type("String"),
                                fieldWithPath("_links")
                                        .description("<<resources-user-links,Links>> to other resources"))));
    }
}
