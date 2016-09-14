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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;


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
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	}

	@Test
	public void zSeedData() throws Exception {
        final String GATEWAY_1_SN = "1503071099";
        final String GATEWAY_2_SN = "1601041224";
        final String GATEWAY_3_SN = "1603141151";
        final String GATEWAY_4_SN = "1603141152";
        final String GATEWAY_5_SN = "1603141153";
        final String GATEWAY_6_SN = "1603141154";
        final String GATEWAY_7_SN = "1603141155";
        final String GATEWAY_8_SN = "1603141156";

		clearAllData();

        String db = environment.getProperty("spring.data.mongodb.database");
        if (db == null) {
            db = "test";
        }

        List<Address> addresses = createAddresses(40);

        // create attachments
        List<Attachment> attachments = createAttachments(mockMvc);
        Attachment logo1 = createLogoAttachment(mockMvc);

        // create some oems and dealers
        Oem oem1 = createOem("Sundance Spas", "103498", addresses.get(0), "oem001");
        Oem oem2 = createOemWithLogo("Maax Spas Industries Corp.", "102188", addresses.get(1), "oem002", logo1);
        Oem oem3 = createOem("Coast Spas Manufacturing Inc.", "100843", addresses.get(26), "oem003");

        Dealer dealer1 = createDealer("Sundance Spas", addresses.get(2), oem1.get_id(), "dealer001");
        Dealer dealer2 = createDealerWithLogo("Pt. Loma Spa Outlet", addresses.get(3), oem1.get_id(), "dealer002", logo1);
        Dealer dealer3 = createDealer("Valley Hot Springs Spas", addresses.get(15), oem3.get_id(), "dealer003");

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
        User oz     = createUser("user0009", "oosborn", "Ozzie", "Osborn", dealer1.get_id(), oem1.get_id(), addresses.get(11), adminRole, null);

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


        // create term and conditions
        TermsAndConditions tac1 = createTermsAndAgreement("1.0", TAC1_TEXT);
        TacUserAgreement agreement1 = createAgreement(owner1.get_id(), tac1.getVersion());
        TacUserAgreement agreement2 = createAgreement(owner2.get_id(), tac1.getVersion());

        // create set of materials
        Material t1Panel = createSpaTemplateMaterial("Panel", "6600-760");
        Material t1Controller = createSpaTemplateMaterial("Controller", "6600-761");
        Material t1Pump = createSpaTemplateMaterial("Captain's Chair", "DJAYGB-9173D");
        Material t1Gateway = createSpaTemplateMaterial("Gateway", "17092-83280-1b");
        List<Material> spaTemplate1List = Arrays.asList(t1Panel, t1Controller, t1Pump, t1Gateway);

        Material t2Panel = createSpaTemplateMaterial("Panel", "53886-02");
        Material t2Controller = createSpaTemplateMaterial("Controller", "56241");
        Material t2Pump = createSpaTemplateMaterial("Main Jets", "1023012");
        Material t2Gateway = createSpaTemplateMaterial("Gateway", "17092-83280-1a");
        List<Material> spaTemplate2List = Arrays.asList(t2Panel, t2Controller, t2Pump, t2Gateway);

        // create spaTemplates
        SpaTemplate st1 = createSpaTemplate("J-500 Luxury Collection", "J-585", "109834-1525-585", "oem001", spaTemplate1List, attachments);
        SpaTemplate st2 = createSpaTemplate("J-400 Designer Collection", "J-495", "109834-1425-495", "oem001", spaTemplate2List);
        SpaTemplate st3 = createSpaTemplate("Hot Spring Spas", "Los Coyote", "109834-1525-811", "oem002", spaTemplate2List);

        // create a variety of spas.  sold, unsold, fully populated w components, some with alerts...
        Spa spa22 = createDemoJacuzziSpa("150307", oem1.get_id(), dealer1.get_id(), owner1, "spa000022", GATEWAY_1_SN, st1.get_id(), maker1);
        createSpaRecipe(spa22.get_id(), "TGIF", "Some like it hot!");
        createSpaRecipe(spa22.get_id(), "Kids Play", "Body Temp", "98");
        createSpaEvent(spa22.get_id(), "ALERT");
        createSpaEvent(spa22.get_id(), "MEASUREMENT");
        createSpaEvent(spa22.get_id(), "NOTIFICATION");
        createSpaEvent(spa22.get_id(), "REQUEST");
        createSpaWifiStat(spa22.get_id(), WifiConnectionHealth.AVG);
        createSpaWifiStat(spa22.get_id(), WifiConnectionHealth.DISCONNECTED);
        createSpaWifiStat(spa22.get_id(), WifiConnectionHealth.STRONG);
        createSpaWifiStat(spa22.get_id(), WifiConnectionHealth.UNKONWN);
        createSpaWifiStat(spa22.get_id(), WifiConnectionHealth.WEAK);

        Spa spa24 = createDemoSpa2("160104", oem3.get_id(), dealer3.get_id(), owner2, "spa000024", GATEWAY_2_SN, st1.get_id(), sales3);
        Spa spa25 = createSmallSpaWithState("151122", "Fish", "Minnow", oem1.get_id(), dealer1.get_id(), owner3, "spa000025", GATEWAY_5_SN, st1.get_id(), sales2);
        this.add2Alerts(spa25);
        Spa spa26 = createFullSpaWithState("160229", "Shark", "Tiger", oem2.get_id(), dealer2.get_id(), owner4, "spa000026", GATEWAY_4_SN, sales3, st1.get_id(), null);
        spa26 = this.addOverheatSevereAlert(spa26);
        Spa spa27 = createDemoSpa3("160315", oem1.get_id(), dealer1.get_id(), owner5, "spa000027", GATEWAY_3_SN, st1.get_id(), sales1);

        Spa spa23 = createSmallSpaWithState("160412", "Fish", "Minnow", oem1.get_id(), dealer2.get_id(), owner6, "spa000023", GATEWAY_6_SN, st1.get_id(), maker1);
        Spa spa28 = createSmallSpaWithState("160412", "Shark", "Card", oem2.get_id(), dealer3.get_id(), owner7, "spa000028", GATEWAY_7_SN, st1.get_id(), sales4);
        Spa spa29 = createSmallSpaWithState("160412", "Shark", "Land", oem2.get_id(), dealer3.get_id(), owner8, "spa000029", GATEWAY_8_SN, st1.get_id(), sales4);

        Spa spa1  = createSmallSpaWithState("160217", "Shark", "Hammerhead", oem1.get_id(), dealer1.get_id(), null, "spa000001", st1.get_id(), null);
        Spa spa2  = createSmallSpaWithState("160217", "Shark", "Hammerhead", oem1.get_id(), dealer1.get_id(), null, "spa000002", st1.get_id(), null);
        Spa spa3  = createSmallSpaWithState("160217", "Shark", "Mako", oem1.get_id(), dealer1.get_id(), null, "spa000003", st1.get_id(), null);
        Spa spa4  = createSmallSpaWithState("151220", "Shark", "Sand", oem1.get_id(), dealer1.get_id(), null, "spa000004", st1.get_id(), null);
        Spa spa5  = createSmallSpaWithState("151220", "Shark", "Mako", oem1.get_id(), dealer1.get_id(), null, "spa000005", st1.get_id(), null);
        Spa spa6  = createSmallSpaWithState("151220", "Shark", "Sand", oem1.get_id(), dealer1.get_id(), null, "spa000006", st1.get_id(), null);
        Spa spa7  = createSmallSpaWithState("151220", "Whale", "Orca", oem1.get_id(), dealer2.get_id(), null, "spa000007", st1.get_id(), null);
        Spa spa8  = createSmallSpaWithState("160118", "Whale", "Grey", oem1.get_id(), dealer2.get_id(), null, "spa000008", st1.get_id(), null);
        Spa spa9  = createSmallSpaWithState("160118", "Shark", "Mako", oem1.get_id(), dealer1.get_id(), null, "spa000009", st1.get_id(), null);
        Spa spa10 = createSmallSpaWithState("160118", "Shark", "Sand", oem1.get_id(), dealer1.get_id(), null, "spa000010", st1.get_id(), null);
        Spa spa11 = createSmallSpaWithState("160118", "Whale", "Orca", oem1.get_id(), dealer2.get_id(), null, "spa000011", st1.get_id(), null);
        Spa spa12 = createSmallSpaWithState("160125", "Whale", "Grey", oem1.get_id(), dealer2.get_id(), null, "spa000012", st1.get_id(), null);
        Spa spa13 = createSmallSpaWithState("160101", "Whale", "Orca", oem1.get_id(), dealer2.get_id(), null, "spa000013", st1.get_id(), null);
        Spa spa14 = createSmallSpaWithState("160118", "Whale", "Grey", oem1.get_id(), dealer2.get_id(), null, "spa000014", st1.get_id(), null);
        Spa spa15 = createSmallSpaWithState("160111", "Shark", "Card", oem1.get_id(), dealer2.get_id(), null, "spa000015", st1.get_id(), null);
        Spa spa16 = createSmallSpaWithState("160111", "Whale", "Orca", oem1.get_id(), dealer2.get_id(), null, "spa000016", st1.get_id(), null);
        Spa spa17 = createSmallSpaWithState("160111", "Whale", "Grey", oem2.get_id(), dealer3.get_id(), null, "spa000017", st1.get_id(), null);
        Spa spa18 = createSmallSpaWithState("160105", "Whale", "Orca", oem2.get_id(), dealer3.get_id(), null, "spa000018", st1.get_id(), null);
        Spa spa19 = createSmallSpaWithState("160105", "Whale", "Grey", oem2.get_id(), dealer3.get_id(), null, "spa000019", st1.get_id(), null);
        Spa spa20 = createSmallSpaWithState("160105", "Whale", "Orca", oem2.get_id(), dealer3.get_id(), null, "spa000020", st1.get_id(), null);
        Spa spa21 = createSmallSpaWithState("151111", "Whale", "Grey", oem2.get_id(), dealer3.get_id(), null, "spa000021", st1.get_id(), null);

	}

    private static final String TAC1_TEXT =
            "We the People, in Order to form a more perfect Union, establish Justice, insure domestic Tranquility, provide for the common defence,[note 1] promote the general Welfare, and secure the Blessings of Liberty to ourselves and our Posterity, do ordain and establish these Terms an Agreements. And agree to donate all of our assets to Balboa Water Group upon acceptance of this aggrement.";

}
