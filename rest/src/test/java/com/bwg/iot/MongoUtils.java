package com.bwg.iot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by triton on 5/20/16.
 */
public class MongoUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationConfig.class);

    public static void ImportCollectionCsv(String db, String collection, String importFile) {
        Runtime r = Runtime.getRuntime();
        Process p = null;
        String command = "mongoimport --db " + db + "  --collection " + collection
                + " --type csv --headerline --ignoreBlanks --file " + importFile;

        try {
            p = r.exec(command);
            LOGGER.info("Reading " + importFile + " into collection " + collection);
        } catch (Exception e) {
            LOGGER.error("Error executing " + command + e.toString());
        }
    }
    public static void ImportCollectionJson(String db, String collection, String importFile) {
        Runtime r = Runtime.getRuntime();
        Process p = null;
        String command = "mongoimport --db " + db + "  --collection " + collection
                + " --file " + importFile;

        try {
            p = r.exec(command);
            LOGGER.info("Reading " + importFile + " into collection " + collection);
        } catch (Exception e) {
            LOGGER.error("Error executing " + command + e.toString());
        }
    }
}
