package com.bwg.iot;

import com.bwg.iot.model.Material;
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
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;

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

        List<Material> materials = readMaterials();
        LOGGER.info("Importing {} Materials into MongoDB…", materials.size());
        repository.save(materials);
        LOGGER.info("Successfully imported {} Materials.", repository.count());
    }

    /**
     * Reads a file {@code materials.csv} from the class path and parses it into objects
     */
    public static List<Material> readMaterials() throws Exception {

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
            List<String> oems = Arrays.asList(fields.readString("oemId"));
            material.setOemId(oems);
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
        do {
            material = itemReader.read();
            if (material != null) {
                materials.add(material);
            }
        } while (material != null);

        return materials;
    }
}
