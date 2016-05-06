package com.bwg.iot;

import com.bwg.iot.model.FaultLog;
import com.bwg.iot.model.FaultLogDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;

import java.util.HashMap;
import java.util.Map;

/**
 * Processes FaultLog entry appending description.
 */
public final class FaultLogResourceProcessor implements ResourceProcessor<Resource<FaultLog>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FaultLogResourceProcessor.class);
    private final FaultLogDescriptionRepository faultLogDescriptionRepository;
    private final Map<String, FaultLogDescription> cache = new HashMap<>();

    public FaultLogResourceProcessor(final FaultLogDescriptionRepository faultLogDescriptionRepository) {
        this.faultLogDescriptionRepository = faultLogDescriptionRepository;
    }

    @Override
    public Resource<FaultLog> process(final Resource<FaultLog> resource) {
        final FaultLog faultLog = resource.getContent();

        final int code = faultLog.getCode();
        final String controllerType = faultLog.getControllerType();

        final String cacheKey = controllerType + code;
        FaultLogDescription description = cache.get(cacheKey);

        if (description == null && !cache.containsKey(cacheKey)) {
            description = faultLogDescriptionRepository.findFirstByCodeAndControllerType(code, controllerType);
            cache.put(cacheKey, description);
        }

        faultLog.setFaultLogDescription(description);
        return resource;
    }
}
