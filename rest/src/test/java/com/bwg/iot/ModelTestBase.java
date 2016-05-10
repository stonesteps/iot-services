package com.bwg.iot;

import com.bwg.iot.builders.SpaStateBuilder;
import com.bwg.iot.model.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by triton on 2/22/16.
 */
public class ModelTestBase {
    @Autowired
    protected SpaRepository spaRepository;

    @Autowired
    protected AddressRepository addressRepository;

    @Autowired
    protected DealerRepository dealerRepository;

    @Autowired
    protected OemRepository oemRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected ComponentRepository componentRepository;

    @Autowired
    protected MaterialRepository materialRepository;

    @Autowired
    protected AlertRepository alertRepository;

    @Autowired
    protected TermsAndConditionsRepository termsAndConditionsRepository;

    @Autowired
    protected TacUserAgreementRepository tacUserAgreementRepository;

    @Autowired
    protected FaultLogRepository faultLogRepository;

    @Autowired
    protected FaultLogDescriptionRepository faultLogDescriptionRepository;

    @Autowired
    protected SpaTemplateRepository spaTemplateRepository;

    protected static int serialSuffix = 1001;

    protected void clearAllData() {
        this.addressRepository.deleteAll();
        this.dealerRepository.deleteAll();
        this.oemRepository.deleteAll();
        this.spaRepository.deleteAll();
        this.componentRepository.deleteAll();
        this.materialRepository.deleteAll();
        this.userRepository.deleteAll();
        this.alertRepository.deleteAll();
        this.tacUserAgreementRepository.deleteAll();
        this.termsAndConditionsRepository.deleteAll();
        this.spaTemplateRepository.deleteAll();
    }

    protected Address createAddress() {
        Address address = new Address();
        address.setAddress1("30");
        address.setAddress2("Honey Apple Crest");
        address.setCity("Village Five");
        address.setState("CA");
        address.setZip("94113");
        address.setCountry("US");
        return addressRepository.save(address);
    }

    protected Address createAddress(int i) {
        Address address = new Address();
        address.setAddress1("30" + i + " " + i + "35th Ave");
        address.setAddress2("Suite 10" + i);
        address.setCity("San Diego");
        address.setState("CA");
        address.setZip("92101");
        address.setCountry("US");
        return addressRepository.save(address);
    }

