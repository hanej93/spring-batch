package io.springbatch.springbatchlecture.lecture.section15_batch_core.scheduler;

import java.util.HashMap;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApiJobRunner extends JobRunner {

	private final Scheduler scheduler;

	@Override
	protected void doRun(ApplicationArguments args) {
		JobDetail jobDetail = buildJobDetail(ApiSchJob.class, "apiJob", "batch", new HashMap());
		Trigger trigger = buildJobTrigger("0/15 * * * * ?");

		try {
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
}
