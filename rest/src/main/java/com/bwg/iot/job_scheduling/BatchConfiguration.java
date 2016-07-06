package com.bwg.iot.job_scheduling;

import com.bwg.iot.job_scheduling.batch.MongoJobRepositoryFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(BatchConfiguration.class);

    @Autowired
    private MongoJobRepositoryFactoryBean mongoJobRepositoryFactoryBean;

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager() {
        return new ResourcelessTransactionManager();
    }

    @Bean(name = "jobRepository")
    public JobRepository jobRepository() {
        try {
            return mongoJobRepositoryFactoryBean.getObject();
        } catch (Exception e) {
            LOG.error("Error creating jobRepository bean", e);
        }
        return null;
    }

    @Bean(name = "jobLauncher")
    public JobLauncher jobLauncher() {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository());
        return jobLauncher;
    }

    @Bean(name = "jobRegistry")
    public JobRegistry jobRegistry() {
        return new MapJobRegistry();
    }

    @Bean(name = "jobBuilders")
    public JobBuilderFactory jobBuilders() {
        return new JobBuilderFactory(jobRepository());
    }

    @Bean(name = "stepBuilders")
    public StepBuilderFactory stepBuilders() {
        return new StepBuilderFactory(jobRepository(), transactionManager());
    }

    @Bean(name = "jobBuilderFactory")
    public JobBuilderFactory jobBuilderFactory() {
        return new JobBuilderFactory(jobRepository());
    }

    @Bean(name = "stepBuilderFactory")
    public StepBuilderFactory stepBuilderFactory() {
        return new StepBuilderFactory(jobRepository(), transactionManager());
    }

    @Bean(name = "taskScheduler")
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler();
    }

}
