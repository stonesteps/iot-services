package com.bwg.iot.job_scheduling.job;

import com.bwg.iot.model.Recipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

/**
 * Writes events for processed recipes.
 */
public class RecipeEventLogsWriter implements ItemWriter<Recipe> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeEventLogsWriter.class);

    @Override
    public void write(final List<? extends Recipe> items) throws Exception {
        // FIXME what event loss shall be stored here?
    }
}
