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
import com.mysema.util.ArrayUtils;
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

import java.time.LocalDateTime;
import java.util.*;

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
public final class ZSeedDbTest extends ModelTestBase {

    @Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

	@Before
	public void setUp() {
	}

	@Test
	public void zSeedData() throws Exception {
		clearAllData();

        String now = LocalDateTime.now().toString();


        List<Address> addresses = createAddresses(20);

        // create some oems and dealers
        Oem oem1 = createOem("Mod Spas Inc.", addresses.get(0));
        Oem oem2 = createOem("Rockers Ltd.", addresses.get(1));
        Dealer dealer1 = createDealer("Fred's Spas", addresses.get(2), oem1.get_id());
        Dealer dealer2 = createDealer("Pt. Loma Spa Outlet", addresses.get(3), oem1.get_id());
        Dealer dealer3 = createDealer("SpaStic", addresses.get(15), oem2.get_id());

        // create some users
        List<String> ownerRole = Arrays.asList("OWNER");
        List<String> salesRole = Arrays.asList("ASSOCIATE");
        List<String> techRole = Arrays.asList("TECHNICIAN");
        List<String> oemRole = Arrays.asList("OEM");
        List<String> bwgRole = Arrays.asList("BWG");
        List<String> adminRole = Arrays.asList("OWNER", "ASSOCIATE", "TECHNICIAN", "OEM", "BWG");

        User owner1 = createUser("Bonnie", "Raitt", dealer1.get_id(),oem1.get_id(), addresses.get(4), ownerRole, now);
        User owner2 = createUser("Pete", "Townsend", dealer1.get_id(),oem1.get_id(), addresses.get(4), ownerRole, now);
        User owner3 = createUser("Peter", "Gabriel", dealer1.get_id(),oem1.get_id(), addresses.get(5), ownerRole, now);
        User owner4 = createUser("Lady", "Gaga", dealer2.get_id(),oem1.get_id(), addresses.get(6), ownerRole, now);
        User owner5 = createUser("Chrissie", "Hynde", dealer3.get_id(),oem2.get_id(), addresses.get(7), ownerRole, now);

        User maker1 = createUser("Ace", "Face", null, oem1.get_id(), addresses.get(8), oemRole, now);
        User maker2 = createUser("Peter", "Fenton", null, oem1.get_id(), addresses.get(9), oemRole, now);
        User pink   = createUser("Bob", "Geldof", null, null, addresses.get(10), bwgRole, now);
        User oz     = createUser("Ozzie", "Osborn", null, null, addresses.get(11), adminRole, now);

        User sales1 = createUser("Larry", "Stooge", dealer1.get_id(),oem1.get_id(), addresses.get(12), salesRole, now);
        User sales2 = createUser("Curly", "Stooge", dealer1.get_id(),oem1.get_id(), addresses.get(12), salesRole, now);
        User sales3 = createUser("Mo", "Stooge", dealer1.get_id(),oem1.get_id(), addresses.get(12), salesRole, now);
        User tech1  = createUser("William", "Gates", dealer1.get_id(),oem1.get_id(), addresses.get(13), techRole, now);
        User tech2  = createUser("Stefan", "Jobs", dealer1.get_id(),oem1.get_id(), addresses.get(14), techRole, now);

        // create term and conditions

        // create a variety of spas.  sold, unsold, fully populated w components, some with alerts...
        Owner owner = createOwner("Blue Louis", "Lou", "Maroni");
        Spa spa = createFullSpaWithState("0blah345", "Shark", "Blue", "101", owner);


        // Create some measurements


	}


}
