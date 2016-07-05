package com.bwg.iot.job_scheduling.job;

import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.support.CronSequenceGenerator;

import java.util.Date;
import java.util.TimeZone;

/**
 * Created by holow on 7/1/2016.
 */
public final class CronWithStartEndDatesTrigger implements Trigger {

    private final Date startDate;
    private final Date endDate;
    private final String expression;

    private final CronSequenceGenerator sequenceGenerator;

    public CronWithStartEndDatesTrigger(final Date startDate, final Date endDate, final String expression) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.expression = expression;
        this.sequenceGenerator = new CronSequenceGenerator(expression);
    }

    public CronWithStartEndDatesTrigger(final Date startDate, final Date endDate, final String expression, final TimeZone timeZone) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.expression = expression;
        this.sequenceGenerator = new CronSequenceGenerator(expression, timeZone);
    }

    @Override
    public Date nextExecutionTime(TriggerContext triggerContext) {
        Date date = triggerContext.lastCompletionTime();
        if (date != null) {
            Date scheduled = triggerContext.lastScheduledExecutionTime();
            if (scheduled != null && date.before(scheduled)) {
                // Previous task apparently executed too early...
                // Let's simply use the last calculated execution time then,
                // in order to prevent accidental re-fires in the same second.
                date = scheduled;
            }
        } else {
            date = new Date();
        }
        Date nextDate = null;
        if (startDate != null && date.before(startDate)) nextDate = this.sequenceGenerator.next(startDate);
        else nextDate = this.sequenceGenerator.next(date);
        if (endDate != null && nextDate.after(endDate)) nextDate = null;

        return nextDate;
    }
}
