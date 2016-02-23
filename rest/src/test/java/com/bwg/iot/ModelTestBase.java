package com.bwg.iot;

import com.bwg.iot.model.Address;
import com.bwg.iot.model.Dealer;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by triton on 2/22/16.
 */
public class ModelTestBase {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private DealerRepository dealerRepository;

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
}
