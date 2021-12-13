/*
 * Copyright 2021 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.spring.taskletsampleapp;


import io.spring.taskletsampleapp.configuration.BatchConfiguration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Base application for launching the Tasklet Batch Jobs.
 *
 * @author Glenn Renfro
 */
public class TaskletSampleApp {

	public static void main(String[] args) throws Exception{
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BatchConfiguration.class);
		String jobNames[] = applicationContext.getBeanNamesForType(Job.class);
		for(String jobName : jobNames) {
			Job job = applicationContext.getBean(jobName, Job.class);
			applicationContext.getBean(JobLauncher.class).run(job, new JobParameters());
		}
		System.out.println("Done");
	}

}
