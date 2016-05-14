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
        final String GATEWAY_1_SN = "1503071099";
        final String GATEWAY_2_SN = "1601041224";
        final String GATEWAY_3_SN = "1603141151";

		clearAllData();

        List<Address> addresses = createAddresses(40);

        // create some oems and dealers
        Oem oem1 = createOem("Jacuzzi Europe Spa - Italy.", addresses.get(0), "oem001");
        Oem oem2 = createOem("Rockers Ltd.", addresses.get(1), "oem002");
        Dealer dealer1 = createDealer("Jacuzzi Europe Spa - Italy", addresses.get(2), oem1.get_id(), "oem001");
        Dealer dealer2 = createDealer("Pt. Loma Spa Outlet", addresses.get(3), oem1.get_id(), "dealer002");
        Dealer dealer3 = createDealer("SpaStic", addresses.get(15), oem2.get_id(), "dealer003");

        // create some users
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

        User owner1 = createUser("user0001", "braitt", "Bonnie", "Raitt", dealer1.get_id(), oem1.get_id(), addresses.get(4), ownerRole);
        User owner2 = createUser("user0002", "ptownsend", "Pete", "Townsend", dealer1.get_id(), oem1.get_id(), addresses.get(4), ownerRole);
        User owner3 = createUser("user0003", "pgabriel", "Peter", "Gabriel", dealer1.get_id(), oem1.get_id(), addresses.get(5), ownerRole);
        User owner4 = createUser("user0004", "lgaga", "Lady", "Gaga", dealer2.get_id(), oem2.get_id(), addresses.get(6), ownerRole);
        User owner5 = createUser("user0005", "chynde", "Chrissie", "Hynde", dealer1.get_id(), oem1.get_id(), addresses.get(7), ownerRole);
        User owner6 = createUser("user0025", "jcroce", "Jim", "Croce", dealer2.get_id(), oem1.get_id(), addresses.get(26), ownerRole);
        User owner7 = createUser("user0026", "mmathers", "Marshall", "Mathers", dealer3.get_id(), oem2.get_id(), addresses.get(27), ownerRole);
        User owner8 = createUser("user0027", "sdogg", "Calvin", "Broadus", dealer3.get_id(), oem2.get_id(), addresses.get(28), ownerRole);

        User maker1 = createUser("user0006", "jpage", "Jimmy", "Page", dealer1.get_id(), oem1.get_id(), addresses.get(8), oemDealerRole);
        User maker2 = createUser("user0007", "pfenton", "Peter", "Fenton", null, oem2.get_id(), addresses.get(9), oemRole);
        User pink   = createUser("user0008", "bgeldof", "Bob", "Geldof", null, null, addresses.get(10), bwgRole);
        User oz     = createUser("user0009", "oosborn", "Ozzie", "Osborn", null, null, addresses.get(11), adminRole);

        User sales1 = createUser("user0010", "nfinn", "Neil", "Finn", dealer1.get_id(), oem1.get_id(), addresses.get(12), salesRole);
        User sales2 = createUser("user0011", "bpreston", "Billy", "Preston", dealer1.get_id(), oem1.get_id(), addresses.get(12), salesRole);
        User sales3 = createUser("user0012", "cclemons", "Clarence", "Clemons", dealer2.get_id(), oem1.get_id(), addresses.get(12), salesRole);
        User sales4 = createUser("user0025", "rwaters", "Roger", "Waters", dealer3.get_id(), oem2.get_id(), addresses.get(28), salesRole);
        User tech1  = createUser("user0013", "wgates", "William", "Gates", dealer1.get_id(), oem1.get_id(), addresses.get(13), techRole);
        User tech2  = createUser("user0014", "sjobs", "Stefan", "Jobs", dealer1.get_id(), oem1.get_id(), addresses.get(14), techRole);

        User dealer1Admin   = createUser("user0015", "tpetty", "Tom", "Petty", dealer1.get_id(), null, addresses.get(16), dealerAdminRole);
        User dealer2Admin   = createUser("user0016", "jbrown", "James", "Brown", dealer2.get_id(), null, addresses.get(17), dealerAdminRole);
        User dealer3Admin   = createUser("user0017", "ptosh", "Peter", "Tosh", dealer3.get_id(), null, addresses.get(18), dealerAdminRole);
        User oem1Admin   = createUser("user0018", "smorse", "Steve", "Morse", null, oem1.get_id(), addresses.get(19), oemAdminRole);
        User oem2Admin   = createUser("user0019", "jmitchell", "Joni", "Mitchell", null, oem2.get_id(), addresses.get(20), oemAdminRole);
        User bwgAdmin   = createUser("user0020", "blondie", "Debbi", "Harry", null, null, addresses.get(21), bwgAdminRole);

        User salesD2 = createUser("user0021", "psimon", "Paul", "Simon", dealer2.get_id(), oem1.get_id(), addresses.get(22), salesRole);
        User salesD3 = createUser("user0021", "mwaters", "Muddy", "Waters", dealer3.get_id(), oem2.get_id(), addresses.get(23), salesRole);
        User techD2  = createUser("user0023", "csagan", "Carl", "Sagan", dealer2.get_id(), oem1.get_id(), addresses.get(24), techRole);
        User techD3  = createUser("user0024", "shawking", "Stephen", "Hawking", dealer3.get_id(), oem2.get_id(), addresses.get(25), techRole);


        // create term and conditions
        TermsAndConditions tac1 = createTermsAndAgreement("1.0", TAC1_TEXT);
        TacUserAgreement agreement1 = createAgreement(owner1.get_id(), tac1.getVersion());
        TacUserAgreement agreement2 = createAgreement(owner2.get_id(), tac1.getVersion());

        // create set of materials
        List<Material> materialList = setupSeveralTestMaterials(oem1, oem2);
        List<Material> spaTemplate1List = Arrays.asList(materialList.get(2), materialList.get(8));
        List<Material> spaTemplate2List = Arrays.asList(materialList.get(3), materialList.get(7));

        // create spaTemplates
        SpaTemplate st1 = createSpaTemplate("J-500 Luxury Collection", "J-585", "109834-1525-585", "oem001", spaTemplate1List);
        SpaTemplate st2 = createSpaTemplate("J-400 Designer Collection", "J-495", "109834-1425-495", "oem001", spaTemplate2List);
        SpaTemplate st3 = createSpaTemplate("Hot Spring Spas", "Los Coyote", "109834-1525-811", "oem002", spaTemplate2List);

        // create a variety of spas.  sold, unsold, fully populated w components, some with alerts...
        Spa spa22 = createDemoSpa1("150307", oem1.get_id(), dealer1.get_id(), owner1, "spa000022", GATEWAY_1_SN, maker1);
        Spa spa24 = createDemoSpa2("160104", oem1.get_id(), dealer1.get_id(), owner2, "spa000024", GATEWAY_2_SN, sales1);
        Spa spa25 = createSmallSpaWithState("151122", "Fish", "Minnow", oem1.get_id(), dealer1.get_id(), owner3, "spa000025", sales2);
        this.add2Alerts(spa25);
        Spa spa26 = createFullSpaWithState("160229", "Shark", "Tiger", oem2.get_id(), dealer2.get_id(), owner4, "spa000026", sales3);
        spa26 = this.addOverheatRedAlert(spa26);
        Spa spa27 = createDemoSpa3("160315", oem1.get_id(), dealer1.get_id(), owner5, "spa000027", GATEWAY_3_SN, sales1);

        Spa spa23 = createSmallSpaWithState("160412", "Fish", "Minnow", oem1.get_id(), dealer2.get_id(), owner6, "spa000023", maker1);
        Spa spa28 = createSmallSpaWithState("160412", "Shark", "Card", oem2.get_id(), dealer3.get_id(), owner7, "spa000028", sales4);
        Spa spa29 = createSmallSpaWithState("160412", "Shark", "Land", oem2.get_id(), dealer3.get_id(), owner8, "spa000029", sales4);

        Spa spa1  = createSmallSpaWithState("160217", "Shark", "Hammerhead", oem1.get_id(), dealer1.get_id(), null, "spa000001");
        Spa spa2  = createSmallSpaWithState("160217", "Shark", "Hammerhead", oem1.get_id(), dealer1.get_id(), null, "spa000002");
        Spa spa3  = createSmallSpaWithState("160217", "Shark", "Mako", oem1.get_id(), dealer1.get_id(), null, "spa000003");
        Spa spa4  = createSmallSpaWithState("151220", "Shark", "Sand", oem1.get_id(), dealer1.get_id(), null, "spa000004");
        Spa spa5  = createSmallSpaWithState("151220", "Shark", "Mako", oem1.get_id(), dealer1.get_id(), null, "spa000005");
        Spa spa6  = createSmallSpaWithState("151220", "Shark", "Sand", oem1.get_id(), dealer1.get_id(), null, "spa000006");
        Spa spa7  = createSmallSpaWithState("151220", "Whale", "Orca", oem1.get_id(), dealer2.get_id(), null, "spa000007");
        Spa spa8  = createSmallSpaWithState("160118", "Whale", "Grey", oem1.get_id(), dealer2.get_id(), null, "spa000008");
        Spa spa9  = createSmallSpaWithState("160118", "Shark", "Mako", oem1.get_id(), dealer1.get_id(), null, "spa000009");
        Spa spa10 = createSmallSpaWithState("160118", "Shark", "Sand", oem1.get_id(), dealer1.get_id(), null, "spa000010");
        Spa spa11 = createSmallSpaWithState("160118", "Whale", "Orca", oem1.get_id(), dealer2.get_id(), null, "spa000011");
        Spa spa12 = createSmallSpaWithState("160125", "Whale", "Grey", oem1.get_id(), dealer2.get_id(), null, "spa000012");
        Spa spa13 = createSmallSpaWithState("160101", "Whale", "Orca", oem1.get_id(), dealer2.get_id(), null, "spa000013");
        Spa spa14 = createSmallSpaWithState("160118", "Whale", "Grey", oem1.get_id(), dealer2.get_id(), null, "spa000014");
        Spa spa15 = createSmallSpaWithState("160111", "Shark", "Card", oem1.get_id(), dealer2.get_id(), null, "spa000015");
        Spa spa16 = createSmallSpaWithState("160111", "Whale", "Orca", oem1.get_id(), dealer2.get_id(), null, "spa000016");
        Spa spa17 = createSmallSpaWithState("160111", "Whale", "Grey", oem2.get_id(), dealer3.get_id(), null, "spa000017");
        Spa spa18 = createSmallSpaWithState("160105", "Whale", "Orca", oem2.get_id(), dealer3.get_id(), null, "spa000018");
        Spa spa19 = createSmallSpaWithState("160105", "Whale", "Grey", oem2.get_id(), dealer3.get_id(), null, "spa000019");
        Spa spa20 = createSmallSpaWithState("160105", "Whale", "Orca", oem2.get_id(), dealer3.get_id(), null, "spa000020");
        Spa spa21 = createSmallSpaWithState("151111", "Whale", "Grey", oem2.get_id(), dealer3.get_id(), null, "spa000021");

	}

    private static final String TAC1_TEXT =
            "We the People, in Order to form a more perfect Union, establish Justice, insure domestic Tranquility, provide for the common defence,[note 1] promote the general Welfare, and secure the Blessings of Liberty to ourselves and our Posterity, do ordain and establish these Terms an Agreements. And agree to donate all of our assets to Balboa Water Group upon acceptance of this aggrement.";

}
