package io.spring.taskletsampleapp;

import io.spring.taskletsampleapp.configuration.BatchConfiguration;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.jdbc.JdbcTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = BatchConfiguration.class)
public class TaskletSampleAppTest {
	private JdbcTemplate jdbcTemplate;

	@Autowired
	ApplicationContext context;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Test
	void contextLoads() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
		Job job = context.getBean("job1", Job.class);
		context.getBean(JobLauncher.class).run(job, new JobParameters());
		assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "BATCH_JOB_INSTANCE")).isEqualTo(1);

		job = context.getBean("job2", Job.class);
		context.getBean(JobLauncher.class).run(job, new JobParameters());
		assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "BATCH_JOB_INSTANCE")).isEqualTo(2);
	}

}
