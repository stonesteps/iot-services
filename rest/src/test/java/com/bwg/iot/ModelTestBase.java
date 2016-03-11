package com.bwg.iot;

import com.bwg.iot.builders.SpaStateBuilder;
import com.bwg.iot.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
    protected AlertRepository alertRepository;

    @Autowired
    protected TermsAndConditionsRepository termsAndConditionsRepository;

    @Autowired
    protected TacUserAgreementRepository tacUserAgreementRepository;


    protected void clearAllData(){
        this.addressRepository.deleteAll();
        this.dealerRepository.deleteAll();
        this.oemRepository.deleteAll();
        this.spaRepository.deleteAll();
        this.componentRepository.deleteAll();
        this.userRepository.deleteAll();
        this.alertRepository.deleteAll();
        this.tacUserAgreementRepository.deleteAll();
        this.termsAndConditionsRepository.deleteAll();
    }

    protected Address createAddress() {
        Address address = new Address();
        address.setAddress1("30");
        address.setAddress2("Honey Apple Crest");
        address.setCity("Village Five");
        address.setState("CA");
        address.setZip("E6L-4J4");
        address.setCountry("US");
        address.setEmail("gordon@gordon.com");
        address.setPhone("(506) 471-2382");
        return addressRepository.save(address);
    }

    protected Address createAddress(int i) {
        Address address = new Address();
        address.setAddress1("30"+i+" "+i+"35th Ave" );
        address.setAddress2("Suite 10"+i);
        address.setCity("San Diego");
        address.setState("CA");
        address.setZip("E6L-4J4");
        address.setCountry("US");
        address.setEmail("gordon"+i+"@riot.com");
        address.setPhone("(506) 471-2382");
        return addressRepository.save(address);
    }

    protected List<Address> createAddresses(int count) {
        List<Address> addresses = new ArrayList<>(count);
        for(int i = 0; i < count; i++){
            addresses.add(createAddress(i));
        }
        return addresses;
    }

    protected Dealer createDealer(String name, Address address, String oemId) {
        Dealer dealer = new Dealer();
        dealer.setName(name);
        dealer.setAddress(address);
        dealer.setOemId(oemId);
        return dealerRepository.save(dealer);
    }

    protected Oem createOem(String name, Address address) {
        Oem oem = new Oem();
        oem.setName(name);
        oem.setAddress(address);
        return oemRepository.save(oem);
    }

    protected User createUser(String username, String firstName, String lastName, String dealerId, String oemId, Address address, List<String> roles, String createdDate) {
        User user = new User();
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setDealerId(dealerId);
        user.setOemId(oemId);
        user.setAddress(address);
        user.setRoles(roles);
        user.setCreatedDate(createdDate);
        userRepository.save(user);
        return user;
    }

    protected Component createComponent(String type, String port, String serialNumber) {
        Component component = new Component();
        component.setComponentType(type);
        component.setSerialNumber(serialNumber);
        component.setPort(port);
        componentRepository.save(component);
        return component;
    }

    protected ComponentState createComponentState(Component component, String value) {
        ComponentState cs = new ComponentState();
        cs.setComponentType(component.getComponentType());
        cs.setPort(component.getPort());
        cs.setSerialNumber(component.getSerialNumber());
        cs.setValue(value);
        cs.setTargetValue(value);
        return cs;
    }

    protected Spa createUnsoldSpa(String serialNumber, String productName, String model, String dealerId) {
        Spa spa = new Spa();
        spa.setSerialNumber(serialNumber);
        spa.setProductName(productName);
        spa.setModel(model);
        spa.setDealerId(dealerId);

        this.spaRepository.save(spa);
        return spa;
    }

    protected Spa createFullSpaWithState(String serialNumber, String productName, String model, String dealerId, User owner) {

        Component gateway = createComponent(Component.ComponentType.GATEWAY.name(), "0", serialNumber+"riot-00255");
        gateway.setRegistrationDate(LocalDate.now().toString());
        componentRepository.save(gateway);

        Component mote1 = createComponent(Component.ComponentType.MOTE.name(), "0", serialNumber+"riot-002:2683069");
        gateway.setRegistrationDate(LocalDate.now().toString());
        componentRepository.save(mote1);

        Component mote2 = createComponent(Component.ComponentType.MOTE.name(), "0", serialNumber+"riot-002:1551515152a");
        gateway.setRegistrationDate(LocalDate.now().toString());
        componentRepository.save(mote2);

        Component pump1 = createComponent(Component.ComponentType.PUMP.name(), "0", serialNumber+"20398-0298s");
        Component pump2 = createComponent(Component.ComponentType.PUMP.name(), "1", serialNumber+"20398-52335");
        Component pump3 = createComponent(Component.ComponentType.PUMP.name(), "2", serialNumber+"20398-25511");
        Component pump4 = createComponent(Component.ComponentType.PUMP.name(), "3", serialNumber+"20398-25511-abd-f");
        Component pump5 = createComponent(Component.ComponentType.PUMP.name(), "4", serialNumber+"20398-25lskjf:3:3");
        Component pump6 = createComponent(Component.ComponentType.PUMP.name(), "5", serialNumber+"203s");
        Component blower1 = createComponent(Component.ComponentType.BLOWER.name(), "0", serialNumber+"987-987-987");
        Component blower2 = createComponent(Component.ComponentType.BLOWER.name(), "1", serialNumber+"98acedf37-98");
        Component mister1 = createComponent(Component.ComponentType.MISTER.name(), "0", serialNumber+"3246050-4s");
        Component light1 = createComponent(Component.ComponentType.LIGHT.name(), "0", serialNumber+"09sf-slk:332s:001");
        Component light2 = createComponent(Component.ComponentType.LIGHT.name(), "1", serialNumber+"09sf-slk:336a:002");
        Component light3 = createComponent(Component.ComponentType.LIGHT.name(), "2", serialNumber+"09sf-slk:336a:003");
        Component light4 = createComponent(Component.ComponentType.LIGHT.name(), "3", serialNumber+"09sf-slk:336a:35251a");
        Component light5 = createComponent(Component.ComponentType.LIGHT.name(), "4", serialNumber+"09affa3g");
        Component filter1 = createComponent(Component.ComponentType.FILTER.name(), "0", serialNumber+"0295885928");
        Component filter2 = createComponent(Component.ComponentType.FILTER.name(), "1", serialNumber+"a345345");
        Component aux1 = createComponent(Component.ComponentType.AUX.name(), "0", serialNumber+"radio-3987");
        Component aux2 = createComponent(Component.ComponentType.AUX.name(), "1", serialNumber+"waterfall-251-1");
        Component aux3 = createComponent(Component.ComponentType.AUX.name(), "2", serialNumber+"disco-ball-1555551");
        Component microsilk = createComponent(Component.ComponentType.MICROSILK.name(), "0", serialNumber+"m33");
        Component panel = createComponent(Component.ComponentType.PANEL.name(), "0", serialNumber+"bwg-0250-t25525");

        ComponentState p1State = createComponentState(pump1, "ON");
        ComponentState p2State = createComponentState(pump2, "OFF");
        ComponentState p3State = createComponentState(pump3, "ON");
        ComponentState p4State = createComponentState(pump4,"OFF");
        ComponentState p5State = createComponentState(pump5, "MED");
        ComponentState p6State = createComponentState(pump6, "HI");
        ComponentState blower1State = createComponentState(blower1, "OFF");
        ComponentState blower2State = createComponentState(blower2, "ON");
        ComponentState mister1State = createComponentState(mister1, "ON");
        ComponentState light1State = createComponentState(light1, "OFF");
        ComponentState light2State = createComponentState(light2, "OFF");
        ComponentState light3State = createComponentState(light3, "ON");
        ComponentState light4State = createComponentState(light4, "ON");
        ComponentState light5State = createComponentState(light5, "OFF");
        ComponentState filter1State = createComponentState(filter1, "ON");
        ComponentState filter2State = createComponentState(filter2, "OFF");
        ComponentState aux1State = createComponentState(aux1, "ON");
        ComponentState aux2State = createComponentState(aux2, "OFF");
        ComponentState aux3State = createComponentState(aux3, "OFF");
        ComponentState microsilkState = createComponentState(microsilk, "ON");
        ComponentState panelState = createComponentState(panel, "Locked");
        ComponentState gatewayState = createComponentState(gateway, "Connected");
        ComponentState mote1State = createComponentState(mote1, "2 amps");
        ComponentState mote2State = createComponentState(mote2, "300 ma");

        SpaState spaState = new SpaStateBuilder()
                .runMode("Ready")
                .component(p1State).component(p2State).component(p3State)
                .component(p4State).component(p5State).component(p6State)
                .component(blower1State).component(blower2State)
                .component(mister1State)
                .component(light1State).component(light2State).component(light3State).component(light4State).component(light5State)
                .component(filter1State).component(filter2State)
                .component(aux1State).component(aux2State).component(aux3State)
                .component(microsilkState).component(panelState)
                .component(gatewayState).component(mote1State).component(mote2State)
                .targetDesiredTemp("101")
                .build();

        Spa spa = new Spa();
        spa.setSerialNumber(serialNumber);
        spa.setProductName(productName);
        spa.setModel(model);
        spa.setDealerId(dealerId);
        spa.setOwner(owner);
        spa.setCurrentState(spaState);
        spa.setOemId("cab335");
        spa.setManufacturedDate(LocalDate.now().toString());
        spa.setRegistrationDate(LocalDate.now().toString());
        spa.setP2pAPSSID("myWifi");
        spa.setP2pAPPassword("*******");

        spa = this.spaRepository.save(spa);

        Alert alert1 = new Alert();
        alert1.setName("Replace Filter");
        alert1.setLongDescription("The filter is old and needs to be replaced");
        alert1.setSeverityLevel("yellow");
        alert1.setComponent("filter1");
        alert1.setShortDescription("Replace Filter");
        alert1.setCreationDate(LocalDateTime.now().toString());
        alert1.setSpaId(spa.get_id());
        alertRepository.save(alert1);

        spa.setAlerts(Arrays.asList(alert1));
        spa = this.spaRepository.save(spa);
        return spa;
    }

    protected Spa createSmallSpaWithState(String serialNumber, String productName, String model, String dealerId, User owner) {

        Component gateway = createComponent(Component.ComponentType.GATEWAY.name(), "0", serialNumber+"001g");
        gateway.setRegistrationDate(LocalDate.now().toString());
        componentRepository.save(gateway);

        Component mote1 = createComponent(Component.ComponentType.MOTE.name(), "0", serialNumber+"0028:69");
        gateway.setRegistrationDate(LocalDate.now().toString());
        componentRepository.save(mote1);

        Component pump1 = createComponent(Component.ComponentType.PUMP.name(), "0", serialNumber+"20398-0298s");
        Component blower1 = createComponent(Component.ComponentType.BLOWER.name(), "0", serialNumber+"987-987-987");
        Component mister1 = createComponent(Component.ComponentType.MISTER.name(), "0", serialNumber+"3246050-4s");
        Component light1 = createComponent(Component.ComponentType.LIGHT.name(), "0", serialNumber+"09sf-slk:332s:001");
        Component filter1 = createComponent(Component.ComponentType.FILTER.name(), "0", serialNumber+"0295885928");
        Component panel = createComponent(Component.ComponentType.PANEL.name(), "0", serialNumber+"bwg-0250-t25525");

        ComponentState p1State = createComponentState(pump1, "ON");
        ComponentState blower1State = createComponentState(blower1, "OFF");
        ComponentState mister1State = createComponentState(mister1, "ON");
        ComponentState light1State = createComponentState(light1, "OFF");
        ComponentState filter1State = createComponentState(filter1, "ON");
        ComponentState panelState = createComponentState(panel, "Locked");
        ComponentState gatewayState = createComponentState(gateway, "Connected");
        ComponentState mote1State = createComponentState(mote1, "2 amps");

        SpaState spaState = new SpaStateBuilder()
                .runMode("Ready")
                .component(p1State)
                .component(blower1State)
                .component(mister1State)
                .component(light1State)
                .component(panelState)
                .component(gatewayState).component(mote1State)
                .targetDesiredTemp("100")
                .build();

        Spa spa = new Spa();
        spa.setSerialNumber(serialNumber);
        spa.setProductName(productName);
        spa.setModel(model);
        spa.setDealerId(dealerId);
        spa.setOwner(owner);
        spa.setCurrentState(spaState);
        spa.setOemId("cab335");
        spa.setManufacturedDate(LocalDate.now().toString());
        spa.setRegistrationDate(LocalDate.now().toString());
        spa.setP2pAPSSID("myWifi");
        spa.setP2pAPPassword("*******");

        spa = this.spaRepository.save(spa);

        return spa;
    }

    protected TermsAndConditions createTermsAndAgreement(String version, String text){
        TermsAndConditions tac = new TermsAndConditions();
        tac.setText(text);
        tac.setVersion(version);
        tac.setCreatedTimestamp(LocalDateTime.now().toString());
        tac.setCurrent(Boolean.TRUE);
        tac = termsAndConditionsRepository.save(tac);
        return tac;
    }

    protected TacUserAgreement createAgreement(String userId, String version){
        TacUserAgreement agreement = new TacUserAgreement();
        agreement.setUserId(userId);
        agreement.setVersion(version);
        agreement.setCurrent(Boolean.TRUE);
        agreement.setDateAgreed(LocalDateTime.now().toString());
        agreement = tacUserAgreementRepository.save(agreement);
        return agreement;
    }

}
