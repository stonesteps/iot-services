package com.bwg.iot.job_scheduling;

import com.bwg.iot.model.Recipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 * Responsible for scheduling and rescheduling jobs.
 */
@Component
public class JobSchedulingComponent extends AbstractMongoEventListener<Recipe> {

    private static final Logger LOG = LoggerFactory.getLogger(JobSchedulingComponent.class);

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private MongoTemplate mongoTemplate;

    private Map<String, ScheduledFuture<?>> scheduledRecipes;

    @PostConstruct
    public void init() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

        mongoTemplate.stream(new Query(), Recipe.class).forEachRemaining(recipe -> {
            rescheduleRecipe(recipe);
        });
    }

    private void rescheduleRecipe(final Recipe recipe) {

        if (scheduledRecipes.get(recipe.get_id()) != null) {
            // cancel any scheduled task and reshedule it
            scheduledRecipes.get(recipe.get_id()).cancel(false);
        }

        final Trigger trigger = new CronWithStartEndDatesTrigger(
                recipe.getSchedule().getStartDate(),
                recipe.getSchedule().getEndDate(),
                recipe.getSchedule().getCronExpression(),
                recipe.getSchedule().getTimeZone());

        ScheduledFuture<?> scheduledRecipe = taskScheduler.schedule(new Runnable() {
            public void run() {
                final Job job = jobBuilderFactory.get("recipeJob")
                        .incrementer(new RunIdIncrementer())
                        .flow(runRecipe(recipe))
                        .end()
                        .build();

                final String dateParam = new Date().toString();
                JobParameters param = new JobParametersBuilder().addString("date", dateParam).toJobParameters();
                try {
                    jobLauncher.run(job, param);
                } catch (final Exception e) {
                    LOG.error("Error running a job", e);
                }
            }
        }, trigger);

        scheduledRecipes.put(recipe.get_id(), scheduledRecipe);
    }

    public Step runRecipe(final Recipe recipe) {
        return stepBuilderFactory.get("runRecipe")
                .<Recipe, Recipe>chunk(1)
                .reader(singleRecipeReader(recipe))
                .processor(recipeRunner())
                .listener(new JobCompletionNotificationListener())
                .build();
    }

    private ItemReader<Recipe> singleRecipeReader(final Recipe recipe) {
        final List<Recipe> list = new ArrayList<>();
        list.add(recipe);
        return new ItemReader<Recipe>() {
            int index = 0;

            @Override
            public Recipe read() throws Exception {
                if (index >= list.size()) return null;
                else return list.get(index++);
            }
        };
    }

    private ItemProcessor<Recipe, Recipe> recipeRunner() {
        return new ItemProcessor<Recipe, Recipe>() {
            @Override
            public Recipe process(Recipe recipe) throws Exception {
                LOG.info("Running a recipe {}", recipe);
                return recipe;
            }
        };
    }

    @Override
    public void onAfterSave(AfterSaveEvent<Recipe> event) {
        super.onAfterSave(event);
        final Recipe recipe = event.getSource();
        rescheduleRecipe(recipe);
    }
}
