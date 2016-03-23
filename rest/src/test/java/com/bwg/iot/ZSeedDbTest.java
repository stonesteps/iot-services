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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;


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

        User owner1 = createUser("braitt", "Bonnie", "Raitt", dealer1.get_id(),oem1.get_id(), addresses.get(4), ownerRole);
        User owner2 = createUser("ptownsend", "Pete", "Townsend", dealer1.get_id(),oem1.get_id(), addresses.get(4), ownerRole);
        User owner3 = createUser("pgabriel", "Peter", "Gabriel", dealer1.get_id(),oem1.get_id(), addresses.get(5), ownerRole);
        User owner4 = createUser("lgaga", "Lady", "Gaga", dealer2.get_id(),oem1.get_id(), addresses.get(6), ownerRole);
        User owner5 = createUser("chynde", "Chrissie", "Hynde", dealer3.get_id(),oem2.get_id(), addresses.get(7), ownerRole);

        User maker1 = createUser("face", "Ace", "Face", null, oem1.get_id(), addresses.get(8), oemRole);
        User maker2 = createUser("pfenton", "Peter", "Fenton", null, oem1.get_id(), addresses.get(9), oemRole);
        User pink   = createUser("bgeldof", "Bob", "Geldof", null, null, addresses.get(10), bwgRole);
        User oz     = createUser("oosborn", "Ozzie", "Osborn", null, null, addresses.get(11), adminRole);

        User sales1 = createUser("lstooge", "Larry", "Stooge", dealer1.get_id(),oem1.get_id(), addresses.get(12), salesRole);
        User sales2 = createUser("cstooge", "Curly", "Stooge", dealer1.get_id(),oem1.get_id(), addresses.get(12), salesRole);
        User sales3 = createUser("mstooge", "Mo", "Stooge", dealer1.get_id(),oem1.get_id(), addresses.get(12), salesRole);
        User tech1  = createUser("wgates", "William", "Gates", dealer1.get_id(),oem1.get_id(), addresses.get(13), techRole);
        User tech2  = createUser("sjobs", "Stefan", "Jobs", dealer1.get_id(),oem1.get_id(), addresses.get(14), techRole);

        // create term and conditions
        TermsAndConditions tac1 = createTermsAndAgreement("1.0", TAC1_TEXT);
        TacUserAgreement agreement1 = createAgreement(owner1.get_id(), tac1.getVersion());
        TacUserAgreement agreement2 = createAgreement(owner2.get_id(), tac1.getVersion());

        // create a variety of spas.  sold, unsold, fully populated w components, some with alerts...
        Spa spa22 = createFullSpaWithState("150307", "Shark", "Blue", dealer1.get_id(), owner1);
        spa22 = this.addOverheatRedAlert(spa22);
        Spa spa24 = createSmallSpaWithState("160104", "Shark", "Tiger", dealer1.get_id(), owner2);
        this.addLowFlowYellowAlert(spa24);
        Spa spa25 = createSmallSpaWithState("151122", "Fish", "Minnow", dealer1.get_id(), owner3);
        this.add2Alerts(spa25);
        Spa spa26 = createSmallSpaWithState("160229", "Shark", "Tiger", dealer2.get_id(), owner4);
        Spa spa27 = createDemoSpa("160315", "Whale", "Beluga", dealer3.get_id(), owner5);

        Spa spa1  = createUnsoldSpa("160217", "Shark", "Hammerhead", dealer1.get_id());
        Spa spa2  = createUnsoldSpa("160217", "Shark", "Hammerhead", dealer1.get_id());
        Spa spa3  = createUnsoldSpa("160217", "Shark", "Mako", dealer1.get_id());
        Spa spa4  = createUnsoldSpa("151220", "Shark", "Sand", dealer1.get_id());
        Spa spa5  = createUnsoldSpa("151220", "Shark", "Mako", dealer1.get_id());
        Spa spa6  = createUnsoldSpa("151220", "Shark", "Sand", dealer1.get_id());
        Spa spa7  = createUnsoldSpa("151220", "Whale", "Orca", dealer2.get_id());
        Spa spa8  = createUnsoldSpa("160118", "Whale", "Grey", dealer2.get_id());
        Spa spa9  = createUnsoldSpa("160118", "Shark", "Mako", dealer1.get_id());
        Spa spa10 = createUnsoldSpa("160118", "Shark", "Sand", dealer1.get_id());
        Spa spa11 = createUnsoldSpa("160118", "Whale", "Orca", dealer2.get_id());
        Spa spa12 = createUnsoldSpa("160125", "Whale", "Grey", dealer2.get_id());
        Spa spa13 = createUnsoldSpa("160101", "Whale", "Orca", dealer2.get_id());
        Spa spa14 = createUnsoldSpa("160118", "Whale", "Grey", dealer2.get_id());
        Spa spa15 = createUnsoldSpa("160111", "Shark", "Card", dealer2.get_id());
        Spa spa16 = createUnsoldSpa("160111", "Whale", "Orca", dealer2.get_id());
        Spa spa17 = createUnsoldSpa("160111", "Whale", "Grey", dealer3.get_id());
        Spa spa18 = createUnsoldSpa("160105", "Whale", "Orca", dealer3.get_id());
        Spa spa19 = createUnsoldSpa("160105", "Whale", "Grey", dealer3.get_id());
        Spa spa20 = createUnsoldSpa("160105", "Whale", "Orca", dealer3.get_id());
        Spa spa21 = createUnsoldSpa("151111", "Whale", "Grey", dealer3.get_id());


        // Create some measurements


	}

    private static final String TAC1_TEXT =
            "We the People, in Order to form a more perfect Union, establish Justice, insure domestic Tranquility, provide for the common defence,[note 1] promote the general Welfare, and secure the Blessings of Liberty to ourselves and our Posterity, do ordain and establish these Terms an Agreements. And agree to donate all of our assets to Balboa Water Group upon acceptance of this aggrement.";

}
