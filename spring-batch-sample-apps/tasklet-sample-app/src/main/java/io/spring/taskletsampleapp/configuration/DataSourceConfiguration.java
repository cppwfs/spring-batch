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

package io.spring.taskletsampleapp.configuration;

import java.util.Locale;

import javax.sql.DataSource;

import org.springframework.batch.support.DatabaseType;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

/**
 * Configure the data source for the batch application.
 *
 * @author Glenn Renfro
 */
@Configuration
public class DataSourceConfiguration {

	@Bean
	public DataSource dataSource(ApplicationContext context) {

		Environment env = context.getEnvironment();
		String dataSourceUrl = env.getProperty("spring.datasource.url");
		String  driverClassName = env.getProperty("spring.datasource.driverClassName");
		String userName = env.getProperty("spring.datasource.username");
		String password = env.getProperty("spring.datasource.password");

		if (dataSourceUrl != null && driverClassName != null && userName != null &&
				password != null) {
			DriverManagerDataSource dataSource = new DriverManagerDataSource();
			dataSource.setDriverClassName(driverClassName);
			dataSource.setUrl(dataSourceUrl);
			dataSource.setUsername(userName);
			dataSource.setPassword(password);
			return dataSource;
		}
		else {
			return new EmbeddedDatabaseBuilder()
					.setType(EmbeddedDatabaseType.H2)
					.addScript("/org/springframework/batch/core/schema-h2.sql")
					.build();
		}
	}

	@Conditional(DatabaseCondition.class)
	@Bean
	public DataSourceInitializer dataSourceInitializer(final DataSource dataSource)  throws Exception {
		final DataSourceInitializer initializer = new DataSourceInitializer();
		initializer.setDataSource(dataSource);
		initializer.setDatabasePopulator(databasePopulator(dataSource));
		return initializer;
	}

	private DatabasePopulator databasePopulator(DataSource dataSource) throws Exception {
		DatabaseType databaseType = DatabaseType.fromMetaData(dataSource);
		Resource schemaScript = new ClassPathResource("org/springframework/batch/core/schema-" +
				databaseType.getProductName().toLowerCase(Locale.ROOT) + ".sql");
		final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(schemaScript);
		populator.setContinueOnError(true);
		return populator;
	}
}
