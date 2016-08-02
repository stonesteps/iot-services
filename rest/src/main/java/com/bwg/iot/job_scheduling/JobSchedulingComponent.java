package com.bwg.iot.job_scheduling;

import com.bwg.iot.SpaCommandRepository;
import com.bwg.iot.job_scheduling.job.CronWithStartEndDatesTrigger;
import com.bwg.iot.job_scheduling.job.JobTask;
import com.bwg.iot.model.JobSchedule;
import com.bwg.iot.model.Recipe;
import com.bwg.iot.model.SpaCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Responsible for scheduling and rescheduling jobs.
 */
@Component
public class JobSchedulingComponent extends AbstractMongoEventListener<Recipe> {

    private static final Logger LOG = LoggerFactory.getLogger(JobSchedulingComponent.class);

    @Autowired
    private SpaCommandRepository spaCommandRepository;

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

    @Value("${jobRecipeSpaTurnOffAfterDurationMins:15}")
    private int spaTurnOffDuration;

    private final Map<String, ScheduledFuture<?>> scheduledRecipes = new HashMap<>();
    private final Map<String, JobSchedule> scheduledRecipesSchedules = new HashMap<>();

    // for turn of spa commands
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);

    @PostConstruct
    public void init() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        mongoTemplate.stream(new Query(), Recipe.class).forEachRemaining(recipe -> {
            scheduleRecipe(recipe);
        });
    }

    public void scheduleRecipe(final Recipe recipe) {

        LOG.info("Scheduling recipe {}, id: {}", recipe.getName(), recipe.get_id());

        final String recipeId = recipe.get_id();

        final JobSchedule currentSchedule = scheduledRecipesSchedules.get(recipeId);
        final boolean scheduleChanged = currentSchedule == null || (!currentSchedule.equals(recipe.getSchedule()));

        final ScheduledFuture<?> recipeTask = scheduledRecipes.get(recipeId);
        if (scheduleChanged && recipeTask != null) {
            // cancel already scheduled task and reshedule it
            LOG.info("Cancelling existing scheduled task");
            recipeTask.cancel(false);
        }
        scheduledRecipes.remove(recipeId);
        scheduledRecipesSchedules.remove(recipeId);

        if (recipe.getSchedule() != null && recipe.getSchedule().isEnabled()) {
            final Trigger trigger = new CronWithStartEndDatesTrigger(
                    recipe.getSchedule().getStartDate(),
                    recipe.getSchedule().getEndDate(),
                    recipe.getSchedule().getCronExpression(),
                    recipe.getSchedule().getTimeZone());

            final ScheduledFuture<?> scheduledRecipe = taskScheduler.schedule(
                    new JobTask(this, spaCommandRepository, stepBuilderFactory, jobBuilderFactory, jobLauncher, recipe), trigger);
            scheduledRecipes.put(recipeId, scheduledRecipe);
            scheduledRecipesSchedules.put(recipeId, recipe.getSchedule());
        }
    }

    public void scheduleSpaTurnOff(final String spaId) {
        executorService.schedule(() -> {
            LOG.info("Scheduling spa {} to turn off", spaId);
            final SpaCommand turnOffSpaCommand = SpaCommand.newInstanceNoHeat();
            turnOffSpaCommand.setSpaId(spaId);
            spaCommandRepository.save(turnOffSpaCommand);
        }, spaTurnOffDuration, TimeUnit.MINUTES);
    }

    /**
     * Listens to save events on recipe entities. Works only in same VM. If this listener is to be taken out to another
     * VM then different mechanism is to be used.
     *
     * @param event
     */
    @Override
    public void onAfterSave(final AfterSaveEvent<Recipe> event) {
        super.onAfterSave(event);
        final Recipe recipe = event.getSource();
        if (recipe != null) {
            LOG.info("JobSchedule update: {}",recipe.getName());
            scheduleRecipe(recipe);
        } else {
            LOG.info("JobSchedule update. - Null Recipe");
        }
    }
}
