package io.springbatch.springbatchlecture.lecture.section15_batch_core.scheduler;

import java.util.HashMap;
import java.util.Map;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

public abstract class JobRunner implements ApplicationRunner {

	@Override
	public void run(ApplicationArguments args) throws Exception {
		doRun(args);
	}

	protected abstract void doRun(ApplicationArguments args);

	public Trigger buildJobTrigger(String scheduleExp) {
		return TriggerBuilder.newTrigger()
			.withSchedule(CronScheduleBuilder.cronSchedule(scheduleExp)).build();
	}

	public JobDetail buildJobDetail(Class job, String name, String group, Map params) {
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.putAll(params);

		return JobBuilder.newJob(job).withIdentity(name, group)
			.usingJobData(jobDataMap)
			.build();
	}
}
