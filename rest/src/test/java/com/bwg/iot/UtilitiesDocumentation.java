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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IotServicesApplication.class)
@WebAppConfiguration
public final class UtilitiesDocumentation extends ModelTestBase{

	@Rule
	public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
				.apply(documentationConfiguration(this.restDocumentation)).build();
	}


	@Test
	public void systemSettingsExample() throws Exception {
		this.mockMvc
				.perform(get("/util/systemSettings"))
				.andExpect(status().is2xxSuccessful())
				.andDo(document("system-settings-example",
						responseFields(fieldWithPath("max_file_size").description("Maximum Filesize Allowed for Attachment Uploads"),
							fieldWithPath("profiles_active").description("The mode that the system is running in (prod, qa, dev)"),
							fieldWithPath("log_file").description("The filename of the log file or iot-services application"),
							fieldWithPath("idm_domain").description("Link to the Identity Manager "
										+ "SCIM REST interface"),
							fieldWithPath("uma_metadata_url").description("Link to the Identity Manager "
								+ "UMA Metadata URL showing various configuration settings"),
							fieldWithPath("uma_aat_client_id").description("Identity Manager "
										+ "Client ID for SCIM"),
							fieldWithPath("uma_openid_keys_filename").description("File containing keys for SCIM access"),
							fieldWithPath("_links.umaMetadata").description("Link to IDM Manager UMA configuration information"),
							fieldWithPath("_links.self").description("Link to obtain User Details"))));
	}
}
