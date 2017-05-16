/*
 * Copyright 2017 the original author or authors.
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

package org.springframework.batch.item.data.builder;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.data.repository.CrudRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;

/**
 * @author Glenn Renfro
 */
public class RepositoryItemWriterBuilderTests {
	@Mock
	private CrudRepository<String, Serializable> repository;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testNullRepository() throws Exception {
		try {
			new RepositoryItemWriterBuilder<String>().methodName("save").build();

			fail("IllegalArgumentException should have been thrown");
		}
		catch (IllegalArgumentException iae) {
			assertEquals("IllegalArgumentException message did not match the expected result.",
					"repository is required.", iae.getMessage());
		}
	}

	@Test
	public void testEmptyMethodName() throws Exception {
		try {
			new RepositoryItemWriterBuilder<String>().repository(this.repository).build();

			fail("IllegalArgumentException should have been thrown");
		}
		catch (IllegalArgumentException iae) {
			assertEquals("IllegalArgumentException message did not match the expected result.",
					"methodName is required.", iae.getMessage());
		}
	}

	@Test
	public void testWriteItems() throws Exception {
		RepositoryItemWriter<String> writer = new RepositoryItemWriterBuilder<String>().methodName("save")
				.repository(this.repository).build();

		List<String> items = Collections.singletonList("foo");

		writer.write(items);

		verify(this.repository).save("foo");
	}
}
