package com.bwg.iot;

/**
 * Created by triton on 2/17/16.
 */

import com.bwg.iot.model.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/spas")
public class CustomSpaController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    SpaTemplateRepository spaTemplateRepository;

    @Autowired
    ComponentRepository componentRepository;

    @Autowired
    MaterialRepository materialRepository;

    @Autowired
    SpaRepository spaRepository;

    @RequestMapping(value = "/{spaId}/sellSpa", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> sellSpa(@PathVariable String spaId, @RequestBody SellSpaRequest request) {
        String ownerId = request.getOwnerId();
        String associateId = request.getAssociateId();
        String technicianId = request.getTechnicianId();
        Date salesDate = request.getSalesDate();
        String transactionCode = request.getTransactionCode();


        Spa spa = spaRepository.findOne(spaId);
        if (spa == null) {
            return new ResponseEntity<String>("Invalid Spa ID, spa not found",HttpStatus.BAD_REQUEST);
        }

        User owner = userRepository.findOne(ownerId);
        if (owner == null) {
            return new ResponseEntity<String>("Invalid Owner ID, owner not found",HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isNotEmpty(owner.getSpaId())) {
            return new ResponseEntity<String>("This person already owns a spa. We currently only allow 1 spa per user account.", HttpStatus.BAD_REQUEST);
        }

        User associate = userRepository.findOne(associateId);
        if (associate == null) {
            return new ResponseEntity<String>("Invalid associate ID, associate not found.",HttpStatus.BAD_REQUEST);
        } 
        if (!associate.hasRole(User.Role.ASSOCIATE.toString())) {
            return new ResponseEntity<String>("Invalid associate ID, user does not have ASSOCIATE role.",HttpStatus.BAD_REQUEST);
        }
        associate = associate.toMinimal();

        User technician = null;
        if (technicianId != null) {
            technician = userRepository.findOne(technicianId);
            if (technician != null && !technician.hasRole(User.Role.TECHNICIAN.toString())) {
                return new ResponseEntity<String>("Invalid technician ID, user does not have TECHNICIAN role.", HttpStatus.BAD_REQUEST);
            }
        }

        owner.setSpaId(spa.get_id());
        if (!owner.hasRole(User.Role.OWNER.toString())) {
            owner.getRoles().add(User.Role.OWNER.toString());
        }
        userRepository.save(owner);

        // update spa fields affected by sale
        spa.setOwner(owner);
        spa.setAssociate(associate);
        if (technician != null) {
            technician = technician.toMinimal();
            spa.setTechnician(technician);
        }
        if (salesDate == null) {
            salesDate = new Date();
        }
        spa.setSalesDate(salesDate);
        if (transactionCode != null) {
            spa.setTransactionCode(transactionCode);
        }
        spaRepository.save(spa);

        ResponseEntity<?> response = new ResponseEntity<Spa>(spa, HttpStatus.OK);
        return response;
    }

    @RequestMapping(value = "/buildSpa", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> buildSpa(@RequestHeader(name="remote_user")String remote_user, @RequestBody BuildSpaRequest request) {
        Spa myNewSpa = request.getSpa();
        List<Component> components = request.getComponents();

        // required params: templateId, serialNumber,
        if (myNewSpa.getManufacturedDate() == null) {
            myNewSpa.setManufacturedDate(new Date());
        }

        // validate user, get oemid
        User remoteUser = userRepository.findByUsername(remote_user);
        if (!remoteUser.hasRole(User.Role.OEM.toString())) {
            return new ResponseEntity<String>("User does not have OEM role", HttpStatus.FORBIDDEN);
        }

        // TODO: validate minimal set of components

        // Remove any existing spas using this gateway.
        // One may have been created when the Gateway was tested on the factory floor
        // (make sure they are unsold),  also delete any existing components
        Component gatewayComponent = null;
        String gatewaySerialNumber = null;
        Component existingGateway = null;
        List<Component> gateways = components.stream()
                .filter(c -> Component.ComponentType.GATEWAY.name().equalsIgnoreCase(c.getComponentType()))
                .collect(Collectors.toList());
        if (!gateways.isEmpty()) {
            gatewaySerialNumber = gateways.get(0).getSerialNumber();
        }
        if (gatewaySerialNumber != null) {
            existingGateway = componentRepository.findBySerialNumber(gatewaySerialNumber);
        }
        if (existingGateway != null) {
            String existingSpaId = existingGateway.getSpaId();
            if (existingSpaId != null) {
                List<Component> existingComponents = componentRepository.findBySpaId(existingSpaId);
                List<Component> testComponents = existingComponents.stream()
                        .filter(c -> c.isFactoryInit())
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(testComponents)) {
                    componentRepository.delete(testComponents);
                }
                Spa existingSpa = spaRepository.findOne(existingSpaId);
                if (existingSpa != null) {
                    spaRepository.delete(existingSpa);
                }
            }
        }

        // build up new spa
        myNewSpa.setOemId(remoteUser.getOemId());

        // validate spaTemplate, get productName, model, sku
        SpaTemplate spaTemplate = spaTemplateRepository.findOne(myNewSpa.getTemplateId());
        myNewSpa.setProductName(spaTemplate.getProductName());
        myNewSpa.setModel(spaTemplate.getModel());

        myNewSpa = spaRepository.insert(myNewSpa);

        // process component list
        for( Component component : components) {
            component.setSpaId(myNewSpa.get_id());
            component.setOemId(myNewSpa.getOemId());
            component.setFactoryInit(false);
            component.removeLinks();
            component.set_id(null);

            // TODO: validate SKU
            // validate component

            componentRepository.insert(component);
        }

        ResponseEntity<?> response = new ResponseEntity<Spa>(myNewSpa, HttpStatus.CREATED);
        return response;
    }
}
