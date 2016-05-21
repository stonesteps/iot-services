package com.bwg.iot;

import com.bwg.iot.model.Material;
import com.bwg.iot.model.Oem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.separator.DefaultRecordSeparatorPolicy;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Initializes the faultLogDescription collection.
 *
 * Created by triton on 5/20/16.
 */
@Component
public class MaterialInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(MaterialInitializer.class);

    @Autowired
    public MaterialInitializer(MaterialRepository repository, MongoOperations operations) throws Exception {

        if (repository.count() != 0) {
            return;
        }

        if (operations.count( new Query(), "oem") == 0) {
            // TODO: load oems if empty
            LOGGER.warn("Unable to initial Material collection, OEM collection is empty");
            return;
        }

        List<Material> materials = readMaterials(operations);
        LOGGER.info("Importing {} Materials into MongoDB…", materials.size());
        repository.save(materials);
        LOGGER.info("Successfully imported {} Materials.", repository.count());
    }

    /**
     * Reads a file {@code materials.csv} from the class path and parses it into objects
     */
    public static List<Material> readMaterials(MongoOperations operations) throws Exception {

        ClassPathResource resource = new ClassPathResource("db/material.csv");
        Scanner scanner = new Scanner(resource.getInputStream());
        String line = scanner.nextLine();
        scanner.close();

        FlatFileItemReader<Material> itemReader = new FlatFileItemReader<Material>();
        itemReader.setResource(resource);

        // DelimitedLineTokenizer defaults to comma as its delimiter
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(line.split(","));
        tokenizer.setStrict(false);

        DefaultLineMapper<Material> lineMapper = new DefaultLineMapper<Material>();
        lineMapper.setFieldSetMapper(fields -> {
            Material material = new Material();

            List<Oem> oems = operations.find(query(where("customerNumber").is(fields.readInt("customerNumber"))), Oem.class);

            String oemId;
            if (oems.isEmpty()) {
                oemId = "BOGUS:" + fields.readString("customerNumber");
            } else {
                oemId = oems.get(0).get_id();
            }

            material.setOemId(oemId);
            material.setComponentType(fields.readString("componentType"));
            material.setBrandName(fields.readString("brandName"));
            material.setDescription(fields.readString("description"));
            material.setSku(fields.readString("sku"));
            material.setAlternateSku(fields.readString("alternateSku"));
            if (!StringUtils.isEmpty(fields.readString("warrantyDays"))) {
                material.setWarrantyDays(fields.readInt("warrantyDays"));
            }
            material.setUploadDate(new Date());
            return material;
        });

        lineMapper.setLineTokenizer(tokenizer);
        itemReader.setLineMapper(lineMapper);
        itemReader.setRecordSeparatorPolicy(new DefaultRecordSeparatorPolicy());
        itemReader.setLinesToSkip(1);
        itemReader.open(new ExecutionContext());

        List<Material> materials = new ArrayList<>();
        Material material = null;
        int skipCount = 0;
        do {
            material = itemReader.read();
            if (material != null && !material.getOemId().contains("BOGUS")) {
                materials.add(material);
            } else {
                skipCount++;
                String custNum = (material == null || material.getOemId() == null)
                        ? "null" : material.getOemId().substring(material.getOemId().indexOf(":"));
                LOGGER.warn("Skipping Material Entry, no OEM exists with id: " + custNum);
            }
        } while (material != null);

        return materials;
    }
}
