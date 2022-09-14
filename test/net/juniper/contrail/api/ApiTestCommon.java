/*
 *  * Copyright (c) 2013 Juniper Networks, Inc. All rights reserved.
 *   */
package net.juniper.contrail.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.UUID;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteResultHandler;
import org.apache.commons.exec.PumpStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.juniper.contrail.api.types.InstanceIp;
import net.juniper.contrail.api.types.IpamSubnetType;
import net.juniper.contrail.api.types.NetworkIpam;
import net.juniper.contrail.api.types.NetworkPolicy;
import net.juniper.contrail.api.types.Project;
import net.juniper.contrail.api.types.SubnetType;
import net.juniper.contrail.api.types.VirtualMachine;
import net.juniper.contrail.api.types.VirtualMachineInterface;
import net.juniper.contrail.api.types.VirtualNetwork;
import net.juniper.contrail.api.types.VnSubnetsType;

public class ApiTestCommon {
	public static ApiConnector _api;
	private static final Logger s_logger = LoggerFactory.getLogger(ApiConnectorImpl.class);

	ApiTestCommon(final ApiConnector api) {
		_api = api;
	}

	public static int findFreePort() throws Exception {
		int port;
		final ServerSocket socket = new ServerSocket(0);
		port = socket.getLocalPort();
		socket.close();
		return port;
	}

	public static CommandLine buildServerLaunchCmd(final int port) {
		final CommandLine cmdLine = new CommandLine("fab");
		cmdLine.addArgument("-f");
		cmdLine.addArgument("fab_tasks.py");
		cmdLine.addArgument("run_api_srv:listen_port=" + port);
		return cmdLine;
	}

	public static void launchContrailServer(final int port) throws Exception {
		try {
			final DefaultExecutor exec = new DefaultExecutor();
			final int exitValues[] = { 1 };
			exec.setExitValues(exitValues);

			final String workingDir = System.getProperty("user.dir");
			final String path = workingDir + "/../../config/api-server/tests/";
			final File f = new File(path);
			exec.setWorkingDirectory(f);
			exec.setStreamHandler(new PumpStreamHandler(new ByteArrayOutputStream()));
			final CommandLine cmd = buildServerLaunchCmd(port);
			final ExecuteResultHandler handler = null;
			exec.execute(cmd, handler);
			/* sleep 5 seconds for server to get started */
			Thread.sleep(5000);
		} catch (final Exception e) {
			s_logger.debug("", e);
			final String cause = e.getMessage();
			if (cause.equals("python: not found")) {
				System.out.println("No python interpreter found.");
			}
			throw e;
		}
	}

	public void setUp() throws Exception {

		final String hostname = "localhost";
		final int port = findFreePort();
		launchContrailServer(port);
		s_logger.debug("test api server launched <" + hostname + ", " + port + ">");
	}

	public void tearDown() throws Exception {
	}

	public void testNetwork() {
		final String uuid1 = UUID.randomUUID().toString();
		final VirtualNetwork net1 = new VirtualNetwork();
		net1.setName("test-network");
		net1.setUuid(uuid1);
		try {
			s_logger.info("create '<name=test-network, uuid=" +
					uuid1 + ">' Virtual Network");
			assertTrue(_api.create(net1).isSuccess());
		} catch (final IOException ex) {
			s_logger.warn("create test-network io exception " + ex.getMessage());
			fail(ex.getMessage());
		} catch (final Exception ex) {
			s_logger.warn("create test-network http exception " + ex.getMessage());
			fail(ex.getMessage());
		}

		final VirtualNetwork net2 = new VirtualNetwork();
		net2.setName("srv-id-assign");
		try {
			s_logger.info("create '<name=srv-id-assign, uuid=empty" +
					">' Virtual Network");
			assertTrue(_api.create(net2).isSuccess());
		} catch (final IOException ex) {
			s_logger.warn("create srv-id-assign exception " + ex.getMessage());
			fail(ex.getMessage());
		}

		assertNotNull(net2.getUuid());
		ApiObjectBase net3 = null;
		try {
			net3 = _api.findById(VirtualNetwork.class, net2.getUuid());
			assertNotNull(net3);
		} catch (final IOException ex) {
			fail(ex.getMessage());
		}
		if (null == net3) {
			throw new NullPointerException();
		}
		assertEquals(net2.getUuid(), net3.getUuid());

		try {
			final String uuid_1 = _api.findByName(VirtualNetwork.class, null, "test-network");
			assertNotNull(uuid_1);
			assertEquals(net1.getUuid(), uuid_1);

			final List<? extends ApiObjectBase> list = _api.list(VirtualNetwork.class, null);
			assertNotNull(list);
			assertTrue(list.size() >= 2);
		} catch (final IOException ex) {
			fail(ex.getMessage());
		}

		try {
			s_logger.info("delete '<name=test-network, uuid=" +
					uuid1 + ">' Virtual Network");
			_api.delete(net1);
			s_logger.info("delete '<name=srv-id-assign, uuid=" +
					net2.getUuid() + ">' Virtual Network");
			_api.delete(net2);
		} catch (final IOException ex) {
			fail(ex.getMessage());
		}
	}

