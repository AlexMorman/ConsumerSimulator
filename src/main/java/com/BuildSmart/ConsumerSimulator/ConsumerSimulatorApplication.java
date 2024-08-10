package com.BuildSmart.ConsumerSimulator;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class ConsumerSimulatorApplication
{
	public static void main(String[] args) throws SchedulerException
	{
		// I have added an additional comment
		Logger logger = Logger.getLogger(SimpleJob.class.getName());
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		Scheduler scheduler = schedulerFactory.getScheduler();
		scheduler.start();

		Properties prop = new Properties();
		String fileName = "app.config";
		try (FileInputStream fis = new FileInputStream(fileName)) {
			prop.load(fis);
		} catch (Exception e) {
			logger.severe("ERROR: Failed to read config file: " + e.getMessage());
		}

		JobDetail job = JobBuilder.newJob(SimpleJob.class)
			.withIdentity("myJob", "group1")
			.usingJobData("target", prop.getProperty("app.target"))
			.build();

		Trigger trigger = TriggerBuilder.newTrigger()
			.withIdentity("myTrigger", "group1")
			.startNow()
			.withSchedule(SimpleScheduleBuilder.simpleSchedule()
			.withIntervalInSeconds(30)
			.repeatForever())
			.build();


		scheduler.scheduleJob(job, trigger);

	}

}
