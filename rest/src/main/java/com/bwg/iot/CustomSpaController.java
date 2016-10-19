package com.bwg.iot;

/**
 * Created by triton on 2/17/16.
 */

import com.bwg.iot.model.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.spi.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ValidationException;
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

    @Autowired
    Environment environment;

    @RequestMapping(value = "/{spaId}/sellSpa", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> sellSpa(@PathVariable String spaId, @RequestBody SellSpaRequest request) {
        String ownerId = request.getOwnerId();
        String associateId = request.getAssociateId();
        String technicianId = request.getTechnicianId();
        Date salesDate = request.getSalesDate();
        String transactionCode = request.getTransactionCode();


        Spa spa = spaRepository.findOne(spaId);
        if (spa == null) {
            throw new NoSuchElementException("Spa not found. Spa ID: "+spaId);
        }

        User owner = userRepository.findOne(ownerId);
        if (owner == null) {
            throw new UsernameNotFoundException("Owner not found, Owner ID:" + ownerId);
        }
        if (StringUtils.isNotEmpty(owner.getSpaId())) {
            throw new ValidationException("This person already owns a spa. We currently only allow 1 spa per user account.");
        }
        if (owner.hasRole(User.Role.BWG.name())
            || owner.hasRole(User.Role.OEM.name())
            || owner.hasRole(User.Role.DEALER.name())) {
            throw new ValidationException("An employee in the system may not also be a spa owner. Please use a different email address for the owner account.");
        }

        User associate = userRepository.findOne(associateId);
        if (associate == null) {
            throw new ValidationException("Invalid associate ID, associate not found.");
        } 
        if (!associate.hasRole(User.Role.ASSOCIATE.toString())) {
            throw new ValidationException("Invalid associate ID, user does not have ASSOCIATE role.");
        }
        associate = associate.toMinimal();

        User technician = null;
        if (technicianId != null) {
            technician = userRepository.findOne(technicianId);
            if (technician != null && !technician.hasRole(User.Role.TECHNICIAN.toString())) {
                throw new ValidationException("Invalid technician ID, user does not have TECHNICIAN role.");
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
        // validate user, get oemid
        User remoteUser = userRepository.findByUsername(remote_user);
        if (remoteUser == null || !remoteUser.hasRole(User.Role.OEM.toString())) {
            throw new UnauthorizedException("User does not have the required OEM role");
        }

        // TODO: validate minimal set of components
        List<Component> components = request.getComponents();
        components.forEach(c -> {
            if (c.getComponentType()== null || c.getComponentType().isEmpty()) {
                c.setComponentType(c.getMaterialType());
            }});
        List<Component> gateways = components.stream()
                .filter(c -> Component.ComponentType.GATEWAY.name().equalsIgnoreCase(c.getComponentType()))
                .collect(Collectors.toList());
        if (gateways.isEmpty()) {
            throw new ValidationException("Spa must have at least 1 Gateway Component");
        }

        Spa myNewSpa = new Spa();

        // Find and use existing spas using this gateway.
        // One may have been created when the Gateway was tested on the factory floor
        Component existingGateway = null;
        Component gatewayComponent = gateways.get(0);
        String gatewaySerialNumber = gatewayComponent.getSerialNumber();
        if (gatewaySerialNumber != null) {
            existingGateway = componentRepository.findBySerialNumber(gatewaySerialNumber);
        }
        if (existingGateway != null) {
            if (!existingGateway.isFactoryInit()) {
                throw new ValidationException("A Gateway Board with serial number:"
                        + gatewaySerialNumber + " is already in use.");
            }
            existingGateway.setSku(gatewayComponent.getSku());
            existingGateway.setName(gatewayComponent.getName());
            existingGateway.setFactoryInit(false);
            componentRepository.save(existingGateway);

            components.remove(gatewayComponent);
            components.add(existingGateway);

            String existingSpaId = existingGateway.getSpaId();
            if (existingSpaId != null) {
                List<Component> existingComponents = componentRepository.findBySpaId(existingSpaId);
                List<Component> testComponents = existingComponents.stream()
                        .filter(c -> c.isFactoryInit())
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(testComponents)) {
                    componentRepository.delete(testComponents);
                }
                myNewSpa = spaRepository.findOne(existingSpaId);
            }
        }

        Spa requestSpa = request.getSpa();
        // required params: templateId, serialNumber,
        if (myNewSpa.getManufacturedDate() == null) {
            myNewSpa.setManufacturedDate(new Date());
        }
        myNewSpa.setOemId(remoteUser.getOemId());

        // validate spaTemplate, get productName, model, sku
        if (requestSpa.getTemplateId() != null) {
            SpaTemplate spaTemplate = spaTemplateRepository.findOne(requestSpa.getTemplateId());
            if (spaTemplate == null) {
                throw new ValidationException("Invalid Spa Template ID");
            }
            myNewSpa.setTemplateId(requestSpa.getTemplateId());
            myNewSpa.setProductName(spaTemplate.getProductName());
            myNewSpa.setModel(spaTemplate.getModel());
        }

        myNewSpa.setSerialNumber(requestSpa.getSerialNumber());

        myNewSpa = spaRepository.save(myNewSpa);

        // create TurnOffSpa default Recipe,
        // same _id as spaId to avoid db fetch when creating link
        Recipe turnMeOff = new Recipe();
        turnMeOff.setName("Turn Off Spa");
        turnMeOff.set_id(myNewSpa.get_id());
        turnMeOff.setSpaId(myNewSpa.get_id());
        turnMeOff.setSystem(true);
        List<SpaCommand> commands = new ArrayList<>();
        commands.add(SpaCommand.newInstanceNoHeat());

        // process component list
        for( Component component : components) {
            // TODO: validate SKU
            // validate component

            component.setSpaId(myNewSpa.get_id());
            component.setOemId(myNewSpa.getOemId());
            component.setFactoryInit(false);
            component.removeLinks();

            SpaCommand sc = SpaCommand.newInstanceFromComponent(component);
            if (sc != null) {
                sc.getMetadata().put("Recipe", "Turn Off Spa");
                commands.add(sc);
            }
            componentRepository.save(component);
        }
        turnMeOff.setSettings(commands);
        recipeRepository.insert(turnMeOff);

        ResponseEntity<?> response = new ResponseEntity<Spa>(myNewSpa, HttpStatus.CREATED);
        return response;
    }


    @RequestMapping(value = "/{spaId}/recipes", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> saveSettings(@PathVariable String spaId, @RequestBody RecipeDTO request) {
        // check for maximum allowed number of Recipes
        long maxRecipeCount = Long.valueOf(environment.getProperty(PropertyNames.MAX_RECIPES));
        long spaRecipeCount = recipeRepository.countBySpaId(spaId);
        if (spaRecipeCount >= maxRecipeCount) {
            throw new DataIntegrityViolationException("Maximum number of recipes exceeded for this spa.");
        }

        Recipe recipe = new Recipe();
        List<SpaCommand> settings = new ArrayList<>();
        HashMap<String, String> metadata = new HashMap<>(1);
        metadata.put("Recipe", request.getName());
        try {
            for (HashMap.Entry<String, HashMap<String, String>> entry : request.getSettings().entrySet()) {
                // massage key
                String incomingKey = entry.getKey();
                int suffixIndex = incomingKey.indexOf("_");
                if (suffixIndex > 0) {
                    incomingKey = incomingKey.substring(0, suffixIndex);
                }

                SpaCommand.RequestType key = SpaCommand.RequestType.valueOf(incomingKey);
                switch (key) {
                    case HEATER:
                        settings.add(helper.setDesiredTemp(spaId, entry.getValue(), key.getCode(), metadata, false));
                        break;
                    case PUMP:
                    case CIRCULATION_PUMP:
                    case LIGHT:
                    case BLOWER:
                    case MISTER:
                    case OZONE:
                    case MICROSILK:
                        settings.add(helper.setButtonCommand(spaId, entry.getValue(), key.getCode(), metadata, false));
                        break;
                    case FILTER:
                        settings.add(helper.setFilerCycleIntervals(spaId, entry.getValue(), key.getCode(), metadata, false));
                        break;
                    default:
                }
            }
        } catch (ValidationException ve) {
            LOGGER.error(ve.getMessage());
            throw ve;
        } catch (IllegalArgumentException ex) {
            LOGGER.error(ex.getMessage());
            throw ex;
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
            throw new NoSuchElementException("Spa Recipe with id " + id + " not found");
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
            throw new NoSuchElementException("Spa Recipe with id " + id + " not found");
        }

        if (currentRecipe.isSystem()) {
            currentRecipe.setSchedule(recipe.getSchedule());
        } else {
            currentRecipe.setName(recipe.getName());
            currentRecipe.setNotes(recipe.getNotes());
            currentRecipe.setSettings(recipe.getSettings());
            currentRecipe.setSchedule(recipe.getSchedule());
        }

        final String spaId = currentRecipe.getSpaId();
        final HashMap<String, String> meta = new HashMap<>();
        meta.put("Recipe",currentRecipe.getName());
        currentRecipe.getSettings().forEach(spaCommand -> {
            spaCommand.setSpaId(spaId);
            spaCommand.setMetadata(meta);
        });

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
            throw new NoSuchElementException("Unable to delete: Spa Recipe with id " + id + " not found");
        }
        if (currentRecipe.isSystem()) {
            throw new DataIntegrityViolationException("Cannot Delete System Presets");
        }

        recipeRepository.delete(id);
        return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/{spaId}/recipes/{id}/run", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> runSpaRecipe(@PathVariable("id") String id,
                                          @RequestHeader(name="remote_user", required=false)String remote_user,
                                          @RequestHeader (value="x-forwarded-prefix", required=false) String pathPrefix) {
        Recipe recipe = recipeRepository.findOne(id);
        if (recipe == null) {
            LOGGER.info("Spa Recipe with id " + id + " not found");
            throw new NoSuchElementException("Spa Recipe with id " + id + " not found");
        }

        recipe.getSettings().forEach(spaCommand -> {
            spaCommand.getMetadata().put(SpaCommand.REQUESTED_BY, (remote_user == null) ? "Anonymous User" : remote_user);
            spaCommand.getMetadata().put(SpaCommand.REQUEST_PATH, (pathPrefix == null) ? "Direct" : pathPrefix);
        });

        spaCommandRepository.insert(recipe.getSettings());
        Spa spa = spaRepository.findOne(recipe.getSpaId());
        return new ResponseEntity<Spa>(spa, HttpStatus.OK);
    }
}
