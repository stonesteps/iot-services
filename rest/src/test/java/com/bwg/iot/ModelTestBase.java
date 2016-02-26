package com.bwg.iot;

import com.bwg.iot.model.Address;
import com.bwg.iot.model.Dealer;
import com.bwg.iot.model.Oem;
import com.bwg.iot.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by triton on 2/22/16.
 */
public class ModelTestBase {
    @Autowired
    protected AddressRepository addressRepository;

    @Autowired
    protected DealerRepository dealerRepository;

    @Autowired
    protected OemRepository oemRepository;

    @Autowired
    protected UserRepository userRepository;

    protected Address createAddress() {
        Address address = new Address();
        address.setAddress1("5671");
        address.setAddress2("Honey Apple Crest");
        address.setCity("Village Five");
        address.setState("CA");
        address.setZip("E6L-4J4");
        address.setCountry("US");
        address.setEmail("gordon@gordon.com");
        address.setPhone("(506) 471-2382");
        return addressRepository.save(address);
    }

    protected Dealer createDealer(String name, Address address) {
        Dealer dealer = new Dealer();
        dealer.setName(name);
        dealer.setAddress(address);
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
}
