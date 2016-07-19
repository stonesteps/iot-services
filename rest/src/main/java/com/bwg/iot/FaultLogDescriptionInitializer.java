package com.bwg.iot;

import com.bwg.iot.model.FaultLogDescription;
import com.bwg.iot.model.FaultLogSeverity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.separator.DefaultRecordSeparatorPolicy;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Initializes the faultLogDescription collection.
 *
 * Created by triton on 5/20/16.
 */
@Component
public class FaultLogDescriptionInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(FaultLogDescriptionInitializer.class);
    private static String defaultDescriptions;
    private static String overrideDescriptions;

    @Autowired
    public FaultLogDescriptionInitializer(Environment env, FaultLogDescriptionRepository repository, MongoOperations operations) throws Exception {

        if (repository.count() != 0) {
            LOGGER.info("Found " + Long.toString(repository.count()) + " exising Fault Log Descriptions");
            return;
        }

        defaultDescriptions = env.getProperty(PropertyNames.FAULT_DESC_RESOURCE);
        overrideDescriptions = env.getProperty(PropertyNames.FAULT_DESC_FILE);
        List<FaultLogDescription> descriptions = readFaultLogDescriptions();
        LOGGER.info("Importing {} fault log descriptions into MongoDB…", descriptions.size());
        repository.save(descriptions);
        LOGGER.info("Successfully imported {} fault log descriptions.", repository.count());
    }

    /**
     * Reads a file {@code faultLogDescriptions.csv} from the class path and parses it into objects
     */
    public static List<FaultLogDescription> readFaultLogDescriptions() throws Exception {

        Resource resource;
        if (overrideDescriptions != null) {
            resource = new FileSystemResource(overrideDescriptions);
            LOGGER.info("Overriding Fault Log Description List with: " + overrideDescriptions);
        } else {
            resource =  new ClassPathResource(defaultDescriptions);
            LOGGER.info("Reloading Fault Log Description List from: " + defaultDescriptions);
        }
        Scanner scanner = new Scanner(resource.getInputStream());
        String line = scanner.nextLine();
        scanner.close();

        FlatFileItemReader<FaultLogDescription> itemReader = new FlatFileItemReader<FaultLogDescription>();
        itemReader.setResource(resource);

        // DelimitedLineTokenizer defaults to comma as its delimiter
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(line.split(","));
        tokenizer.setStrict(false);

        DefaultLineMapper<FaultLogDescription> lineMapper = new DefaultLineMapper<FaultLogDescription>();
        lineMapper.setFieldSetMapper(fields -> {
            FaultLogDescription description = new FaultLogDescription();
            description.setControllerType(fields.readString("controllerType"));
            description.setCode(fields.readInt("code"));
            description.setDescription(fields.readString("description"));
            description.setSeverity(FaultLogSeverity.valueOf(fields.readString("severity")));
            return description;
        });

        lineMapper.setLineTokenizer(tokenizer);
        itemReader.setLineMapper(lineMapper);
        itemReader.setRecordSeparatorPolicy(new DefaultRecordSeparatorPolicy());
        itemReader.setLinesToSkip(1);
        itemReader.open(new ExecutionContext());

        List<FaultLogDescription> descriptions = new ArrayList<>();
        FaultLogDescription faultLogDescription = null;
        do {
            faultLogDescription = itemReader.read();
            if (faultLogDescription != null) {
                descriptions.add(faultLogDescription);
            }
        } while (faultLogDescription != null);

        return descriptions;
    }
}
