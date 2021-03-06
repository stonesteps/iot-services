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
import com.bwg.iot.model.Dealer;
import com.bwg.iot.model.Oem;
import com.bwg.iot.model.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import gluu.scim.client.model.ScimPerson;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IotServicesApplication.class)
@WebAppConfiguration
public final class UserDocumentation extends ModelTestBase {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Autowired
    private UserRepository realUserRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private UserRegistrationHelper gluuHelper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EntityLinks entityLinks;

    @InjectMocks
    private UserRegistrationController userRegistrationController;

    @Autowired
    private EntityLinks realEntityLinks;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;
    private MockMvc userRegMockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.userRegMockMvc = MockMvcBuilders.standaloneSetup(userRegistrationController)
                .apply(documentationConfiguration(this.restDocumentation)).build();

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation)).build();
    }

    @Test
    public void userListExample() throws Exception {
        clearAllData();
        createVariousUsers();
        this.mockMvc.perform(get("/users")).andExpect(status().isOk())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page.totalElements", is(26)))
                .andExpect(jsonPath("page.totalPages", is(2)))
                .andDo(document("users-list-example",
                        responseFields(
                                fieldWithPath("_embedded.users")
                                        .description("An array of <<resources-user, User resources>>"),
                                fieldWithPath("_links").description("<<resources-userslist-links,Links>> to other resources"),
                                fieldWithPath("page").description("Page information"))));
    }

    @Test
    public void findUsersByDealerExample() throws Exception {
        clearAllData();
        createVariousUsers();

        this.mockMvc.perform(get("/users/search/findByDealerId?dealerId=dealer001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("page.totalElements", is(9)))
                .andDo(document("users-findbydealer-example",
                        requestParameters(parameterWithName("dealerId").description("The unique identifier for the dealer"))))
                .andDo(document("users-findbydealer-example",
                        responseFields(
                                fieldWithPath("_embedded.users").description("An array of <<resources-user, User resources>>"),
                                fieldWithPath("_links").description("<<resources-userlist-links,Links>> to other resources"),
                                fieldWithPath("page").description("Page information"))));
    }

    @Test
    public void findUsersByOemExample() throws Exception {
        clearAllData();
        createVariousUsers();

        this.mockMvc.perform(get("/users/search/findByOemId?oemId=oem001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("page.totalElements", is(13)))
                .andDo(document("users-findbyoem-example",
                        requestParameters(parameterWithName("oemId").description("The unique identifier for the spa manufacturer"))))
                .andDo(document("users-findbyoem-example",
                        responseFields(
                                fieldWithPath("_embedded.users").description("An array of <<resources-user, User resources>>"),
                                fieldWithPath("_links").description("<<resources-userlist-links,Links>> to other resources"),
                                fieldWithPath("page").description("Page information"))));

    }

    @Test
    public void userCreateExample() throws Throwable {
        this.realUserRepository.deleteAll();
        this.addressRepository.deleteAll();

        ScimPerson scimPerson = new ScimPerson();
        scimPerson.setUserName("glee");
        when(gluuHelper.createUser(any(User.class))).thenReturn(scimPerson);
        Address address = createAddress();



        final Map<String, Object> user = new HashMap<>();
        user.put("username", "glee");
        user.put("firstName", "Getty");
        user.put("lastName", "Lee");
        user.put("dealerId", "dealer111");
        user.put("oemId", "oem222");
        user.put("email", "glee@rush.net");
        user.put("phone", "(800)222-3456");
        user.put("address", address);
        user.put("createdDate", new Date());
        user.put("roles", Arrays.asList("OWNER"));
        user.put("notes", "Notes");

        this.userRegMockMvc
                .perform(post("/user-registration").contentType(MediaTypes.HAL_JSON)
                        .content(this.objectMapper.writeValueAsString(user)))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("user-create-example",
                        requestFields(fieldWithPath("username").description("Unique friendly identifier for the user."),
                                fieldWithPath("firstName").description("First name of the user"),
                                fieldWithPath("lastName").description("Last name of the user"),
                                fieldWithPath("dealerId").description("Dealer id"),
                                fieldWithPath("oemId").description("Manufacturer id"),
                                fieldWithPath("email").description("The user's email address"),
                                fieldWithPath("phone").description("The user's phone number"),
                                fieldWithPath("address").description("The user's address"),
                                fieldWithPath("createdDate").description("Created date").type("Date"),
                                fieldWithPath("roles").description("User roles. Supported role values: OWNER, ASSOCIATE, TECHNICIAN, DEALER, OEM, BWG, ADMIN").type("List<String>"),
                                fieldWithPath("notes").description("The notes about the user"))));

//        helper.deleteUser((String) user.get("username"));
    }

    @Test
    public void userPasswordChangeExample() throws Throwable {
        this.realUserRepository.deleteAll();
        this.addressRepository.deleteAll();

        Address address = createAddress();
        User owner5 = createUser("user0003", "chynde", "Chrissie", "Hynde", "dealer001", "oem001", address, Arrays.asList("OWNER"), null);
        userRepository.save(owner5);

        ScimPerson scimPerson = new ScimPerson();
        scimPerson.setUserName("chynde");

        when(gluuHelper.findPerson(any(User.class))).thenReturn(scimPerson);
        when(gluuHelper.updatePerson(any(ScimPerson.class))).thenReturn(scimPerson);
        when(userRepository.findByUsername(any(String.class))).thenReturn(owner5);

        final Map<String, Object> user = new HashMap<>();
        user.put("password", "reckless");

        this.userRegMockMvc
                .perform(post("/user-registration/{0}/changePassword/", owner5.get_id())
                        .contentType(MediaTypes.HAL_JSON)
                        .header("remote_user", "chynde")
                        .content(this.objectMapper.writeValueAsString(user)))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("user-password-update-example",
                        requestFields(fieldWithPath("password").description("newPassword"))));
    }

    @Test
    public void userUpdateExample() throws Exception {
        this.realUserRepository.deleteAll();
        this.addressRepository.deleteAll();

        Address address = createAddress();
        User user = createUser("meddy", "Mo", "Eddy", "111", "222", address, Arrays.asList("OWNER"), null);

        final Map<String, Object> userUpdate = new HashMap<>();
        userUpdate.put("username", user.getUsername());
        userUpdate.put("firstName", "Maurice");
        userUpdate.put("lastName", user.getLastName());
        userUpdate.put("dealerId", user.getDealerId());
        userUpdate.put("oemId", user.getOemId());
        userUpdate.put("address", address);
        userUpdate.put("createdDate", user.getCreatedDate());
        userUpdate.put("modifiedDate", new Date());
        userUpdate.put("email", "alee@rush.net");
        userUpdate.put("phone", "(800)222-3456");
        userUpdate.put("roles", Arrays.asList("OWNER"));
        userUpdate.put("notes", "edited notes for Mo");

        this.mockMvc
                .perform(put("/user-registration/{0}", user.get_id()).contentType(MediaTypes.HAL_JSON)
                        .content(this.objectMapper.writeValueAsString(userUpdate)))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("user-update-example",
                        requestFields(fieldWithPath("username").description("Unique, friendly name for the user"),
                                fieldWithPath("firstName").description("First name of the user"),
                                fieldWithPath("lastName").description("Last name of the user"),
                                fieldWithPath("dealerId").description("Dealer id"),
                                fieldWithPath("oemId").description("Manufacturer id"),
                                fieldWithPath("email").description("The user's email address"),
                                fieldWithPath("phone").description("The user's phone number"),
                                fieldWithPath("address").description("Address"),
                                fieldWithPath("createdDate").description("Created date").type("Date"),
                                fieldWithPath("modifiedDate").description("Last modified date").type("Date"),
                                fieldWithPath("roles").description("User roles. Supported role values: OWNER, ASSOCIATE, TECHNICIAN, DEALER, OEM, BWG, ADMIN").type("List<String>"),
                                fieldWithPath("notes").description("User's notes"))));
    }

    @Test
    public void userRemoveExample() throws Throwable {
        ScimPerson scimPerson = new ScimPerson();
        scimPerson.setUserName("dmw@mailinator.com");


        this.realUserRepository.deleteAll();
        this.addressRepository.deleteAll();

        Address address = createAddress();
        User admin = createUser("boss", "Boss", "Tones", "111", "222", address, Arrays.asList("DEALER","ADMIN"), null);
        User user = createUser("dmw", "Dedman", "Walkin", "111", "222", address, Arrays.asList("DEALER","ASSOCIATE"), null);
        userRepository.save(admin);
        userRepository.save(user);

        Link mockLink = realEntityLinks.linkToSingleResource(User.class, user.get_id());

        when(gluuHelper.findPerson(any(User.class))).thenReturn(scimPerson);
        when(gluuHelper.createUser(any(User.class))).thenReturn(scimPerson);
        when(userRepository.findByUsername(admin.getUsername())).thenReturn(admin);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(userRepository.findOne(any(String.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(entityLinks.linkToSingleResource(User.class, user.get_id())).thenReturn(mockLink);


        // verify user is in db
        this.mockMvc.perform(get("/users/{0}", user.get_id())).andExpect(status().isOk())
                .andExpect(jsonPath("firstName", is(user.getFirstName())))
                .andExpect(jsonPath("active", is(Boolean.TRUE)));

        // perform removeUser
        this.userRegMockMvc
                .perform(post("/user-registration/{0}/remove", user.get_id()).contentType(MediaTypes.HAL_JSON)
                        .header("remote_user", "boss@mailinator.com"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("active", is(Boolean.FALSE)))
                .andDo(document("user-remove-example",
                responseFields(
                        fieldWithPath("_id").description("Object Id"),
                        fieldWithPath("username").description("Unique string for the user"),
                        fieldWithPath("firstName").description("First name of the user"),
                        fieldWithPath("lastName").description("Last name of the user"),
                        fieldWithPath("fullName").description("First and Last name"),
                        fieldWithPath("dealerId").description("dealer id"),
                        fieldWithPath("oemId").description("Manufacturer id"),
                        fieldWithPath("roles").description("User roles. Supported role values: OWNER, ASSOCIATE, TECHNICIAN, DEALER, OEM, BWG, ADMIN").type("List<String>"),
                        fieldWithPath("email").description("The user's email address"),
                        fieldWithPath("phone").description("The user's phone number"),
                        fieldWithPath("address").description("User's address"),
                        fieldWithPath("createdDate").description("User creation date").type("Date"),
                        fieldWithPath("modifiedDate").description("Date of last update").optional().type("Date"),
                        fieldWithPath("active").description("Date of last update").optional().type("boolean"),
                        fieldWithPath("inactivatedDate").description("Date user inactivated").optional().type("Date"),
                        fieldWithPath("inactivatedBy").description("who performed the inactivation").optional().type("String"),
                        fieldWithPath("_links")
                                .description("<<resources-user-links,Links>> to other resources").optional().type(Object.class),
                        fieldWithPath("links").ignored(),
                        fieldWithPath("notes").description("User's notes"))));

    }


    @Test
    public void userRestoreExample() throws Throwable {
        ScimPerson scimPerson = new ScimPerson();
        scimPerson.setUserName("dmw@mailinator.com");

        this.realUserRepository.deleteAll();
        this.addressRepository.deleteAll();

        Address address = createAddress();
        User admin = createUser("boss", "Boss", "Tones", "111", "222", address, Arrays.asList("DEALER","ADMIN"), null);
        User user = createUser("dmw", "Dedman", "Walkin", "111", "222", address, Arrays.asList("DEALER","ASSOCIATE"), null);
        userRepository.save(admin);
        userRepository.save(user);

        Link mockLink = realEntityLinks.linkToSingleResource(User.class, user.get_id());

        when(gluuHelper.findPerson(any(User.class))).thenReturn(scimPerson);
        when(gluuHelper.createUser(any(User.class))).thenReturn(scimPerson);
        when(userRepository.findByUsername(admin.getUsername())).thenReturn(admin);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(userRepository.findOne(any(String.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(entityLinks.linkToSingleResource(User.class, user.get_id())).thenReturn(mockLink);


        // verify user is in db
        this.mockMvc.perform(get("/users/{0}", user.get_id())).andExpect(status().isOk())
                .andExpect(jsonPath("firstName", is(user.getFirstName())))
                .andExpect(jsonPath("active", is(Boolean.TRUE)));

        // perform removeUser
        this.userRegMockMvc
                .perform(post("/user-registration/{0}/remove", user.get_id()).contentType(MediaTypes.HAL_JSON)
                        .header("remote_user", "boss@mailinator.com"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("active", is(Boolean.FALSE)));



        // perform user restore
        this.userRegMockMvc
                .perform(post("/user-registration/{0}/restore", user.get_id()).contentType(MediaTypes.HAL_JSON)
                        .header("remote_user", "boss@mailinator.com"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("active", is(Boolean.TRUE)))
                .andDo(document("user-restore-example",
                        responseFields(
                                fieldWithPath("_id").description("Object Id"),
                                fieldWithPath("username").description("Unique string for the user"),
                                fieldWithPath("firstName").description("First name of the user"),
                                fieldWithPath("lastName").description("Last name of the user"),
                                fieldWithPath("fullName").description("First and Last name"),
                                fieldWithPath("dealerId").description("dealer id"),
                                fieldWithPath("oemId").description("Manufacturer id"),
                                fieldWithPath("roles").description("User roles. Supported role values: OWNER, ASSOCIATE, TECHNICIAN, DEALER, OEM, BWG, ADMIN").type("List<String>"),
                                fieldWithPath("email").description("The user's email address"),
                                fieldWithPath("phone").description("The user's phone number"),
                                fieldWithPath("address").description("User's address"),
                                fieldWithPath("createdDate").description("User creation date").type("Date"),
                                fieldWithPath("modifiedDate").description("Date of last update").optional().type("Date"),
                                fieldWithPath("active").description("Date of last update").optional().type("boolean"),
                                fieldWithPath("_links")
                                        .description("<<resources-user-links,Links>> to other resources").optional().type(Object.class),
                                fieldWithPath("links").ignored(),
                                fieldWithPath("notes").description("User's notes"))));


        //verify that user is active again
        this.mockMvc.perform(get("/users/{0}", user.get_id())).andExpect(status().isOk())
                .andExpect(jsonPath("firstName", is(user.getFirstName())))
                .andExpect(jsonPath("active", is(Boolean.TRUE)));

        // verify find by user works
        this.mockMvc.perform(get("/users/search/findByUsername?username={0}", user.getUsername()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("firstName", is(user.getFirstName())));
    }

    @Test
    public void userGetExample() throws Exception {
        this.realUserRepository.deleteAll();
        this.addressRepository.deleteAll();

        Address address = createAddress();
        User user = createUser("afranklin", "Aretha", "Franklin", "111", "222", address, Arrays.asList("USER"), null);

        this.mockMvc.perform(get("/users/{0}?active=true", user.get_id())).andExpect(status().isOk())
                .andExpect(jsonPath("firstName", is(user.getFirstName())))
                .andDo(document("user-get-example",
                        links(linkWithRel("self").description("This <<resources-user,user>>"),
                                linkWithRel("user").description("This <<resources-user,user>>")),
                        responseFields(
                                fieldWithPath("_id").description("Object Id"),
                                fieldWithPath("username").description("Unique string for the user"),
                                fieldWithPath("firstName").description("First name of the user"),
                                fieldWithPath("lastName").description("Last name of the user"),
                                fieldWithPath("fullName").description("First and Last name"),
                                fieldWithPath("dealerId").description("dealer id"),
                                fieldWithPath("oemId").description("Manufacturer id"),
                                fieldWithPath("roles").description("User roles. Supported role values: OWNER, ASSOCIATE, TECHNICIAN, DEALER, OEM, BWG, ADMIN").type("List<String>"),
                                fieldWithPath("email").description("The user's email address"),
                                fieldWithPath("phone").description("The user's phone number"),
                                fieldWithPath("address").description("User's address"),
                                fieldWithPath("createdDate").description("User creation date").type("Date"),
                                fieldWithPath("modifiedDate").description("Date of last update").optional().type("Date"),
                                fieldWithPath("active").description("Date of last update").optional().type("boolean"),
                                fieldWithPath("inactivatedDate").description("Date user inactivated").optional().type("Date"),
                                fieldWithPath("inactivatedBy").description("who performed the inactivation").optional().type("String"),
                                fieldWithPath("_links")
                                        .description("<<resources-user-links,Links>> to other resources"),
                                fieldWithPath("notes").description("User's notes"))));
    }

    @Test
    public void userFindByUsername() throws Exception {
        this.realUserRepository.deleteAll();
        this.addressRepository.deleteAll();

        Address address = createAddress();
        User user = createUser("veddy", "Eddie", "Vedder", "111", "222", address, Arrays.asList("OWNER"), null);

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
                                fieldWithPath("fullName").description("First and Last name"),
                                fieldWithPath("dealerId").description("Dealer id"),
                                fieldWithPath("oemId").description("Manufacturer id"),
                                fieldWithPath("roles").description("User roles. Supported role values: OWNER, ASSOCIATE, TECHNICIAN, DEALER, OEM, BWG, ADMIN").type("List<String>"),
                                fieldWithPath("email").description("The user's email address"),
                                fieldWithPath("phone").description("The user's phone number"),
                                fieldWithPath("address").description("User's address"),
                                fieldWithPath("createdDate").description("User creation date").type("Date"),
                                fieldWithPath("modifiedDate").description("Date of last update").optional().type("Date"),
                                fieldWithPath("active").description("Date of last update").optional().type("boolean"),
                                fieldWithPath("inactivatedDate").description("Date user inactivated").optional().type("Date"),
                                fieldWithPath("inactivatedBy").description("who performed the inactivation").optional().type("String"),
                                fieldWithPath("_links")
                                        .description("<<resources-user-links,Links>> to other resources"),
                                fieldWithPath("notes").description("User's notes"))));
    }

    private void createVariousUsers(){
        List<Address> addresses = createAddresses(30);

        // create some oems and dealers
        Oem oem1 = createOem("Mod Spas Inc.", "1968", addresses.get(0), "oem001");
        Oem oem2 = createOem("Rockers Ltd.", "1972", addresses.get(1), "oem002");
        Dealer dealer1 = createDealer("Fred's Spas", addresses.get(2), oem1.get_id(), "dealer001");
        Dealer dealer2 = createDealer("Pt. Loma Spa Outlet", addresses.get(3), oem1.get_id(), "dealer002");
        Dealer dealer3 = createDealer("SpaStic", addresses.get(15), oem2.get_id(), "dealer003");

        // create some users
        List<String> ownerRole = Arrays.asList(User.Role.OWNER.name());
        List<String> salesRole = Arrays.asList(User.Role.DEALER.name(), User.Role.ASSOCIATE.name());
        List<String> techRole = Arrays.asList(User.Role.DEALER.name(), User.Role.TECHNICIAN.name());
        List<String> oemRole = Arrays.asList(User.Role.OEM.name());
        List<String> bwgRole = Arrays.asList(User.Role.BWG.name());
        List<String> adminRole = Arrays.asList(User.Role.BWG.name(), User.Role.OEM.name(),
                User.Role.DEALER.name(), User.Role.ADMIN.name());
        List<String> dealerAdminRole = Arrays.asList(User.Role.DEALER.name(), User.Role.ADMIN.name());
        List<String> oemAdminRole = Arrays.asList(User.Role.OEM.name(), User.Role.ADMIN.name());
        List<String> bwgAdminRole = Arrays.asList(User.Role.BWG.name(), User.Role.ADMIN.name());

        User owner1 = createUser("user0001", "braitt", "Bonnie", "Raitt", dealer1.get_id(), oem1.get_id(), addresses.get(4), ownerRole, null);
        User owner2 = createUser("user0002", "ptownsend", "Pete", "Townsend", dealer1.get_id(), oem1.get_id(), addresses.get(4), ownerRole, null);
        User owner3 = createUser("user0003", "pgabriel", "Peter", "Gabriel", dealer1.get_id(), oem1.get_id(), addresses.get(5), ownerRole, null);
        User owner4 = createUser("user0004", "lgaga", "Lady", "Gaga", dealer2.get_id(), oem2.get_id(), addresses.get(6), ownerRole, null);
        User owner5 = createUser("user0005", "chynde", "Chrissie", "Hynde", dealer1.get_id(), oem1.get_id(), addresses.get(7), ownerRole, null);
        User owner6 = createUser("user0025", "jcroce", "Jim", "Croce", dealer2.get_id(), oem1.get_id(), addresses.get(26), ownerRole, null);
        User owner7 = createUser("user0026", "mmathers", "Micheal", "Mathers", dealer3.get_id(), oem2.get_id(), addresses.get(27), ownerRole, null);
        User owner8 = createUser("user0027", "sdogg", "Calvin", "Broadus", dealer3.get_id(), oem2.get_id(), addresses.get(28), ownerRole, null);

        User maker1 = createUser("user0006", "jpage", "Jimmy", "Page", null, oem1.get_id(), addresses.get(8), oemRole, null);
        User maker2 = createUser("user0007", "pfenton", "Peter", "Fenton", null, oem2.get_id(), addresses.get(9), oemRole, null);
        User pink   = createUser("user0008", "bgeldof", "Bob", "Geldof", null, null, addresses.get(10), bwgRole, null);
        User oz     = createUser("user0009", "oosborn", "Ozzie", "Osborn", null, null, addresses.get(11), adminRole, null);

        User sales1 = createUser("user0010", "nfinn", "Neil", "Finn", dealer1.get_id(), oem1.get_id(), addresses.get(12), salesRole, null);
        User sales2 = createUser("user0011", "bpreston", "Billy", "Preston", dealer1.get_id(), oem1.get_id(), addresses.get(12), salesRole, null);
        User sales3 = createUser("user0012", "cclemons", "Clarence", "Clemons", dealer2.get_id(), oem1.get_id(), addresses.get(12), salesRole, null);
        User tech1  = createUser("user0013", "wgates", "William", "Gates", dealer1.get_id(), oem1.get_id(), addresses.get(13), techRole, null);
        User tech2  = createUser("user0014", "sjobs", "Stefan", "Jobs", dealer1.get_id(), oem1.get_id(), addresses.get(14), techRole, null);

        User dealer1Admin   = createUser("user0015", "tpetty", "Tom", "Petty", dealer1.get_id(), null, addresses.get(16), dealerAdminRole, null);
        User dealer2Admin   = createUser("user0016", "jbrown", "James", "Brown", dealer2.get_id(), null, addresses.get(17), dealerAdminRole, null);
        User dealer3Admin   = createUser("user0017", "ptosh", "Peter", "Tosh", dealer3.get_id(), null, addresses.get(18), dealerAdminRole, null);
        User oem1Admin   = createUser("user0018", "smorse", "Steve", "Morse", null, oem1.get_id(), addresses.get(19), oemAdminRole, null);
        User oem2Admin   = createUser("user0019", "jmitchell", "Joni", "Mitchell", null, oem2.get_id(), addresses.get(20), oemAdminRole, null);
        User bwgAdmin   = createUser("user0020", "blondie", "Debbi", "Harry", null, null, addresses.get(21), bwgAdminRole, null);

        User salesD2 = createUser("user0021", "psimon", "Paul", "Simon", dealer2.get_id(), oem1.get_id(), addresses.get(22), salesRole, null);
        User salesD3 = createUser("user0021", "mwaters", "Muddy", "Waters", dealer3.get_id(), oem2.get_id(), addresses.get(23), salesRole, null);
        User techD2  = createUser("user0023", "csagan", "Carl", "Sagan", dealer2.get_id(), oem1.get_id(), addresses.get(24), techRole, null);
        User techD3  = createUser("user0024", "shawking", "Stephen", "Hawking", dealer3.get_id(), oem2.get_id(), addresses.get(25), techRole, null);
        return;
    }
}
