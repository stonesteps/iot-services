package com.bwg.iot.job_scheduling.job;

import com.bwg.iot.SpaCommandRepository;
import com.bwg.iot.job_scheduling.JobSchedulingComponent;
import com.bwg.iot.model.Recipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;

import java.util.Date;

/**
 * Builds a job and runs it.
 */
public class JobTask implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(JobTask.class);

    private final JobSchedulingComponent jobSchedulingComponent;
    private final SpaCommandRepository spaCommandRepository;
    private final StepBuilderFactory stepBuilderFactory;
    private final JobBuilderFactory jobBuilderFactory;
    private final JobLauncher jobLauncher;
    private final Recipe recipe;

    public JobTask(final JobSchedulingComponent jobSchedulingComponent, final SpaCommandRepository spaCommandRepository,
                   final StepBuilderFactory stepBuilderFactory, final JobBuilderFactory jobBuilderFactory,
                   final JobLauncher jobLauncher, final Recipe recipe) {
        this.jobSchedulingComponent = jobSchedulingComponent;
        this.spaCommandRepository = spaCommandRepository;
        this.stepBuilderFactory = stepBuilderFactory;
        this.jobBuilderFactory = jobBuilderFactory;
        this.jobLauncher = jobLauncher;
        this.recipe = recipe;
    }

    @Override
    public void run() {
        LOG.info("Building a job for recipe {}", recipe.get_id());
        // build a job
        final Job job = jobBuilderFactory.get("recipeJob" + recipe.get_id())
                .incrementer(new RunIdIncrementer())
                .flow(runRecipeStep(recipe))
                .end()
                .build();

        final JobParameters params = new JobParametersBuilder().addDate("date", new Date()).toJobParameters();

        try {
            // and run it
            LOG.info("Starting a job for recipe {}", recipe.get_id());
            jobLauncher.run(job, params);
        } catch (final Exception e) {
            LOG.error("Error running a job", e);
        }
    }

    private Step runRecipeStep(final Recipe recipe) {
        return stepBuilderFactory.get("runRecipeStep")
                .<Recipe, Recipe>chunk(1)
                .reader(new SingleItemReader<Recipe>(recipe))
                .processor(new RecipeRunner(jobSchedulingComponent, spaCommandRepository))
                .writer(new RecipeEventLogsWriter())
                .listener(new JobCompletionNotificationListener())
                .build();
    }
}
