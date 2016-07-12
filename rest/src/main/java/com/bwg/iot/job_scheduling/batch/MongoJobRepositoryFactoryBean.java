package com.bwg.iot.job_scheduling.batch;


import org.springframework.batch.core.repository.dao.ExecutionContextDao;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.batch.core.repository.dao.StepExecutionDao;
import org.springframework.batch.core.repository.support.AbstractJobRepositoryFactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
public class MongoJobRepositoryFactoryBean extends AbstractJobRepositoryFactoryBean implements InitializingBean {

    @Autowired
    private MongoExecutionContextDao executionContextDao;

    @Autowired
    private MongoJobExecutionDao jobExecutionDao;

    @Autowired
    private MongoJobInstanceDao jobInstanceDao;

    @Autowired
    private MongoStepExecutionDao stepExecutionDao;

    @Override
    @Autowired
    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        super.setTransactionManager(transactionManager);
    }

    @Override
    protected JobInstanceDao createJobInstanceDao() throws Exception {
        return jobInstanceDao;
    }

    @Override
    protected JobExecutionDao createJobExecutionDao() throws Exception {
        return jobExecutionDao;
    }

    @Override
    protected StepExecutionDao createStepExecutionDao() throws Exception {
        return stepExecutionDao;
    }

    @Override
    protected ExecutionContextDao createExecutionContextDao() throws Exception {
        return executionContextDao;
    }
}
