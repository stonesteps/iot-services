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

        this.mockMvc.perform(get("/dashboard")
                    .param("role","dealer").param("id","dealer001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("alertCounts.totalAlertCount", is(42)))
                .andDo(document("dashboard-dealer-example",
                        requestParameters(parameterWithName("role")
                                .description("The perspective of the user making the requests. "
                                        + "Acceptable role values: BWG, OEM, ASSOCIATE, TECHNICIAN, DEALER"),
                            parameterWithName("id")
                                .description("unique identifier. Could be either an oemId, dealerId, or userId depending on the role: for TECHNICIAN or ASSOCIATE use the userId. "
                                        + "For dealer admin use dealerId. For OEM use oemId. BWG role does not require an id."))))
                .andDo(document("dashboard-dealer-example",
                        links(
                                linkWithRel("alertList").description("Link to the list of <<resources-alert, Alerts>>"),
                                linkWithRel("spaList").description("Link to the list of <<resources-spa, Spas>>"),
                                linkWithRel("ownerList").description("Link to the list of <<resources-user, Owners>>"),
                                linkWithRel("messageList").description("Link to the list of Messages"),
                                linkWithRel("self").description("Link to this API")),
                        responseFields(
                                fieldWithPath("alertCounts")
                                        .description("A map containing counts for total alerts, red alerts, and yellow alerts"),
                                fieldWithPath("spaCounts")
                                        .description("A map of counts for total spas, sold spas, and online spas"),
                                fieldWithPath("messageCounts")
                                        .description("A map containing total message count and new message count."),
                                fieldWithPath("_links").description("<<resources-dashboard-links,Links>> to other resources"))));
    }

    @Test
    public void oemDashboardExample() throws Exception {

        this.mockMvc.perform(get("/dashboard")
                .param("role","oem").param("id","oem001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("alertCounts.totalAlertCount", is(42)))
                .andDo(document("dashboard-oem-example",
                        links(
                                linkWithRel("alertList").description("Link to the list of <<resources-alert, Alerts>>"),
                                linkWithRel("spaList").description("Link to the list of <<resources-spa, Spas>>"),
                                linkWithRel("ownerList").description("Link to the list of <<resources-user, Owners>>"),
                                linkWithRel("messageList").description("Link to the list of Messages"),
                                linkWithRel("self").description("Link to this API")),
                        responseFields(
                                fieldWithPath("alertCounts")
                                        .description("A map containing counts for total alerts, red alerts, and yellow alerts"),
                                fieldWithPath("spaCounts")
                                        .description("A map of counts for total spas, sold spas, and online spas"),
                                fieldWithPath("messageCounts")
                                        .description("A map containing total message count and new message count."),
                                fieldWithPath("_links").description("<<resources-dashboard-links,Links>> to other resources"))));
    }

    @Test
    public void associateDashboardExample() throws Exception {

        this.mockMvc.perform(get("/dashboard")
                .param("role","associate").param("id","user011"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("alertCounts.totalAlertCount", is(42)))
                .andDo(document("dashboard-associate-example",
                        links(
                                linkWithRel("alertList").description("Link to the list of <<resources-alert, Alerts>>"),
                                linkWithRel("spaList").description("Link to the list of <<resources-spa, Spas>>"),
                                linkWithRel("ownerList").description("Link to the list of <<resources-user, Owners>>"),
                                linkWithRel("messageList").description("Link to the list of Messages"),
                                linkWithRel("self").description("Link to this API")),
                        responseFields(
                                fieldWithPath("alertCounts")
                                        .description("A map containing counts for total alerts, red alerts, and yellow alerts"),
                                fieldWithPath("spaCounts")
                                        .description("A map of counts for total spas, sold spas, and online spas"),
                                fieldWithPath("messageCounts")
                                        .description("A map containing total message count and new message count."),
                                fieldWithPath("_links").description("<<resources-dashboard-links,Links>> to other resources"))));
    }
    
    @Test
    public void technicianDashboardExample() throws Exception {

        this.mockMvc.perform(get("/dashboard")
                .param("role","technician").param("id","user019"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("alertCounts.totalAlertCount", is(42)))
                .andDo(document("dashboard-technician-example",
                        links(
                                linkWithRel("alertList").description("Link to the list of <<resources-alert, Alerts>>"),
                                linkWithRel("spaList").description("Link to the list of <<resources-spa, Spas>>"),
                                linkWithRel("ownerList").description("Link to the list of <<resources-user, Owners>>"),
                                linkWithRel("messageList").description("Link to the list of Messages"),
                                linkWithRel("self").description("Link to this API")),
                        responseFields(
                                fieldWithPath("alertCounts")
                                        .description("A map containing counts for total alerts, red alerts, and yellow alerts"),
                                fieldWithPath("spaCounts")
                                        .description("A map of counts for total spas, sold spas, and online spas"),
                                fieldWithPath("messageCounts")
                                        .description("A map containing total message count and new message count."),
                                fieldWithPath("_links").description("<<resources-dashboard-links,Links>> to other resources"))));
    }

    @Test
    public void bwgDashboardExample() throws Exception {

        this.mockMvc.perform(get("/dashboard").param("role","bwg"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("alertCounts.totalAlertCount", is(42)))
                .andDo(document("dashboard-bwg-example",
                        links(
                                linkWithRel("alertList").description("Link to the list of <<resources-alert, Alerts>>"),
                                linkWithRel("spaList").description("Link to the list of <<resources-spa, Spas>>"),
                                linkWithRel("ownerList").description("Link to the list of <<resources-user, Owners>>"),
                                linkWithRel("messageList").description("Link to the list of Messages"),
                                linkWithRel("self").description("Link to this API")),
                        responseFields(
                                fieldWithPath("alertCounts")
                                        .description("A map containing counts for total alerts, red alerts, and yellow alerts"),
                                fieldWithPath("spaCounts")
                                        .description("A map of counts for total spas, sold spas, and online spas"),
                                fieldWithPath("messageCounts")
                                        .description("A map containing total message count and new message count."),
                                fieldWithPath("_links").description("<<resources-dashboard-links,Links>> to other resources"))));
    }
}
