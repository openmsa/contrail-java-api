/*
 *  * Copyright (c) 2013 Juniper Networks, Inc. All rights reserved.
 *   */
package net.juniper.contrail.api;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContrailApiTest {
	public static ApiTestCommon _apiTest;
	private static final Logger s_logger = LoggerFactory.getLogger(ApiConnectorImpl.class);

	@Before
	public void setUp() throws Exception {

		if (_apiTest != null) {
			return;
		}

		final int port = ApiTestCommon.findFreePort();
		ApiTestCommon.launchContrailServer(port);
		s_logger.debug("test api server launched <localhost" + ", " + port + ">");
		_apiTest = new ApiTestCommon(ApiConnectorFactory.build("localhost", port));
	}

	@After
	public void tearDown() throws Exception {
		_apiTest.tearDown();
	}

	@Test
	public void testNetwork() {
		_apiTest.testNetwork();
	}

	@Test
	public void testDeserializeReferenceList() {
		_apiTest.testDeserializeReferenceList();
	}

	@Test
	public void testAddressAllocation() {
		_apiTest.testAddressAllocation();
	}
}