	public void testDeserializeReferenceList() {
		final Project project = new Project();
		project.setName("testProject");
		project.setUuid(UUID.randomUUID().toString());
		try {
			assertTrue(_api.create(project).isSuccess());
		} catch (final IOException ex) {
			fail(ex.getMessage());
		}
		final NetworkPolicy policy = new NetworkPolicy();
		policy.setParent(project);
		policy.setName("testPolicy");
		try {
			assertTrue(_api.create(policy).isSuccess());
		} catch (final IOException ex) {
			fail(ex.getMessage());
		}
		try {
			assertTrue(_api.read(project).isSuccess());
		} catch (final IOException ex) {
			fail(ex.getMessage());
		}

		final List<ObjectReference<ApiPropertyBase>> policyList = project.getNetworkPolicys();
		assertTrue(policyList != null);
		assertTrue(policyList.size() != 0);
	}

	public void testAddressAllocation() {
		final VirtualNetwork net = new VirtualNetwork();
		net.setName("test");
		net.setUuid(UUID.randomUUID().toString());

		NetworkIpam ipam = null;
		try {
			// Find default-network-ipam
			final String ipam_id = _api.findByName(NetworkIpam.class, null, "default-network-ipam");
			assertNotNull(ipam_id);
			ipam = (NetworkIpam) _api.findById(NetworkIpam.class, ipam_id);
			assertNotNull(ipam);
		} catch (final IOException ex) {
			fail(ex.getMessage());
		}

		final VnSubnetsType subnet = new VnSubnetsType();
		subnet.addIpamSubnets(new IpamSubnetType(new SubnetType("10.0.1.0", 24), "10.0.1.254", null, UUID.randomUUID().toString(), false, null, null, false, null, null, net.getName() + "-subnet", 1));
		net.setNetworkIpam(ipam, subnet);

		final VirtualMachine vm = new VirtualMachine();
		vm.setName("aa01");
		try {
			assertTrue(_api.create(vm).isSuccess());
		} catch (final IOException ex) {
			fail(ex.getMessage());
		}

		final VirtualMachineInterface vmi = new VirtualMachineInterface();
		vmi.setParent(vm);
		vmi.setName("test-vmi");

		try {
			assertTrue(_api.create(vmi).isSuccess());
			assertTrue(_api.create(net).isSuccess());
		} catch (final IOException ex) {
			fail(ex.getMessage());
		}

		final InstanceIp ip_obj = new InstanceIp();
		ip_obj.setName(net.getName() + ":0");
		ip_obj.setVirtualNetwork(net);
		ip_obj.setVirtualMachineInterface(vmi);
		try {
			assertTrue(_api.create(ip_obj).isSuccess());
			// Must perform a GET in order to update the object contents.
			assertTrue(_api.read(ip_obj).isSuccess());
			assertNotNull(ip_obj.getAddress());

			_api.delete(ip_obj);
			_api.delete(net);

		} catch (final IOException ex) {
			fail(ex.getMessage());
		}
	}
}
