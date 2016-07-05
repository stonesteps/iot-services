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
public final class MaterialDocumentation extends ModelTestBase{
    @Rule
	public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

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
	public void materialListExample() throws Exception {
		this.mockMvc.perform(get("/materials")).andExpect(status().isOk())
				.andDo(document("materials-list-example",
						responseFields(
								fieldWithPath("_embedded.materials").description("An array of <<resources-material, Material resources>>"),
								fieldWithPath("_links").description("<<resources-materialslist-links,Links>> to other resources"),
								fieldWithPath("page").description("Page information"))));
	}


	@Test
	public void materialGetExample() throws Exception {
		Material t1Panel = createSpaTemplateMaterial("CONTROLLER", "52710-03");

        this.mockMvc.perform(get("/materials/{0}", t1Panel.get_id())).andExpect(status().isOk())
				.andExpect(jsonPath("sku", is(t1Panel.getSku())))
				.andDo(document("material-get-example",
						links(linkWithRel("self").description("This <<resources-material, material>>"),
								linkWithRel("material").description("This <<resources-material, material>>")),
                        responseFields(
                                fieldWithPath("_id").description("Object Id"),
								fieldWithPath("brandName").description("Name associated with this part"),
                                fieldWithPath("description").description("A description of this part"),
                                fieldWithPath("sku").description("The BWG part number of this item."),
                                fieldWithPath("alternateSku").description("A alternate part number that may be used by the OEM.").optional().type("String"),
								fieldWithPath("materialType").description("The kind of material item this piece of equipment actually is.").optional().type("String"),
								fieldWithPath("componentType").description("Usually the same as materialType. But when used " +
										"inside a spa template, actually means: The type of input connection point as seen by the spa controller. "
										+ "This distinction is necessary because piece of equipment may be hooked to any of the spa controller inputs."),
                                fieldWithPath("warrantyDays").description("The type of component.").optional().type(Integer.class),
                                fieldWithPath("uploadDate").description("The date this information was uploaded into the system."),
                                fieldWithPath("oemId").description("The OEMS that use this part number").optional().type(List.class),
								fieldWithPath("displayName").description("A friendly name to refer to this piece of equipment "
										+ "as designed in a specific model of a spa.  Note: It is not saved in the Materials db table."
										+ "Rather this name is saved embedded saved spaTemplates.").optional().type("String"),
								fieldWithPath("port").description("When used in a spa template, indicates the spa controller board connection input assignment. (0 - based)").optional().type("String"),
                                fieldWithPath("_links")
                                        .description("<<resources-oem-links,Links>> to other resources"))));
	}

    @Test
    public void findBySkuExample() throws Exception {
		String testSku = "52710-03";

        this.mockMvc.perform(get("/materials/search/findBySku?sku=" + testSku))
                .andExpect(status().isOk())
                .andDo(document("materials-findbySku-example",
                        responseFields(
                                fieldWithPath("_id").description("Object Id"),
								fieldWithPath("brandName").description("Name associated with this part"),
								fieldWithPath("description").description("A description of this part"),
								fieldWithPath("sku").description("The BWG part number of this item."),
								fieldWithPath("alternateSku").description("A alternate part number that may be used by the OEM."),
								fieldWithPath("componentType").description("The type of component."),
                                fieldWithPath("warrantyDays").description("The type of component.").optional().type(Integer.class),
                                fieldWithPath("uploadDate").description("The date this information was uploaded into the system."),
								fieldWithPath("oemId").description("The OEMS that use this part number").optional().type(List.class),
								fieldWithPath("displayName").description("Display Name. It is not saved in the Materials db table."
										+ "However the field is saved embedded saved spaTemplates.").optional().type("String"),
                                fieldWithPath("_links")
                                        .description("<<resources-oem-links,Links>> to other resources"))));
    }


	@Test
	public void findByOemIdExample() throws Exception {
		this.mockMvc.perform(get("/materials/search/findByOemId?oemId=" + "oem002"))
				.andExpect(status().isOk())
				.andDo(document("materials-findbyOemId-example",
						responseFields(
								fieldWithPath("_embedded.materials").description("An array of <<resources-material, Material resources>>"),
								fieldWithPath("_links").description("<<resources-materialslist-links,Links>> to other resources"),
								fieldWithPath("page").description("Page information"))));
	}

	@Test
	public void findByOemIdAndComponentTypeExample() throws Exception {
		this.mockMvc.perform(get("/materials/search/findByOemIdAndComponentType?oemId=" + "oem002" + "&componentType=PUMP"))
				.andExpect(status().isOk())
				.andDo(document("materials-findbyOemIdAndComponentType-example",
						responseFields(
								fieldWithPath("_embedded.materials").description("An array of <<resources-material, Material resources>>"),
								fieldWithPath("_links").description("<<resources-materialslist-links,Links>> to other resources"),
								fieldWithPath("page").description("Page information"))));
	}
}
