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
        Spa spa1  = createUnsoldSpa("111111", "Shark", "Hammerhead", dealer1.get_id());
        Spa spa2  = createUnsoldSpa("222222", "Shark", "Hammerhead", dealer1.get_id());
        Spa spa3  = createUnsoldSpa("33333", "Shark", "Mako", dealer1.get_id());
        Spa spa4  = createUnsoldSpa("44444", "Shark", "Sand", dealer1.get_id());
        Spa spa5  = createUnsoldSpa("55555", "Shark", "Mako", dealer1.get_id());
        Spa spa6  = createUnsoldSpa("66666", "Shark", "Sand", dealer1.get_id());
        Spa spa7  = createUnsoldSpa("seven7:", "Whale", "Humpback", dealer2.get_id());
        Spa spa8  = createUnsoldSpa("88-", "Whale", "Sperm", dealer2.get_id());
        Spa spa9  = createUnsoldSpa("99999", "Shark", "Mako", dealer1.get_id());
        Spa spa10 = createUnsoldSpa("10101", "Shark", "Sand", dealer1.get_id());
        Spa spa11 = createUnsoldSpa("11_11_11", "Whale", "Humpback", dealer2.get_id());
        Spa spa12 = createUnsoldSpa("121212", "Whale", "Sperm", dealer2.get_id());
        Spa spa13 = createUnsoldSpa("1313", "Whale", "Humpback", dealer2.get_id());
        Spa spa14 = createUnsoldSpa("14141414", "Whale", "Sperm", dealer2.get_id());
        Spa spa15 = createUnsoldSpa("fifteen::", "Shark", "Card", dealer2.get_id());
        Spa spa16 = createUnsoldSpa("161616", "Whale", "Humpback", dealer2.get_id());
        Spa spa17 = createUnsoldSpa("177777", "Whale", "Sperm", dealer3.get_id());
        Spa spa18 = createUnsoldSpa("18181818", "Whale", "Humpback", dealer3.get_id());
        Spa spa19 = createUnsoldSpa("191919999", "Whale", "Sperm", dealer3.get_id());
        Spa spa20 = createUnsoldSpa("20twenty:", "Whale", "Humpback", dealer3.get_id());
        Spa spa21 = createUnsoldSpa("21_", "Whale", "Sperm", dealer3.get_id());

        Spa spa22 = createFullSpaWithState("001:", "Shark", "Blue", dealer1.get_id(), owner1);
        Spa spa24 = createSmallSpaWithState("002-", "Shark", "Tiger", dealer1.get_id(), owner2);
        Spa spa25 = createSmallSpaWithState("003-", "Fish", "Minnow", dealer1.get_id(), owner3);
        Spa spa26 = createSmallSpaWithState("004-", "Shark", "Tiger", dealer2.get_id(), owner4);
        Spa spa27 = createFullSpaWithState("005-", "Whale", "Sperm", dealer3.get_id(), owner5);


        // Create some measurements


	}


}