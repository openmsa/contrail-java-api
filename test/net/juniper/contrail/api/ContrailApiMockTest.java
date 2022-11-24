/*
 *  * Copyright (c) 2013 Juniper Networks, Inc. All rights reserved.
 *   */
package net.juniper.contrail.api;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.juniper.contrail.api.types.Domain;
import net.juniper.contrail.api.types.FloatingIp;
import net.juniper.contrail.api.types.InstanceIp;
import net.juniper.contrail.api.types.NetworkIpam;
import net.juniper.contrail.api.types.NetworkPolicy;
import net.juniper.contrail.api.types.Project;
import net.juniper.contrail.api.types.ServiceInstance;
import net.juniper.contrail.api.types.VirtualMachine;
import net.juniper.contrail.api.types.VirtualNetwork;

public class ContrailApiMockTest {
	public static ApiTestCommon _apiTest;
	private static final Logger s_logger = LoggerFactory.getLogger(ApiConnectorImpl.class);

	public static final String defaultConfigFile = "test/resources/default_config";

	@Before
	public void setUp() throws Exception {

		if (_apiTest != null) {
			return;
		}

		initDefaultConfig();
		final ApiConnector api = new ApiConnectorMock(null, 0);
		((ApiConnectorMock) api).dumpConfig(VirtualNetwork.class);
		_apiTest = new ApiTestCommon(api);

	}

	public void initDefaultConfig() throws Exception {
		final int port = ApiTestCommon.findFreePort();
		ApiTestCommon.launchContrailServer(port);
		s_logger.debug("initDefaultConfig: test api server launched <localhost" + ", " + port + ">");
		final ApiConnector api = ApiConnectorFactory.build(URI.create("http://localhost:" + port));

		final Class<? extends ApiObjectBase>[] vncClasses = new Class[] {
				Domain.class,
				VirtualNetwork.class,
				VirtualMachine.class,
				NetworkIpam.class,
				InstanceIp.class,
				ServiceInstance.class,
				FloatingIp.class,
				NetworkPolicy.class,
				Project.class,
				Port.class
		};

		final HashMap<Class<? extends ApiObjectBase>, List<HashMap<String, ApiObjectBase>>> map = new HashMap<>();
		for (final Class<? extends ApiObjectBase> cls : vncClasses) {
			final List<? extends ApiObjectBase> vncList = api.list(cls, null);
			final List<HashMap<String, ApiObjectBase>> objList = new ArrayList<>();
			final HashMap<String, ApiObjectBase> uuidMap = new HashMap<>();
			final HashMap<String, ApiObjectBase> fqnMap = new HashMap<>();
			objList.add(uuidMap);
			objList.add(fqnMap);
			for (final ApiObjectBase obj : vncList) {
				api.read(obj);
				uuidMap.put(obj.getUuid(), obj);
				fqnMap.put(StringUtils.join(obj.getQualifiedName(), ':'), obj);
			}
			map.put(cls, objList);
		}

		final FileOutputStream fout = new FileOutputStream(defaultConfigFile);
		final ObjectOutputStream objOut = new ObjectOutputStream(fout);
		objOut.writeObject(map);
		objOut.close();
	}

	@After
	public void tearDown() throws Exception {
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