    protected List<Address> createAddresses(int count) {
        List<Address> addresses = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            addresses.add(createAddress(i));
        }
        return addresses;
    }

    protected Dealer createDealer(String name, Address address, String oemId) {
        return createDealer(name, address, oemId, null);
    }

    protected Dealer createDealer(String name, Address address, String oemId, String myId) {
        Dealer dealer = new Dealer();
        if (StringUtils.isNotEmpty(myId)) {
            dealer.set_id(myId);
        }
        dealer.setName(name);
        dealer.setAddress(address);
        dealer.setOemId(oemId);
        return dealerRepository.save(dealer);
    }

    protected Oem createOem(String name, Address address, String id) {
        Oem oem = new Oem();
        if (StringUtils.isNotEmpty(id)) {
            oem.set_id(id);
        }
        oem.setName(name);
        oem.setAddress(address);
        return oemRepository.save(oem);
    }

    protected User createUser(String username, String firstName, String lastName, String dealerId, String oemId, Address address, List<String> roles) {
        return createUser(null, username, firstName, lastName, dealerId, oemId, address, roles);
    }

    protected User createUser(String id, String username, String firstName, String lastName, String dealerId, String oemId, Address address, List<String> roles) {
        User user = new User();
        if (StringUtils.isNotEmpty(id)) {
            user.set_id(id);
        }
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setDealerId(dealerId);
        user.setOemId(oemId);
        user.setAddress(address);
        user.setRoles(roles);
        user.setEmail(username + "@riot.com");
        user.setPhone("(800) 471-2382");
        user.setCreatedDate(new Date());
        userRepository.save(user);
        return user;
    }

    public final static String PUMP1_BRAND_NAME = "Ultimax (UM)";
    public final static String PUMP1_DESCRIPTION = "SPA PUMP 3HP 230V 2SPD 53IN CORD";
    public final static String PUMP1_SKU = "DJAYHB-0151G";
    public final static String PUMP1_ALT_SKU = "50008200";
    public final static int ONE_YEAR_WARRANTY_DAYS = 366;
    public final static int THREE_YEAR_WARRANTY_DAYS = 1096;

    public final static String PUMP2_BRAND_NAME = "Dura Jet (DJ)";
    public final static String PUMP2_DESCRIPTION = "DJ AO 1.5HP 2S 43FT A2MB LV BU";
    public final static String PUMP2_SKU = "DJAYFA-01A3";
    public final static String PUMP2_ALT_SKU = "101293";

    public final static String PUMP3_BRAND_NAME = "(Undetermined)";
    public final static String PUMP3_DESCRIPTION = "UM EME 12.8A 1S 230V 50Hz 120\" AMP";
    public final static String PUMP3_SKU = "1023120-NHP";

    public final static String PUMP4_BRAND_NAME = "(Undetermined)";
    public final static String PUMP4_DESCRIPTION = "UM EME 10.3/1.8A 2S 230V 50Hz 120\" AMP";
    public final static String PUMP4_SKU = "1023110-NHP";
    public final static String PUMP4_ALT_SKU = "X320535";

    public final static String PUMP5_BRAND_NAME = "Niagra";
    public final static String PUMP5_DESCRIPTION = "Niagara spa pump 2HP 2S amp 230v(1)";
    public final static String PUMP5_SKU = "1023322";

    public final static String CONTROLLER1_BRAND_NAME = "TP Series";
    public final static String CONTROLLER1_DESCRIPTION = "PANEL BALBOA TP600C NO O/L";
    public final static String CONTROLLER1_SKU = "50305-01";

    public final static String CONTROLLER2_BRAND_NAME = "Bull Frog 600";
    public final static String CONTROLLER2_DESCRIPTION = "PANEL BULFRG BFTP600";
    public final static String CONTROLLER2_SKU = "50285-02";

    public final static String CONTROLLER3_BRAND_NAME = "Bull Frog 900";
    public final static String CONTROLLER3_DESCRIPTION = "PANEL BULFRG BFTP900";
    public final static String CONTROLLER3_SKU = "50284-07";

    public final static String CONTROLLER4_BRAND_NAME = "Spa Touch";
    public final static String CONTROLLER4_DESCRIPTION = "PANEL BULFRG BFTP600U";
    public final static String CONTROLLER4_SKU = "50285-05";


    protected void setupTestMaterials() {
        List<Address> addresses = createAddresses(2);
        Oem oem1 = createOem("Blue Wave Spas, LTD", addresses.get(0), "oem101");
        Oem oem2 = createOem("Jazzi Pool & Spa Products, LTD", addresses.get(1), "oem102");
        setupTestMaterials(oem1, oem2);
    }

    protected List<Material> setupTestMaterials(Oem oem1, Oem oem2){
        List<String> bothOems = Arrays.asList(oem1.get_id(), oem2.get_id());
        List<String> justOne = Arrays.asList(oem1.get_id());
        List<String> justTwo = Arrays.asList(oem2.get_id());

        Material material1 = createMaterial(Component.ComponentType.PUMP.name(), PUMP1_BRAND_NAME, PUMP1_DESCRIPTION, PUMP1_SKU, PUMP1_ALT_SKU, ONE_YEAR_WARRANTY_DAYS, bothOems);
        Material material2 = createMaterial(Component.ComponentType.PUMP.name(), PUMP2_BRAND_NAME, PUMP2_DESCRIPTION, PUMP2_SKU, PUMP2_ALT_SKU, ONE_YEAR_WARRANTY_DAYS, bothOems);
        Material material3 = createMaterial(Component.ComponentType.PUMP.name(), PUMP3_BRAND_NAME, PUMP3_DESCRIPTION, PUMP3_SKU, null, ONE_YEAR_WARRANTY_DAYS, bothOems);
        Material material4 = createMaterial(Component.ComponentType.PUMP.name(), PUMP4_BRAND_NAME, PUMP4_DESCRIPTION, PUMP4_SKU, PUMP4_ALT_SKU, ONE_YEAR_WARRANTY_DAYS, justOne);
        Material material5 = createMaterial(Component.ComponentType.PUMP.name(), PUMP5_BRAND_NAME, PUMP5_DESCRIPTION, PUMP5_SKU, null, 0, justTwo);

        Material material6 = createMaterial(Component.ComponentType.CONTROLLER.name(), CONTROLLER1_BRAND_NAME, CONTROLLER1_DESCRIPTION, CONTROLLER1_SKU, null, THREE_YEAR_WARRANTY_DAYS, bothOems);
        Material material7 = createMaterial(Component.ComponentType.CONTROLLER.name(), CONTROLLER2_BRAND_NAME, CONTROLLER2_DESCRIPTION, CONTROLLER2_SKU, null, THREE_YEAR_WARRANTY_DAYS, justOne);
        Material material8 = createMaterial(Component.ComponentType.CONTROLLER.name(), CONTROLLER3_BRAND_NAME, CONTROLLER3_DESCRIPTION, CONTROLLER3_SKU, null, THREE_YEAR_WARRANTY_DAYS, justOne);
        Material material9 = createMaterial(Component.ComponentType.CONTROLLER.name(), CONTROLLER4_BRAND_NAME, CONTROLLER4_DESCRIPTION, CONTROLLER4_SKU, null, THREE_YEAR_WARRANTY_DAYS, justOne);
        List<Material> materialList = Arrays.asList(material1, material2, material3, material4, material5, material6, material7, material8, material9);
        return materialList;
    }

    protected Material createMaterial(String type, String brandName, String description, String sku, String alternateSku, int warrantyDays, List<String> oemIds){
        Material material = new Material();
        material.setComponentType(type);
        material.setBrandName(brandName);
        material.setDescription(description);
        material.setSku(sku);
        material.setAlternateSku(alternateSku);
        material.setWarrantyDays(warrantyDays);
        material.setUploadDate(new Date());
        material.setOemId(oemIds);
        materialRepository.save(material);
        return material;
    }


    protected Component createComponent(String type, String port, String name, String serialNumber, String spaId) {
        return createFixedComponent(type, port, name, serialNumber + (serialSuffix++), spaId);
    }

    protected Component createFixedComponent(String type, String port, String name, String serialNumber, String spaId) {
        Component component = new Component();
        component.setComponentType(type);
        component.setSerialNumber(serialNumber);
        component.setPort(port);
        component.setSpaId(spaId);
        component.setName(name);

        componentRepository.save(component);
        return component;
    }

    protected ComponentState createComponentState(Component component, String value) {
        ComponentState cs = new ComponentState();
        cs.setComponentType(component.getComponentType());
        cs.setPort(component.getPort());
        cs.setSerialNumber(component.getSerialNumber());
        cs.setName(component.getName());
        cs.setValue(value);
        cs.setTargetValue(value);

        List<String> availableStates = null;
        String type = component.getComponentType();
        if (Component.ComponentType.MICROSILK.toString().equalsIgnoreCase(type)
                || Component.ComponentType.AUX.toString().equalsIgnoreCase(type)
                || Component.ComponentType.MISTER.toString().equalsIgnoreCase(type)
                || Component.ComponentType.OZONE.toString().equalsIgnoreCase(type)) {
            availableStates = Arrays.asList("OFF", "ON");
        } else if (Component.ComponentType.PUMP.toString().equalsIgnoreCase(type)) {
            if ("0".equalsIgnoreCase(component.getPort())) {
                availableStates = Arrays.asList("OFF", "LOW", "HIGH");
            } else {
                availableStates = Arrays.asList("OFF", "HIGH");
            }
        } else if (Component.ComponentType.LIGHT.toString().equalsIgnoreCase(type)) {
            if ("0".equalsIgnoreCase(component.getPort())) {
                availableStates = Arrays.asList("OFF", "LOW", "MED", "HIGH");
            } else if ("1".equalsIgnoreCase(component.getPort())) {
                availableStates = Arrays.asList("OFF", "LOW", "HIGH");
            } else {
                availableStates = Arrays.asList("OFF", "HIGH");
            }
        } else if (Component.ComponentType.BLOWER.toString().equalsIgnoreCase(type)) {
            availableStates = Arrays.asList("OFF", "LOW", "MED", "HIGH");
        }
        cs.setAvailableValues(availableStates);
        return cs;
    }

    protected Spa createUnsoldSpa(String serialNumber, String productName, String model, String oemId, String dealerId) {
        return createUnsoldSpa(serialNumber, productName, model, oemId, dealerId, null);
    }

    protected Spa createUnsoldSpa(String serialNumber, String productName, String model, String oemId, String dealerId, String spaId) {
        Spa spa = new Spa();
        spa.setSerialNumber(serialNumber + (serialSuffix++));
        spa.setProductName(productName);
        spa.setModel(model);
        spa.setDealerId(dealerId);
        spa.setOemId(oemId);
        this.spaRepository.save(spa);
        return spa;
    }

    protected Spa createFullSpaWithState(String serialNumber, String productName, String model, String oemId, String dealerId, User owner) {
        return createFullSpaWithState(serialNumber, productName, model, oemId, dealerId, owner, null);
    }

    protected Spa createFullSpaWithState(String serialNumber, String productName, String model, String oemId, String dealerId, User owner, String spaId) {
        Spa spa = new Spa();
        if (StringUtils.isNotEmpty(spaId)) {
            spa.set_id(spaId);
        }
        spa.setSerialNumber(serialNumber + (serialSuffix++));
        spa.setProductName(productName);
        spa.setModel(model);
        spa.setDealerId(dealerId);
        spa.setOwner(owner);
        spa.setOemId(oemId);
        spa.setManufacturedDate(new Date());
        spa.setRegistrationDate(new Date());
        spa.setP2pAPSSID("myWifi");
        spa.setP2pAPPassword("*******");
        spa = this.spaRepository.save(spa);

        Component gateway = createComponent(Component.ComponentType.GATEWAY.name(), "0", "IoT Gateway", serialNumber, spa.get_id());
        gateway.setRegistrationDate(new Date());
        componentRepository.save(gateway);

        Component mote1 = createComponent(Component.ComponentType.MOTE.name(), "0", "Pump #1 Measurement", serialNumber, spa.get_id());
        gateway.setRegistrationDate(new Date());
        componentRepository.save(mote1);

        Component mote2 = createComponent(Component.ComponentType.MOTE.name(), "1", "Pump #1 Measurement", serialNumber, spa.get_id());
        gateway.setRegistrationDate(new Date());
        componentRepository.save(mote2);

        String pumpPartNo1 = "1016205*";
        String pumpPartNo2 = "1042118*";
        Component pump1 = createComponent(Component.ComponentType.PUMP.name(), "0", "Circulation", pumpPartNo1 + serialNumber, spa.get_id());
        Component pump2 = createComponent(Component.ComponentType.PUMP.name(), "1", "Massage Jets", pumpPartNo1 + serialNumber, spa.get_id());
        Component pump3 = createComponent(Component.ComponentType.PUMP.name(), "2", "Captain's Chair", pumpPartNo1 + serialNumber, spa.get_id());
        Component pump4 = createComponent(Component.ComponentType.PUMP.name(), "3", "Foot Massage", pumpPartNo2 + serialNumber, spa.get_id());
        Component pump5 = createComponent(Component.ComponentType.PUMP.name(), "4", "Side Jets", pumpPartNo2 + serialNumber, spa.get_id());
        Component pump6 = createComponent(Component.ComponentType.PUMP.name(), "5", "2nd Chair", pumpPartNo2 + serialNumber, spa.get_id());
        Component blower1 = createComponent(Component.ComponentType.BLOWER.name(), "0", "Bubbles", serialNumber, spa.get_id());
        Component blower2 = createComponent(Component.ComponentType.BLOWER.name(), "1", "Captains Bubbles", serialNumber, spa.get_id());
        Component mister1 = createComponent(Component.ComponentType.MISTER.name(), "0", "Mister", serialNumber, spa.get_id());
        Component light1 = createComponent(Component.ComponentType.LIGHT.name(), "0", "Main Light", serialNumber, spa.get_id());
        Component light2 = createComponent(Component.ComponentType.LIGHT.name(), "1", "Captain's Blue", serialNumber, spa.get_id());
        Component light3 = createComponent(Component.ComponentType.LIGHT.name(), "2", "Red Light", serialNumber, spa.get_id());
        Component light4 = createComponent(Component.ComponentType.LIGHT.name(), "3", "Black Light", serialNumber, spa.get_id());
        Component light5 = createComponent(Component.ComponentType.LIGHT.name(), "4", "Disco Ball", serialNumber, spa.get_id());
        Component ozone1 = createComponent(Component.ComponentType.OZONE.name(), "0", "Ozone", serialNumber, spa.get_id());
        Component filter1 = createComponent(Component.ComponentType.FILTER.name(), "0", "Primary Filter", serialNumber, spa.get_id());
        Component filter2 = createComponent(Component.ComponentType.FILTER.name(), "1", "Seconday Filter", serialNumber, spa.get_id());
        Component aux1 = createComponent(Component.ComponentType.AUX.name(), "0", "Stereo", serialNumber, spa.get_id());
        Component aux2 = createComponent(Component.ComponentType.AUX.name(), "1", "Waterfall", serialNumber, spa.get_id());
        Component aux3 = createComponent(Component.ComponentType.AUX.name(), "2", "Aux. #3", serialNumber, spa.get_id());
        Component microsilk = createComponent(Component.ComponentType.MICROSILK.name(), "0", "Microsilk", serialNumber, spa.get_id());
        Component panel = createComponent(Component.ComponentType.CONTROLLER.name(), "0", "Controller Panel", "56549-" + serialNumber, spa.get_id());

        ComponentState p1State = createComponentState(pump1, "LOW");
        ComponentState p2State = createComponentState(pump2, "MED");
        ComponentState p3State = createComponentState(pump3, "OFF");
        ComponentState p4State = createComponentState(pump4, "OFF");
        ComponentState p5State = createComponentState(pump5, "OFF");
        ComponentState p6State = createComponentState(pump6, "HIGH");
        ComponentState blower1State = createComponentState(blower1, "OFF");
        ComponentState blower2State = createComponentState(blower2, "LOW");
        ComponentState mister1State = createComponentState(mister1, "ON");
        ComponentState light1State = createComponentState(light1, "OFF");
        ComponentState light2State = createComponentState(light2, "HIGH");
        ComponentState light3State = createComponentState(light3, "OFF");
        ComponentState light4State = createComponentState(light4, "OFF");
        ComponentState light5State = createComponentState(light5, "OFF");
        ComponentState filter1State = createComponentState(filter1, "OFF");
        ComponentState filter2State = createComponentState(filter2, "OFF");
        ComponentState ozoneState = createComponentState(ozone1, "OFF");
        ComponentState aux1State = createComponentState(aux1, "ON");
        ComponentState aux2State = createComponentState(aux2, "OFF");
        ComponentState aux3State = createComponentState(aux3, "OFF");
        ComponentState microsilkState = createComponentState(microsilk, "ON");
        ComponentState gatewayState = createComponentState(gateway, "OFF");
        ComponentState mote1State = createComponentState(mote1, "2.50");
        ComponentState mote2State = createComponentState(mote2, "3.3367");

        SpaState spaState = new SpaStateBuilder()
                .runMode("Ready")
                .component(p1State).component(p2State).component(p3State)
                .component(p4State).component(p5State).component(p6State)
                .component(blower1State).component(blower2State)
                .component(mister1State)
                .component(light1State).component(light2State).component(light3State).component(light4State).component(light5State)
                .component(filter1State).component(filter2State)
                .component(aux1State).component(aux2State).component(aux3State)
                .component(microsilkState).component(ozoneState)
                .component(gatewayState).component(mote1State).component(mote2State)
                .targetDesiredTemp("102")
                .desiredTemp("102")
                .currentTemp("100")
                .build();

        spa.setCurrentState(spaState);
        spa = this.spaRepository.save(spa);

        return spa;
    }

    protected Spa createSmallSpaWithState(String serialNumber, String productName, String model, String oemId, String dealerId, User owner) {
        return createSmallSpaWithState(serialNumber, productName, model, oemId, dealerId, owner, null);
    }

    protected Spa createSmallSpaWithState(String serialNumber, String productName, String model, String oemId, String dealerId, User owner, String spaId) {
        Spa spa = new Spa();
        if (StringUtils.isNotEmpty(spaId)) {
            spa.set_id(spaId);
        }
        spa.setSerialNumber(serialNumber + (serialSuffix++));
        spa.setProductName(productName);
        spa.setModel(model);
        spa.setDealerId(dealerId);
        spa.setOwner(owner);
        spa.setOemId(oemId);
        spa.setManufacturedDate(new Date());
        spa.setRegistrationDate(new Date());
        spa.setP2pAPSSID("myWifi");
        spa.setP2pAPPassword("*******");
        spa = this.spaRepository.save(spa);

        Component gateway = createComponent(Component.ComponentType.GATEWAY.name(), "0", "IoT Gateway", serialNumber, spa.get_id());
        gateway.setRegistrationDate(new Date());
        componentRepository.save(gateway);

        String pumpPartNo1 = "1016205*";
        Component pump1 = createComponent(Component.ComponentType.PUMP.name(), "0", "Jets", pumpPartNo1 + serialNumber, spa.get_id());
        Component blower1 = createComponent(Component.ComponentType.BLOWER.name(), "0", "Bubbles", serialNumber, spa.get_id());
        Component mister1 = createComponent(Component.ComponentType.MISTER.name(), "0", "Mister", serialNumber, spa.get_id());
        Component light1 = createComponent(Component.ComponentType.LIGHT.name(), "0", "Main Light", serialNumber, spa.get_id());
        Component filter1 = createComponent(Component.ComponentType.FILTER.name(), "0", "Primary Filter", serialNumber, spa.get_id());
        Component panel = createComponent(Component.ComponentType.CONTROLLER.name(), "0", "Controller Panel", "56549-" + serialNumber, spa.get_id());

        ComponentState p1State = createComponentState(pump1, "OFF");
        ComponentState blower1State = createComponentState(blower1, "OFF");
        ComponentState mister1State = createComponentState(mister1, "OFF");
        ComponentState light1State = createComponentState(light1, "OFF");
        ComponentState filter1State = createComponentState(filter1, "OFF");
        ComponentState gatewayState = createComponentState(gateway, "OFF");
        ComponentState panelState = createComponentState(panel, "OFF");

        SpaState spaState = new SpaStateBuilder()
                .runMode("Rest")
                .component(p1State)
                .component(blower1State)
                .component(mister1State)
                .component(light1State)
                .component(filter1State)
                .component(gatewayState)
                .component(panelState)
                .targetDesiredTemp("100")
                .desiredTemp("100")
                .currentTemp("72")
                .build();

        spa.setCurrentState(spaState);
        spa = this.spaRepository.save(spa);

        return spa;
    }

    protected Spa createDemoSpa(String serialNumber, String productName, String model, String oemId, String dealerId, User owner) {
        return createDemoSpa(serialNumber, productName, model, oemId, dealerId, owner, null);
    }

    protected Spa createDemoSpa(String serialNumber, String productName, String model, String oemId, String dealerId, User owner, String spaId) {
        Spa spa = new Spa();
        if (StringUtils.isNotEmpty(spaId)) {
            spa.set_id(spaId);
        }
        spa.setSerialNumber(serialNumber + (serialSuffix++));
        spa.setProductName(productName);
        spa.setModel(model);
        spa.setDealerId(dealerId);
        spa.setOwner(owner);
        spa.setOemId(oemId);
        spa.setManufacturedDate(new Date());
        spa.setRegistrationDate(new Date());
        spa.setP2pAPSSID("pirateRadio");
        spa.setP2pAPPassword("*******");
        spa = this.spaRepository.save(spa);

        Component gateway = createComponent(Component.ComponentType.GATEWAY.name(), "0", "IoT Gateway", serialNumber, spa.get_id());
        gateway.setRegistrationDate(new Date());
        componentRepository.save(gateway);


        String pumpPartNo1 = "1016205*";
        Component pump1 = createComponent(Component.ComponentType.PUMP.name(), "0", "Main Jets", pumpPartNo1 + serialNumber, spa.get_id());
        Component pump2 = createComponent(Component.ComponentType.PUMP.name(), "1", "Captain's Chair", pumpPartNo1 + serialNumber, spa.get_id());
        Component pump3 = createComponent(Component.ComponentType.PUMP.name(), "2", "Massage Jets", pumpPartNo1 + serialNumber, spa.get_id());

        Component light1 = createComponent(Component.ComponentType.LIGHT.name(), "0", "Main Light", serialNumber, spa.get_id());
//        Component filter1 = createComponent(Component.ComponentType.FILTER.name(), "0", "Primary Filter", serialNumber, spa.get_id());
//        Component filter2 = createComponent(Component.ComponentType.FILTER.name(), "0", "Primary Filter", serialNumber, spa.get_id());
        Component panel = createComponent(Component.ComponentType.PANEL.name(), "0", "Controller Panel", "56549-" + serialNumber, spa.get_id());

        ComponentState p1State = createComponentState(pump1, "ON");
        ComponentState p2State = createComponentState(pump2, "ON");
        ComponentState p3State = createComponentState(pump3, "ON");
        ComponentState light1State = createComponentState(light1, "OFF");
        ComponentState panelState = createComponentState(panel, "Unlocked");
        ComponentState gatewayState = createComponentState(gateway, "OFF");

        SpaState spaState = new SpaStateBuilder()
                .runMode("Ready")
                .component(p1State)
                .component(p2State)
                .component(p3State)
                .component(light1State)
                .component(panelState)
                .component(gatewayState)
                .targetDesiredTemp("100")
                .desiredTemp("100")
                .currentTemp("97")
                .build();

        spa.setCurrentState(spaState);
        spa = this.spaRepository.save(spa);

        return spa;
    }

    protected Spa createDemoSpa1(String serialNumber, String oemId, String dealerId, User owner, String spaId, String gatewaySN) {
        final String productName = "Shark";
        final String model = "Blue";
        Spa spa = new Spa();
        if (StringUtils.isNotEmpty(spaId)) {
            spa.set_id(spaId);
        }
        spa.setSerialNumber(serialNumber + (serialSuffix++));
        spa.setProductName(productName);
        spa.setModel(model);
        spa.setDealerId(dealerId);
        spa.setOwner(owner);
        spa.setOemId(oemId);
        spa.setManufacturedDate(new Date());
        spa.setRegistrationDate(new Date());
        spa.setP2pAPSSID("myHomeWifi");
        spa.setP2pAPPassword("*******");
        spa = this.spaRepository.save(spa);

        Component gateway = createFixedComponent(Component.ComponentType.GATEWAY.name(), "0", "IoT Gateway", gatewaySN, spa.get_id());
        gateway.setRegistrationDate(new Date());
        componentRepository.save(gateway);


        String pumpPartNo1 = "1016205*";
        Component pump1 = createComponent(Component.ComponentType.PUMP.name(), "0", "Circulation", pumpPartNo1 + serialNumber, spa.get_id());
        Component pump2 = createComponent(Component.ComponentType.PUMP.name(), "1", "Captain's Chair", pumpPartNo1 + serialNumber, spa.get_id());
        Component light1 = createComponent(Component.ComponentType.LIGHT.name(), "0", "Main Light", serialNumber, spa.get_id());
        Component filter1 = createComponent(Component.ComponentType.FILTER.name(), "0", "Primary Filter", serialNumber, spa.get_id());

        ComponentState p1State = createComponentState(pump1, "LOW");
        ComponentState p2State = createComponentState(pump2, "OFF");
        ComponentState light1State = createComponentState(light1, "OFF");
        ComponentState gatewayState = createComponentState(gateway, "OFF");
        ComponentState filterState = createComponentState(filter1, "OFF");

        SpaState spaState = new SpaStateBuilder()
                .runMode("Ready")
                .component(p1State)
                .component(p2State)
                .component(light1State)
                .component(gatewayState)
                .component(filterState)
                .targetDesiredTemp("100")
                .desiredTemp("100")
                .currentTemp("97")
                .build();

        spa.setCurrentState(spaState);
        spa = this.spaRepository.save(spa);

        return spa;
    }

    protected Spa createDemoSpa2(String serialNumber, String oemId, String dealerId, User owner, String spaId, String gatewaySN) {
        final String productName = "Shark";
        final String model = "Tiger";

        Spa spa = new Spa();
        if (StringUtils.isNotEmpty(spaId)) {
            spa.set_id(spaId);
        }
        spa.setSerialNumber(serialNumber + (serialSuffix++));
        spa.setProductName(productName);
        spa.setModel(model);
        spa.setDealerId(dealerId);
        spa.setOwner(owner);
        spa.setOemId(oemId);
        spa.setManufacturedDate(new Date());
        spa.setRegistrationDate(new Date());
        spa.setP2pAPSSID("NSA Surveillance Van");
        spa.setP2pAPPassword("*******");
        spa = this.spaRepository.save(spa);

        Component gateway = createFixedComponent(Component.ComponentType.GATEWAY.name(), "0", "IoT Gateway", gatewaySN, spa.get_id());
        gateway.setRegistrationDate(new Date());
        componentRepository.save(gateway);


        String pumpPartNo1 = "1016411*";
        Component pump1 = createComponent(Component.ComponentType.PUMP.name(), "0", "Massage Jets", pumpPartNo1 + serialNumber, spa.get_id());
        Component pump2 = createComponent(Component.ComponentType.PUMP.name(), "1", "Captain's Chair", pumpPartNo1 + serialNumber, spa.get_id());
        Component light1 = createComponent(Component.ComponentType.LIGHT.name(), "0", "Main Light", serialNumber, spa.get_id());
        Component filter1 = createComponent(Component.ComponentType.FILTER.name(), "0", "Primary Filter", serialNumber, spa.get_id());
        Component ozone1 = createComponent(Component.ComponentType.OZONE.name(), "0", "Ozone", serialNumber, spa.get_id());

        ComponentState p1State = createComponentState(pump1, "LOW");
        ComponentState p2State = createComponentState(pump2, "OFF");
        ComponentState light1State = createComponentState(light1, "OFF");
        ComponentState gatewayState = createComponentState(gateway, "OFF");
        ComponentState filterState = createComponentState(filter1, "OFF");
        ComponentState ozoneState = createComponentState(ozone1, "OFF");

        SpaState spaState = new SpaStateBuilder()
                .runMode("Ready")
                .component(p1State)
                .component(p2State)
                .component(light1State)
                .component(gatewayState)
                .component(filterState)
                .component(ozoneState)
                .targetDesiredTemp("100")
                .desiredTemp("100")
                .currentTemp("100")
                .build();

        spa.setCurrentState(spaState);
        spa = this.spaRepository.save(spa);

        return spa;
    }

    protected Spa createDemoSpa3(String serialNumber, String oemId, String dealerId, User owner, String spaId, String gatewaySN) {
        final String productName = "Whale";
        final String model = "Beluga";

        Spa spa = new Spa();
        if (StringUtils.isNotEmpty(spaId)) {
            spa.set_id(spaId);
        }
        spa.setSerialNumber(serialNumber + (serialSuffix++));
        spa.setProductName(productName);
        spa.setModel(model);
        spa.setDealerId(dealerId);
        spa.setOwner(owner);
        spa.setOemId(oemId);
        spa.setManufacturedDate(new Date());
        spa.setRegistrationDate(new Date());
        spa.setP2pAPSSID("pirateRadio");
        spa.setP2pAPPassword("*******");
        spa = this.spaRepository.save(spa);

        Component gateway = createFixedComponent(Component.ComponentType.GATEWAY.name(), "0", "IoT Gateway", gatewaySN, spa.get_id());
        gateway.setRegistrationDate(new Date());
        componentRepository.save(gateway);


        String pumpPartNo1 = "1016205*";
        Component pump1 = createComponent(Component.ComponentType.PUMP.name(), "0", "Circulation", pumpPartNo1 + serialNumber, spa.get_id());
        Component pump2 = createComponent(Component.ComponentType.PUMP.name(), "1", "Captain's Chair", pumpPartNo1 + serialNumber, spa.get_id());
        Component light1 = createComponent(Component.ComponentType.LIGHT.name(), "0", "Light", serialNumber, spa.get_id());
        Component filter1 = createComponent(Component.ComponentType.FILTER.name(), "0", "Primary Filter", serialNumber, spa.get_id());
        Component filter2 = createComponent(Component.ComponentType.FILTER.name(), "1", "Secondary Filter", serialNumber, spa.get_id());

        ComponentState p1State = createComponentState(pump1, "LOW");
        ComponentState p2State = createComponentState(pump2, "OFF");
        ComponentState light1State = createComponentState(light1, "OFF");
        ComponentState gatewayState = createComponentState(gateway, "OFF");
        ComponentState filter1State = createComponentState(filter1, "OFF");
        ComponentState filter2State = createComponentState(filter2, "OFF");

        SpaState spaState = new SpaStateBuilder()
                .runMode("Ready")
                .component(p1State)
                .component(p2State)
                .component(light1State)
                .component(gatewayState)
                .component(filter1State)
                .component(filter2State)
                .targetDesiredTemp("103")
                .desiredTemp("103")
                .currentTemp("98")
                .build();

        spa.setCurrentState(spaState);
        spa = this.spaRepository.save(spa);

        return spa;
    }

    public static final String LF_INDICATES = "Persistent water flow problem. ";
    public static final String LF_WAPPENS = "Heater wil shut down while spa continues to function normally. ";
    public static final String LF_CAUSE = "Possible Causes: Plugged Filter, Low Water. ";
    public static final String LF_ACTION = "Remove Filter and clean. Add water. Open All Jets. Contact Dealer. ";
    public static final String OHH_INDICATES = "One of the Sensors had detected water temperature of 118\u00b0F (48\u00b0C). ";
    public static final String OHH_HAPPENS = "Spa Heater will shut down until temperature cools to 108\u00b0F (42\u00b0C) ";
    public static final String OHH_CAUSE = "Low speed pump overuse. Continuous Filtering programming error. Faulty pump. ";
    public static final String OHH_ACTION = "Ensure slice valves are open, Open all jets, Reprogram time cycles. Contact Dealer. ";

    protected Alert createAlert(Spa spa, String name, String level, String shortDesc, String longDesc, int compIndex) {
        Alert alert1 = new Alert();
        alert1.setName(name);
        alert1.setLongDescription(longDesc);
        alert1.setSeverityLevel(level);
        alert1.setComponent(spa.getCurrentState().getComponents().get(compIndex).getSerialNumber());
        alert1.setShortDescription(shortDesc);
        alert1.setCreationDate(new Date());
        alert1.setSpaId(spa.get_id());
        alertRepository.save(alert1);
        return alert1;
    }

    protected Spa addLowFlowYellowAlert(Spa spa) {
        Alert alert1 = createAlert(spa, "Low FLow", "yellow", LF_INDICATES, LF_WAPPENS + LF_CAUSE + LF_ACTION, 0);
        List<Alert> alerts = new ArrayList<Alert>();
        alerts.add(alert1);
        spa.setAlerts(alerts);
        spa = spaRepository.save(spa);
        return spa;
    }

    protected Spa addOverheatRedAlert(Spa spa) {
        Alert alert1 = createAlert(spa, "OVERHEAT", "red", OHH_INDICATES, OHH_HAPPENS + OHH_CAUSE + OHH_ACTION, 1);
        List<Alert> alerts = new ArrayList<Alert>();
        alerts.add(alert1);
        spa.setAlerts(alerts);
        spa = spaRepository.save(spa);
        return spa;
    }

    protected Spa add2Alerts(Spa spa) {
        Alert alert1 = createAlert(spa, "Low FLow", "yellow", LF_INDICATES, LF_WAPPENS + LF_CAUSE + LF_ACTION, 0);
        Alert alert2 = createAlert(spa, "OVERHEAT", "red", OHH_INDICATES, OHH_HAPPENS + OHH_CAUSE + OHH_ACTION, 1);
        List<Alert> alerts = Arrays.asList(alert1, alert2);
        spa.setAlerts(alerts);
        spa = spaRepository.save(spa);
        return spa;
    }


    protected TermsAndConditions createTermsAndAgreement(String version, String text) {
        TermsAndConditions tac = new TermsAndConditions();
        tac.setText(text);
        tac.setVersion(version);
        tac.setCreatedTimestamp(new Date());
        tac.setCurrent(Boolean.TRUE);
        tac = termsAndConditionsRepository.save(tac);
        return tac;
    }

    protected TacUserAgreement createAgreement(String userId, String version) {
        TacUserAgreement agreement = new TacUserAgreement();
        agreement.setUserId(userId);
        agreement.setVersion(version);
        agreement.setCurrent(Boolean.TRUE);
        agreement.setDateAgreed(new Date());
        agreement = tacUserAgreementRepository.save(agreement);
        return agreement;
    }

    protected void createSpaFaultLogAndDescription(String spaId) {
        final FaultLog log = new FaultLog();
        log.setSpaId(spaId);
        log.setControllerType("NGSC");
        log.setCode(1);
        log.setDealerId("ABC");
        log.setOemId("DEF");
        log.setOwnerId("GHI");
        log.setTimestamp(new Date());
        log.setSensorATemp(10);
        log.setSensorBTemp(11);
        log.setTargetTemp(12);
        log.setSeverity(FaultLogSeverity.ERROR);
        faultLogRepository.save(log);

        final FaultLogDescription description = new FaultLogDescription();
        description.setCode(1);
        description.setSeverity(FaultLogSeverity.ERROR);
        description.setDescription("Code description");
        description.setControllerType("NGSC");
        faultLogDescriptionRepository.save(description);
    }
    protected SpaTemplate createSpaTemplate(String productName, String model, String sku, String oemId, List<Material> materialList) {
        SpaTemplate spaTemplate = new SpaTemplate();
        spaTemplate.setProductName(productName);
        spaTemplate.setModel(model);
        spaTemplate.setSku(sku);
        spaTemplate.setOemId(oemId);
        spaTemplate.setNotes("This is a test note.");

        materialList.stream().forEach(m -> {
            m.setOemId(null);
            m.setUploadDate(null);
        });

        spaTemplate.setMaterialList(materialList);
        spaTemplate.setCreationDate(new Date());
        spaTemplateRepository.save(spaTemplate);
        return spaTemplate;
    }

    protected List<SpaTemplate> createSpaTemplates() {
        // create two oems
        List<Address> addresses = createAddresses(2);
        Oem oem1 = createOem("Blue Wave Spas, LTD", addresses.get(0), "oem001" );
        Oem oem2 = createOem("Jazzi Pool & Spa Products, LTD", addresses.get(1), "oem002");

        // create set of materials
        List<Material> materialList = setupTestMaterials(oem1, oem2);
        List<Material> spaTemplate1List = Arrays.asList(materialList.get(2), materialList.get(8));
        List<Material> spaTemplate2List = Arrays.asList(materialList.get(3), materialList.get(7));

        // create spaTemplates
        SpaTemplate st1 = createSpaTemplate("Maxx Collection", "581", "109834-1525-581", "oem001", spaTemplate1List);
        SpaTemplate st2 = createSpaTemplate("Maxx Collection", "811", "109834-1525-811", "oem001", spaTemplate2List);
        SpaTemplate st3 = createSpaTemplate("Hot Spring Spas", "Los Coyote", "109834-1525-811", "oem002", spaTemplate2List);

        List<SpaTemplate> spaTemplateList = Arrays.asList(st1, st2, st3);
        return spaTemplateList;
    }
}
