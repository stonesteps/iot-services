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
    protected OwnerRepository ownerRepository;

    @Autowired
    protected AlertRepository alertRepository;


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

    protected User createUser(String firstName, String lastName, String dealerId, String oemId, Address address, List<String> roles, String createdDate) {
        User user = new User();
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

    protected ComponentState createComponentState(String type, String port, String serialNumber, String value) {
        ComponentState cs = new ComponentState();
        cs.setComponentType(type);
        cs.setPort(port);
        cs.setSerialNumber(serialNumber);
        cs.setValue(value);
        return cs;
    }

    protected void clearAllData(){
        this.addressRepository.deleteAll();
        this.dealerRepository.deleteAll();
        this.oemRepository.deleteAll();
        this.spaRepository.deleteAll();
        this.componentRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    protected void createSpa(String serialNumber, String productName, String model, String dealerId) {
        Spa spa = new Spa();
        spa.setSerialNumber(serialNumber);
        spa.setProductName(productName);
        spa.setModel(model);
        spa.setDealerId(dealerId);

        this.spaRepository.save(spa);
    }

    protected Spa createFullSpaWithState(String serialNumber, String productName, String model, String dealerId, Owner owner) {

        Component gateway = createComponent(Component.ComponentType.GATEWAY.name(), "0", "riot-00255");
        gateway.setRegistrationDate(LocalDate.now().toString());
        componentRepository.save(gateway);

        Component mote1 = createComponent(Component.ComponentType.PUMP.name(), "0", "riot-002:2683069");
        gateway.setRegistrationDate(LocalDate.now().toString());
        componentRepository.save(mote1);

        Component mote2 = createComponent(Component.ComponentType.PUMP.name(), "0", "riot-002:1551515152a");
        gateway.setRegistrationDate(LocalDate.now().toString());
        componentRepository.save(mote2);

        Component pump1 = createComponent(Component.ComponentType.PUMP.name(), "0", "20398-0298s");
        Component pump2 = createComponent(Component.ComponentType.PUMP.name(), "1", "20398-52335");
        Component pump3 = createComponent(Component.ComponentType.PUMP.name(), "2", "20398-25511");
        Component pump4 = createComponent(Component.ComponentType.PUMP.name(), "3", "20398-25511-abd-f");
        Component pump5 = createComponent(Component.ComponentType.PUMP.name(), "4", "20398-25lskjf:3:3");
        Component pump6 = createComponent(Component.ComponentType.PUMP.name(), "5", "203s");
        Component blower1 = createComponent(Component.ComponentType.BLOWER.name(), "0", "987-987-987");
        Component blower2 = createComponent(Component.ComponentType.BLOWER.name(), "1", "98acedf37-98");
        Component mister1 = createComponent(Component.ComponentType.MISTER.name(), "0", "3246050-4s");
        Component light1 = createComponent(Component.ComponentType.LIGHT.name(), "0", "09sf-slk:332s:001");
        Component light2 = createComponent(Component.ComponentType.LIGHT.name(), "1", "09sf-slk:336a:002");
        Component light3 = createComponent(Component.ComponentType.LIGHT.name(), "2", "09sf-slk:336a:003");
        Component light4 = createComponent(Component.ComponentType.LIGHT.name(), "3", "09sf-slk:336a:35251a");
        Component light5 = createComponent(Component.ComponentType.LIGHT.name(), "4", "09affa3g");
        Component filter1 = createComponent(Component.ComponentType.FILTER.name(), "0", "0295885928");
        Component filter2 = createComponent(Component.ComponentType.FILTER.name(), "1", "a345345");
        Component aux1 = createComponent(Component.ComponentType.AUX.name(), "0", "radio-3987");
        Component aux2 = createComponent(Component.ComponentType.AUX.name(), "1", "waterfall-251-1");
        Component aux3 = createComponent(Component.ComponentType.AUX.name(), "2", "disco-ball-1555551");
        Component microsilk = createComponent(Component.ComponentType.MICROSILK.name(), "0", "m33");
        Component panel = createComponent(Component.ComponentType.PANEL.name(), "0", "bwg-0250-t25525");

        ComponentState p1State = createComponentState(Component.ComponentType.PUMP.name(), "0", "20398-0298s", "ON");
        ComponentState p2State = createComponentState(Component.ComponentType.PUMP.name(), "1", "20398-52335", "OFF");
        ComponentState p3State = createComponentState(Component.ComponentType.PUMP.name(), "2", "20398-25511", "ON");
        ComponentState p4State = createComponentState(Component.ComponentType.PUMP.name(), "3", "20398-25511-abd-f","OFF");
        ComponentState p5State = createComponentState(Component.ComponentType.PUMP.name(), "4", "20398-25lskjf:3:3", "MED");
        ComponentState p6State = createComponentState(Component.ComponentType.PUMP.name(), "5", "203s", "HI");
        ComponentState blower1State = createComponentState(Component.ComponentType.BLOWER.name(), "0", "987-987-987", "OFF");
        ComponentState blower2State = createComponentState(Component.ComponentType.BLOWER.name(), "1", "98acedf37-98", "ON");
        ComponentState mister1State = createComponentState(Component.ComponentType.MISTER.name(), "0", "3246050-4s", "ON");
        ComponentState light1State = createComponentState(Component.ComponentType.LIGHT.name(), "0", "09sf-slk:332s", "OFF");
        ComponentState light2State = createComponentState(Component.ComponentType.LIGHT.name(), "0", "09sf-slk:336a", "OFF");
        ComponentState light3State = createComponentState(Component.ComponentType.LIGHT.name(), "2", "09sf-slk:336a:003", "ON");
        ComponentState light4State = createComponentState(Component.ComponentType.LIGHT.name(), "3", "09sf-slk:336a:35251a", "ON");
        ComponentState light5State = createComponentState(Component.ComponentType.LIGHT.name(), "4", "09affa3g", "OFF");
        ComponentState filter1State = createComponentState(Component.ComponentType.FILTER.name(), "0", "0295885928", "ON");
        ComponentState filter2State = createComponentState(Component.ComponentType.FILTER.name(), "0", "a345345", "OFF");
        ComponentState aux1State = createComponentState(Component.ComponentType.AUX.name(), "0", "radio-3987", "ON");
        ComponentState aux2State = createComponentState(Component.ComponentType.AUX.name(), "1", "waterfall-251-1", "OFF");
        ComponentState aux3State = createComponentState(Component.ComponentType.AUX.name(), "2", "disco-ball-1555551", "OFF");
        ComponentState microsilkState = createComponentState(Component.ComponentType.MICROSILK.name(), "0", "m33", "ON");
        ComponentState panelState = createComponentState(Component.ComponentType.PANEL.name(), "0", "bwg-0250-t25525", "Locked");
        ComponentState gatewayState = createComponentState(Component.ComponentType.GATEWAY.name(), "0", "riot-00255", "Connected");
        ComponentState mote1State = createComponentState(Component.ComponentType.MOTE.name(), "0", "riot-002:2683069", "2 amps");
        ComponentState mote2State = createComponentState(Component.ComponentType.MOTE.name(), "0", "riot-002:1551515152a", "300 ma");

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

    protected Owner createOwner(String customerName, String firstName, String lastName) {
        Address address = new Address();
        address.setAddress1("1060 W Addison St");
        address.setAddress2("Apt. C");
        address.setCity("Chicago");
        address.setState("IL");
        address.setZip("60613");
        address.setCountry("US");
        address.setEmail("lou@blues.org");
        address.setPhone("800-123-4567");
        addressRepository.save(address);

        Owner owner = new Owner();
        owner.setCustomerName(customerName);
        owner.setLastName(lastName);
        owner.setFirstName(firstName);
        owner.setAddress(address);
        this.ownerRepository.save(owner);
        return owner;
    }


}
