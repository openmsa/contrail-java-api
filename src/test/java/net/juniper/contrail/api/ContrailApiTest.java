/*
 *  * Copyright (c) 2013 Juniper Networks, Inc. All rights reserved.
 *   */
package net.juniper.contrail.api;

import java.net.URI;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContrailApiTest {
	public static ApiTestCommon _apiTest;
	private static final Logger s_logger = LoggerFactory.getLogger(ContrailApiTest.class);

	@BeforeEach
	public void setUp() throws Exception {

		if (_apiTest != null) {
			return;
		}

		final int port = ApiTestCommon.findFreePort();
		ApiTestCommon.launchContrailServer(port);
		s_logger.debug("test api server launched <localhost, {}>", port);
		_apiTest = new ApiTestCommon(ApiConnectorFactory.build(URI.create("http://localhost:" + port)));
	}

	@AfterEach
	public void tearDown() throws Exception {
		_apiTest.tearDown();
	}

	@Disabled
	@Test
	void testNetwork() {
		_apiTest.testNetwork();
	}

	@Disabled
	@Test
	void testDeserializeReferenceList() {
		_apiTest.testDeserializeReferenceList();
	}

	@Disabled
	@Test
	void testAddressAllocation() {
		_apiTest.testAddressAllocation();
	}
}
