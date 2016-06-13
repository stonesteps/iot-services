package com.bwg.iot;

/**
 * Created by triton on 2/17/16.
 */

import com.bwg.iot.model.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/spas")
public class CustomSpaController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomSpaController.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    SpaTemplateRepository spaTemplateRepository;

    @Autowired
    ComponentRepository componentRepository;

    @Autowired
    MaterialRepository materialRepository;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    SpaRepository spaRepository;

    @Autowired
    SpaCommandRepository spaCommandRepository;

    @Autowired
    EntityLinks entityLinks;

    @Autowired
    SpaCommandHelper helper;

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


    @RequestMapping(value = "/{spaId}/recipes", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> saveSettings(@PathVariable String spaId, @RequestBody RecipeDTO request) {
        Recipe recipe = new Recipe();
        List<SpaCommand> settings = new ArrayList<>();

        try {
            for (HashMap.Entry<String, HashMap<String, String>> entry : request.getSettings().entrySet()) {
                // massage key
                String incomingKey = entry.getKey();
                int suffixIndex = incomingKey.indexOf("_");
                if (suffixIndex > 0) {
                    incomingKey = incomingKey.substring(0, suffixIndex);
                }
                // Hack due to componentType.PUMP RequestType.PUMPS
                if (Component.ComponentType.PUMP.name().equalsIgnoreCase(incomingKey) ||
                        Component.ComponentType.LIGHT.name().equalsIgnoreCase(incomingKey)) {
                    incomingKey = incomingKey + "S";
                }

                SpaCommand.RequestType key = SpaCommand.RequestType.valueOf(incomingKey);
                switch (key) {
                    case HEATER:
                        settings.add(helper.setDesiredTemp(spaId, entry.getValue(), key.getCode(), false));
                        break;
                    case PUMPS:
                    case CIRCULATION_PUMP:
                    case LIGHTS:
                    case BLOWER:
                    case MISTER:
                    case OZONE:
                    case MICROSILK:
                        settings.add(helper.setButtonCommand(spaId, entry.getValue(), key.getCode(), false));
                        break;
                    case FILTER:
                        settings.add(helper.setFilerCycleIntervals(spaId, entry.getValue(), key.getCode(), false));
                        break;
                    default:
                }
            }
        } catch (ValidationException ve) {
            LOGGER.error(ve.getMessage());
            return new ResponseEntity<Object>(ve.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException ex) {
            LOGGER.error(ex.getMessage());
            return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

        recipe.setSpaId(spaId);
        recipe.setName(request.getName());
        recipe.setNotes(request.getNotes());
        recipe.setSettings(settings);
        recipe.setSchedule(request.getSchedule());
        recipe.setCreationDate(new Date());

        // TODO: Validate
        recipe = recipeRepository.save(recipe);

        RecipeDTO returnDto = RecipeDTO.fromRecipe(recipe);
        returnDto.add(entityLinks.linkFor(com.bwg.iot.model.Spa.class)
                .slash("/" + recipe.getSpaId() + "/recipes/" + recipe.get_id())
                .withSelfRel());

        ResponseEntity<?> response = new ResponseEntity<RecipeDTO>(returnDto, HttpStatus.OK);
        return response;
    }

    @RequestMapping(value = "/{spaId}/recipes", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> getSpaRecipes(@PathVariable("spaId") final String spaId, final Pageable pageable) {
            final Page<Recipe> recipes = recipeRepository.findBySpaId(spaId, pageable);
            List<RecipeDTO> dtos = new ArrayList<RecipeDTO>();
            for (Recipe recipe : recipes) {
                RecipeDTO dto = RecipeDTO.fromRecipe(recipe);
                dto.add(entityLinks.linkFor(com.bwg.iot.model.Spa.class)
                        .slash("/" + recipe.getSpaId() + "/recipes/" + recipe.get_id())
                        .withSelfRel());
                dtos.add(dto);
            }

            Resources<RecipeDTO> resources = new Resources<>(dtos);
            resources.add(linkTo(methodOn(CustomSpaController.class).getSpaRecipes(spaId, pageable)).withSelfRel());
            return ResponseEntity.ok(resources);
    }



    @RequestMapping(value = "/{spaId}/recipes/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getSpaRecipe(@PathVariable("id") String id) {
        Recipe recipe = recipeRepository.findOne(id);
        if (recipe == null) {
            LOGGER.info("Spa Recipe with id " + id + " not found");
            return new ResponseEntity<String>("Spa Recipe with id " + id + " not found", HttpStatus.NOT_FOUND);
        }
        RecipeDTO dto = RecipeDTO.fromRecipe(recipe);
        dto.add(entityLinks.linkFor(com.bwg.iot.model.Spa.class)
                .slash("/" + recipe.getSpaId() + "/recipes/" + recipe.get_id())
                .withSelfRel());
        return new ResponseEntity<RecipeDTO>(dto, HttpStatus.OK);
    }

    @RequestMapping(value = "/{spaId}/recipes/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateSpaRecipe(@PathVariable("id") String id, @RequestBody RecipeDTO recipeDto) {
        Recipe recipe = RecipeDTO.toRecipe(recipeDto);
        Recipe currentRecipe = recipeRepository.findOne(id);
        if (currentRecipe == null) {
            LOGGER.info("Spa Recipe with id " + id + " not found");
            return new ResponseEntity<String>("Spa Recipe with id " + id + " not found", HttpStatus.NOT_FOUND);
        }

        currentRecipe.setName(recipe.getName());
        currentRecipe.setNotes(recipe.getNotes());
        currentRecipe.setSettings(recipe.getSettings());

        currentRecipe = recipeRepository.save(currentRecipe);
        currentRecipe.add(entityLinks.linkFor(com.bwg.iot.model.Spa.class)
                .slash("/" + recipe.getSpaId() + "/recipes/" + recipe.get_id())
                .withSelfRel());
        RecipeDTO updatedRecipeDTO = RecipeDTO.fromRecipe(currentRecipe);
        return new ResponseEntity<RecipeDTO>(updatedRecipeDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/{spaId}/recipes/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteSpaRecipe(@PathVariable("id") String id) {

        Recipe currentRecipe = recipeRepository.findOne(id);
        if (currentRecipe == null) {
            LOGGER.info("Unable to delete: Spa Recipe with id " + id + " not found");
            return new ResponseEntity<String>("Unable to delete: Spa Recipe with id " + id + " not found", HttpStatus.NOT_FOUND);
        }

        recipeRepository.delete(id);
        return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/{spaId}/recipes/{id}/run", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> runSpaRecipe(@PathVariable("id") String id) {
        Recipe recipe = recipeRepository.findOne(id);
        if (recipe == null) {
            LOGGER.info("Spa Recipe with id " + id + " not found");
            return new ResponseEntity<String>("Spa Recipe with id " + id + " not found", HttpStatus.NOT_FOUND);
        }

        spaCommandRepository.insert(recipe.getSettings());
        Spa spa = spaRepository.findOne(recipe.getSpaId());
        return new ResponseEntity<Spa>(spa, HttpStatus.OK);
    }
}
