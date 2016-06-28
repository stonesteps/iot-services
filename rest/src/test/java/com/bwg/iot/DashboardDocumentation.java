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

import java.util.*;

import static org.hamcrest.Matchers.is;
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
public final class DashboardDocumentation extends ModelTestBase {

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
    public void dealerDashboardExample() throws Exception {
        clearAllData();
        List<Address> addresses = createAddresses(40);

        // create some oems and dealers
        Oem oem1 = createOem("Sundance Spas", 103498, addresses.get(0), "oem001");
        Oem oem2 = createOem("Maax Spas Industries Corp.", 102188, addresses.get(1), "oem002");
        Oem oem3 = createOem("Coast Spas Manufacturing Inc.", 100843, addresses.get(26), "oem003");

        Dealer dealer1 = createDealer("Sundance Spas", addresses.get(2), oem1.get_id(), "dealer001");
        Dealer dealer2 = createDealer("Pt. Loma Spa Outlet", addresses.get(3), oem1.get_id(), "dealer002");
        Dealer dealer3 = createDealer("Valley Hot Springs Spas", addresses.get(15), oem3.get_id(), "dealer003");

        List<String> ownerRole = Arrays.asList(User.Role.OWNER.name());
        List<String> salesRole = Arrays.asList(User.Role.DEALER.name(), User.Role.ASSOCIATE.name());
        List<String> techRole = Arrays.asList(User.Role.DEALER.name(), User.Role.TECHNICIAN.name());
        List<String> oemRole = Arrays.asList(User.Role.OEM.name());
        List<String> oemDealerRole = Arrays.asList(User.Role.OEM.name(), User.Role.ASSOCIATE.name());
        List<String> bwgRole = Arrays.asList(User.Role.BWG.name());
        List<String> adminRole = Arrays.asList(User.Role.BWG.name(), User.Role.OEM.name(),
                User.Role.DEALER.name(), User.Role.ADMIN.name());
        List<String> dealerAdminRole = Arrays.asList(User.Role.DEALER.name(), User.Role.ADMIN.name());
        List<String> oemAdminRole = Arrays.asList(User.Role.OEM.name(), User.Role.ADMIN.name(), User.Role.ASSOCIATE.name());
        List<String> bwgAdminRole = Arrays.asList(User.Role.BWG.name(), User.Role.ADMIN.name());

        // create some people
        User owner1 = createUser("user0001", "braitt", "Bonnie", "Raitt", dealer1.get_id(), oem1.get_id(), addresses.get(4), ownerRole, "Bonnie is the owner of this spa");
        User owner2 = createUser("user0002", "ptownsend", "Pete", "Townsend", dealer3.get_id(), oem3.get_id(), addresses.get(4), ownerRole, null);
        User owner3 = createUser("user0003", "pgabriel", "Peter", "Gabriel", dealer1.get_id(), oem1.get_id(), addresses.get(5), ownerRole, null);
        User owner4 = createUser("user0004", "lgaga", "Lady", "Gaga", dealer2.get_id(), oem2.get_id(), addresses.get(6), ownerRole, null);
        User owner5 = createUser("user0005", "chynde", "Chrissie", "Hynde", dealer1.get_id(), oem1.get_id(), addresses.get(7), ownerRole, null);
        User owner6 = createUser("user0025", "jcroce", "Jim", "Croce", dealer2.get_id(), oem1.get_id(), addresses.get(26), ownerRole, null);
        User owner7 = createUser("user0026", "mmathers", "Marshall", "Mathers", dealer3.get_id(), oem2.get_id(), addresses.get(27), ownerRole, null);
        User owner8 = createUser("user0027", "sdogg", "Calvin", "Broadus", dealer3.get_id(), oem2.get_id(), addresses.get(28), ownerRole, null);

        User maker1 = createUser("user0006", "jpage", "Jimmy", "Page", dealer1.get_id(), oem1.get_id(), addresses.get(8), oemDealerRole, null);
        User maker2 = createUser("user0007", "pfenton", "Peter", "Fenton", null, oem2.get_id(), addresses.get(9), oemRole, null);
        User sales3 = createUser("user0012", "cclemons", "Clarence", "Clemons", dealer3.get_id(), oem3.get_id(), addresses.get(12), oemDealerRole, null);
        User pink   = createUser("user0008", "bgeldof", "Bob", "Geldof", null, null, addresses.get(10), bwgRole, null);
        User oz     = createUser("user0009", "oosborn", "Ozzie", "Osborn", null, null, addresses.get(11), adminRole, null);

        User sales1 = createUser("user0010", "nfinn", "Neil", "Finn", dealer1.get_id(), oem1.get_id(), addresses.get(12), salesRole, null);
        User sales2 = createUser("user0011", "bpreston", "Billy", "Preston", dealer3.get_id(), oem3.get_id(), addresses.get(12), salesRole, null);
        User sales4 = createUser("user0025", "rwaters", "Roger", "Waters", dealer3.get_id(), oem2.get_id(), addresses.get(28), salesRole, null);
        User tech1  = createUser("user0013", "wgates", "William", "Gates", dealer1.get_id(), oem1.get_id(), addresses.get(13), techRole, null);
        User tech2  = createUser("user0014", "sjobs", "Stefan", "Jobs", dealer1.get_id(), oem1.get_id(), addresses.get(14), techRole, null);

        User dealer1Admin   = createUser("user0015", "tpetty", "Tom", "Petty", dealer1.get_id(), null, addresses.get(16), dealerAdminRole, null);
        User dealer2Admin   = createUser("user0016", "jbrown", "James", "Brown", dealer2.get_id(), null, addresses.get(17), dealerAdminRole, null);
        User dealer3Admin   = createUser("user0017", "ptosh", "Peter", "Tosh", dealer3.get_id(), null, addresses.get(18), dealerAdminRole, null);
        User oem1Admin   = createUser("user0018", "smorse", "Steve", "Morse", null, oem1.get_id(), addresses.get(19), oemAdminRole, null);
        User oem2Admin   = createUser("user0019", "jmitchell", "Joni", "Mitchell", null, oem2.get_id(), addresses.get(20), oemAdminRole, null);
        User oem3Admin   = createUser("user0028", "fmercury", "Fred", "Mercury", null, oem3.get_id(), addresses.get(30), oemAdminRole, null);
        User bwgAdmin   = createUser("user0020", "blondie", "Debbi", "Harry", null, null, addresses.get(21), bwgAdminRole, null);

        User salesD2 = createUser("user0021", "psimon", "Paul", "Simon", dealer2.get_id(), oem1.get_id(), addresses.get(22), salesRole, null);
        User salesD3 = createUser("user0021", "mwaters", "Muddy", "Waters", dealer3.get_id(), oem2.get_id(), addresses.get(23), salesRole, null);
        User techD2  = createUser("user0023", "csagan", "Carl", "Sagan", dealer2.get_id(), oem1.get_id(), addresses.get(24), techRole, null);
        User techD3  = createUser("user0024", "shawking", "Stephen", "Hawking", dealer3.get_id(), oem3.get_id(), addresses.get(25), techRole, null);

        // create some spas
        Spa spa24 = createDemoSpa2("160104", oem1.get_id(), dealer1.get_id(), owner2, "spa000024", "012345", "67894", sales3);
        Spa spa25 = createSmallSpaWithState("151122", "Fish", "Minnow", oem1.get_id(), dealer1.get_id(), owner3, "spa000025", "abcd", "67894", sales2);
        this.add2Alerts(spa25);
        Spa spa26 = createFullSpaWithState("160229", "Shark", "Tiger", oem2.get_id(), dealer2.get_id(), owner4, "spa000026", "abcd002", sales3, "76092", null);
        spa26 = this.addOverheatRedAlert(spa26);
        Spa spa27 = createDemoSpa3("160315", oem1.get_id(), dealer1.get_id(), owner5, "spa000027", "abcd003", "template01", sales1);

        Spa spa23 = createSmallSpaWithState("160412", "Fish", "Minnow", oem1.get_id(), dealer2.get_id(), owner6, "spa000023", "abcd004", "template01", maker1);
        Spa spa28 = createSmallSpaWithState("160412", "Shark", "Card", oem2.get_id(), dealer3.get_id(), owner7, "spa000028", "abcd005", "template01", sales4);
        Spa spa29 = createSmallSpaWithState("160412", "Shark", "Land", oem2.get_id(), dealer3.get_id(), owner8, "spa000029", "abcd006", "template01", sales4);

        Spa spa1  = createSmallSpaWithState("160217", "Shark", "Hammerhead", oem1.get_id(), dealer1.get_id(), null, "spa000001", "template01", null);
        Spa spa2  = createSmallSpaWithState("160217", "Shark", "Hammerhead", oem1.get_id(), dealer1.get_id(), null, "spa000002", "template01", null);


        this.mockMvc.perform(get("/dashboard")
                    .header("remote_user","jpage"))
                .andExpect(status().isOk())
                .andDo(document("dashboard-oem-example",
                        links(
                                linkWithRel("alertList").description("Link to the list of <<resources-alert, Alerts>>"),
                                linkWithRel("spaList").description("Link to the list of <<resources-spa, Spas>>"),
                                linkWithRel("ownerList").description("Link to the list of <<resources-user, Owners>>"),
                                linkWithRel("self").description("Link to this API")),
                        responseFields(
                                fieldWithPath("alertCounts")
                                        .description("A map containing counts for total alerts, red alerts, and yellow alerts"),
                                fieldWithPath("spaCounts")
                                        .description("A map of counts for total spas, sold spas, and online spas"),
                                fieldWithPath("_links").description("<<resources-dashboard-links,Links>> to other resources"))));
    }

//    @Test
//    public void oemDashboardExample() throws Exception {
//
//        this.mockMvc.perform(get("/dashboard")
//                .param("role","oem").param("id","oem001"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("alertCounts.totalAlertCount", is(42)))
//                .andDo(document("dashboard-oem-example",
//                        links(
//                                linkWithRel("alertList").description("Link to the list of <<resources-alert, Alerts>>"),
//                                linkWithRel("spaList").description("Link to the list of <<resources-spa, Spas>>"),
//                                linkWithRel("ownerList").description("Link to the list of <<resources-user, Owners>>"),
//                                linkWithRel("messageList").description("Link to the list of Messages"),
//                                linkWithRel("self").description("Link to this API")),
//                        responseFields(
//                                fieldWithPath("alertCounts")
//                                        .description("A map containing counts for total alerts, red alerts, and yellow alerts"),
//                                fieldWithPath("spaCounts")
//                                        .description("A map of counts for total spas, sold spas, and online spas"),
//                                fieldWithPath("messageCounts")
//                                        .description("A map containing total message count and new message count."),
//                                fieldWithPath("_links").description("<<resources-dashboard-links,Links>> to other resources"))));
//    }
//
//    @Test
//    public void associateDashboardExample() throws Exception {
//
//        this.mockMvc.perform(get("/dashboard")
//                .param("role","associate").param("id","user011"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("alertCounts.totalAlertCount", is(42)))
//                .andDo(document("dashboard-associate-example",
//                        links(
//                                linkWithRel("alertList").description("Link to the list of <<resources-alert, Alerts>>"),
//                                linkWithRel("spaList").description("Link to the list of <<resources-spa, Spas>>"),
//                                linkWithRel("ownerList").description("Link to the list of <<resources-user, Owners>>"),
//                                linkWithRel("messageList").description("Link to the list of Messages"),
//                                linkWithRel("self").description("Link to this API")),
//                        responseFields(
//                                fieldWithPath("alertCounts")
//                                        .description("A map containing counts for total alerts, red alerts, and yellow alerts"),
//                                fieldWithPath("spaCounts")
//                                        .description("A map of counts for total spas, sold spas, and online spas"),
//                                fieldWithPath("messageCounts")
//                                        .description("A map containing total message count and new message count."),
//                                fieldWithPath("_links").description("<<resources-dashboard-links,Links>> to other resources"))));
//    }
//
//    @Test
//    public void technicianDashboardExample() throws Exception {
//
//        this.mockMvc.perform(get("/dashboard")
//                .param("role","technician").param("id","user019"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("alertCounts.totalAlertCount", is(42)))
//                .andDo(document("dashboard-technician-example",
//                        links(
//                                linkWithRel("alertList").description("Link to the list of <<resources-alert, Alerts>>"),
//                                linkWithRel("spaList").description("Link to the list of <<resources-spa, Spas>>"),
//                                linkWithRel("ownerList").description("Link to the list of <<resources-user, Owners>>"),
//                                linkWithRel("messageList").description("Link to the list of Messages"),
//                                linkWithRel("self").description("Link to this API")),
//                        responseFields(
//                                fieldWithPath("alertCounts")
//                                        .description("A map containing counts for total alerts, red alerts, and yellow alerts"),
//                                fieldWithPath("spaCounts")
//                                        .description("A map of counts for total spas, sold spas, and online spas"),
//                                fieldWithPath("messageCounts")
//                                        .description("A map containing total message count and new message count."),
//                                fieldWithPath("_links").description("<<resources-dashboard-links,Links>> to other resources"))));
//    }
//
//    @Test
//    public void bwgDashboardExample() throws Exception {
//
//        this.mockMvc.perform(get("/dashboard").param("role","bwg"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("alertCounts.totalAlertCount", is(42)))
//                .andDo(document("dashboard-bwg-example",
//                        links(
//                                linkWithRel("alertList").description("Link to the list of <<resources-alert, Alerts>>"),
//                                linkWithRel("spaList").description("Link to the list of <<resources-spa, Spas>>"),
//                                linkWithRel("ownerList").description("Link to the list of <<resources-user, Owners>>"),
//                                linkWithRel("messageList").description("Link to the list of Messages"),
//                                linkWithRel("self").description("Link to this API")),
//                        responseFields(
//                                fieldWithPath("alertCounts")
//                                        .description("A map containing counts for total alerts, red alerts, and yellow alerts"),
//                                fieldWithPath("spaCounts")
//                                        .description("A map of counts for total spas, sold spas, and online spas"),
//                                fieldWithPath("messageCounts")
//                                        .description("A map containing total message count and new message count."),
//                                fieldWithPath("_links").description("<<resources-dashboard-links,Links>> to other resources"))));
//    }
}
