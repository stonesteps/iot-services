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
public final class SpaTemplateDocumentation extends ModelTestBase {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Autowired
    private SpaTemplateRepository spaTemplateRepository;

    @Autowired
    private MaterialRepository materialRepository;

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
    public void templateListExample() throws Exception {
        clearAllData();
        createSpaTemplates();
        this.mockMvc.perform(get("/spaTemplates")).andExpect(status().isOk())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page.totalElements", is(3)))
                .andExpect(jsonPath("page.totalPages", is(1)))
                .andDo(document("template-list-example",
                        responseFields(
                                fieldWithPath("_embedded.spaTemplates")
                                        .description("An array of <<resources-spaTemplate, SpaTemplate resources>>"),
                                fieldWithPath("_links").description("<<resources-templatelist-links,Links>> to other resources"),
                                fieldWithPath("page").description("Page information"))));
    }

    @Test
    public void findSpaTemplatesByOemExample() throws Exception {
        clearAllData();
        List<SpaTemplate> spaTemplates = createSpaTemplates();

        this.mockMvc.perform(get("/spaTemplates/search/findByOemId?oemId=oem001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("page.totalElements", is(2)))
                .andDo(document("templates-findbyoem-example",
                        requestParameters(parameterWithName("oemId").description("The unique identifier for the spa Manufacturer"))))
                .andDo(document("templates-findbyoem-example",
                        responseFields(
                                fieldWithPath("_embedded.spaTemplates").description("An array of <<resources-spaTemplate, SpaTemplate resources>>"),
                                fieldWithPath("_links").description("<<resources-templatelist-links,Links>> to other resources"),
                                fieldWithPath("page").description("Page information"))));
    }


    @Test
    public void spaTemplateCreateExample() throws Exception {
        this.clearAllData();

        List<Address> addresses = createAddresses(2);
        Oem oem1 = createOem("Blue Wave Spas, LTD", addresses.get(0), "oem001" );
        Oem oem2 = createOem("Jazzi Pool & Spa Products, LTD", addresses.get(1), "oem002");

        // create set of materials
        List<Material> materialList = setupTestMaterials(oem1, oem2);
        List<Material> spaTemplate1List = Arrays.asList(materialList.get(2), materialList.get(8));

        final Map<String, Object> spaTemplateMap = new HashMap<>();
        spaTemplateMap.put("productName", "Fishy Spas");
        spaTemplateMap.put("model", "Grouper");
        spaTemplateMap.put("sku", "2052350-125516161");
        spaTemplateMap.put("oemId", "oem001");
        spaTemplateMap.put("notes", "Test Spa");
        spaTemplateMap.put("materialList", spaTemplate1List);
        spaTemplateMap.put("creationDate", new Date());


        this.mockMvc
                .perform(post("/spaTemplates").contentType(MediaTypes.HAL_JSON)
                        .content(this.objectMapper.writeValueAsString(spaTemplateMap)))
                .andExpect(status().isCreated())
                .andDo(document("template-create-example",
                        requestFields(fieldWithPath("productName").description("Spa Product Line"),
                                fieldWithPath("model").description("Model Name of the spa"),
                                fieldWithPath("sku").description("The SKU number associated to this Spa"),
                                fieldWithPath("oemId").description("Manufacturer ids"),
                                fieldWithPath("notes").description("Field to store extra text about the spa template").optional().type("String"),
                                fieldWithPath("materialList").description("List of Materials used to build the spa").type("List<Material>"),
                                fieldWithPath("attachments").description("References to supporting documents").optional().type("List<String>"),
                                fieldWithPath("creationDate").description("Created date").type("Date"))));
    }

    @Test
    public void spaTemplateGetExample() throws Exception {
        clearAllData();
        List<SpaTemplate> spaTemplates = createSpaTemplates();

        this.mockMvc.perform(get("/spaTemplates/{0}", spaTemplates.get(0).get_id())).andExpect(status().isOk())
                .andExpect(jsonPath("model", is(spaTemplates.get(0).getModel())))
                .andDo(document("template-get-example",
                        links(linkWithRel("self").description("This <<resources-spaTemplate, SpaTemplate>>"),
                                linkWithRel("spaTemplate").description("This <<resources-spaTemplate,SpaTemplate>>")),
                        responseFields(
                                fieldWithPath("productName").description("Spa Product Line"),
                                fieldWithPath("model").description("Model Name of the spa"),
                                fieldWithPath("sku").description("The SKU number associated to this Spa"),
                                fieldWithPath("oemId").description("Manufacturer ids"),
                                fieldWithPath("notes").description("Field to store extra text about the spa template").optional().type("String"),
                                fieldWithPath("materialList").description("List of Materials used to build the spa").type("List<Material>"),
                                fieldWithPath("attachments").description("References to supporting documents").optional().type("List<String>"),
                                fieldWithPath("creationDate").description("Created date").type("Date"),
                                fieldWithPath("_links")
                                        .description("<<resources-templatelist-links,Links>> to other resources"))));
    }

}