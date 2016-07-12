package com.bwg.iot.job_scheduling;

import com.bwg.iot.IotServicesApplication;
import com.bwg.iot.RecipeRepository;
import com.bwg.iot.SpaCommandRepository;
import com.bwg.iot.model.JobSchedule;
import com.bwg.iot.model.Recipe;
import com.bwg.iot.model.SpaCommand;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Basic tests for job scheduling.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {IotServicesApplication.class, BatchConfiguration.class})
@WebAppConfiguration
public class JobSchedulingComponentTest {
    
    @Autowired
    private SpaCommandRepository spaCommandRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Before
    @After
    public void initAndCleanup() {
        spaCommandRepository.deleteAll();
        recipeRepository.deleteAll();
    }

    @Test
    public void testRecipeSchedule() throws InterruptedException {
        // "0/30 * * * * ?"
        final Recipe recipe = new Recipe();
        recipe.setSpaId("111");
        recipe.setName("test recipe");
        recipe.setSchedule(buildSchedule("0/30 * * * * ?"));
        recipe.setSettings(buildEmptyCommand("111"));
        recipeRepository.save(recipe);
        // recipe is being scheduled upon save

        // sleep 40s - job should fire during this time
        Thread.sleep(40000);

        // there should be 1 command at this point
        final List<SpaCommand> foundCommands = spaCommandRepository.findAll();
        Assert.assertEquals(1, foundCommands.size());
    }

    private List<SpaCommand> buildEmptyCommand(final String spaId) {
        final List<SpaCommand> commands = new ArrayList<>(1);
        final SpaCommand emptyCommand = SpaCommand.newInstanceNoHeat();
        emptyCommand.setSpaId("111");
        commands.add(emptyCommand);
        return commands;
    }

    private JobSchedule buildSchedule(final String cronExpression) {
        final JobSchedule schedule = new JobSchedule();
        schedule.setCronExpression(cronExpression);
        schedule.setEnabled(true);
        return schedule;
    }
}
