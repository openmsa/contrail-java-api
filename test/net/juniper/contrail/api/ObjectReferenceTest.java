/*
 * Copyright (c) 2013 Juniper Networks, Inc. All rights reserved.
 */
package net.juniper.contrail.api;

import java.util.List;
import java.util.UUID;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import junit.framework.TestCase;
import net.juniper.contrail.api.types.IpamSubnetType;
import net.juniper.contrail.api.types.NetworkIpam;
import net.juniper.contrail.api.types.Project;
import net.juniper.contrail.api.types.SubnetType;
import net.juniper.contrail.api.types.VirtualMachineInterface;
import net.juniper.contrail.api.types.VirtualNetwork;
import net.juniper.contrail.api.types.VnSubnetsType;

public class ObjectReferenceTest extends TestCase {
	/**
	 * API server requires the attr element in an ObjectReference to be present,
	 * even when null.
	 */
	@Test
	public void testNullAttr() {
		final VirtualMachineInterface vmi = new VirtualMachineInterface();
		final VirtualNetwork vn = new VirtualNetwork();
		vn.setName("testnet");
		vn.setUuid(UUID.randomUUID().toString());
		vmi.setName("x-0");
		vmi.setVirtualNetwork(vn);
		final Project project = new Project();
		vmi.setParent(project);
		final String jsdata = ApiSerializer.serializeObject("virtual-machine-interface", vmi, null, null);
		assertNotSame(jsdata, -1, jsdata.indexOf("\"attr\":null"));
	}

	@Test
	public void testAttr() {
		final VirtualNetwork vn = new VirtualNetwork();
		vn.setName("testnet");
		final NetworkIpam ipam = new NetworkIpam();
		ipam.setName("testipam");
		final VnSubnetsType subnets = new VnSubnetsType();
		subnets.addIpamSubnets(new IpamSubnetType(new SubnetType("192.168.0.0", 24), "192.168.0.254", null, UUID.randomUUID().toString(), false, null, null, false, null, null, vn.getName() + "-subnet", 1));
		vn.setNetworkIpam(ipam, subnets);
		final String jsdata = ApiSerializer.serializeObject("virtual-network", vn, null, null);
		assertNotSame(jsdata, -1, jsdata.indexOf("192.168.0.0"));

		final JsonParser parser = new JsonParser();
		final JsonObject js_obj = parser.parse(jsdata).getAsJsonObject();
		final JsonElement element = js_obj.get("virtual-network");
		final VirtualNetwork result = (VirtualNetwork) ApiSerializer.deserialize(element.toString(), VirtualNetwork.class);
		final List<IpamSubnetType> iplist = result.getNetworkIpam().get(0).getAttr().getIpamSubnets();
		assertSame(1, iplist.size());
		assertEquals("192.168.0.0", iplist.get(0).getSubnet().getIpPrefix());
	}

	@Test
	public void testVoidReference() {
		final String voidref = "{\"network_policys\": [{\"to\": [\"default-domain\", \"testProject\", \"testPolicy\"], \"href\": \"http://localhost:53730/network-policy/4e4b0486-e56f-4bfe-8716-afc1a76ad106\", \"uuid\": \"4e4b0486-e56f-4bfe-8716-afc1a76ad106\"}]}";
		final JsonParser parser = new JsonParser();
		final JsonObject js_obj = parser.parse(voidref).getAsJsonObject();
		final JsonElement element = js_obj.get("network_policys");
		final JsonArray items = element.getAsJsonArray();
		final JsonElement item = items.get(0);
		final Gson json = ApiSerializer.getDeserializer();
		final ObjectReference<?> result = json.fromJson(item.toString(), ObjectReference.class);
		assertNotNull(result);
		assertEquals("testPolicy", result.getReferredName().get(2));
	}

	@Test
	/**
	 * API generator adds an "s" at the end of the children name. Thus
	 * "network-policy" becomes "network-policys".
	 */
	public void testVoidAttrType() {
		final String content = "{\"project\": {\"network_policys\": [{\"to\": [\"default-domain\", \"testProject\", \"testPolicy\"], \"href\": \"http://localhost:53730/network-policy/4e4b0486-e56f-4bfe-8716-afc1a76ad106\", \"uuid\": \"4e4b0486-e56f-4bfe-8716-afc1a76ad106\"}], \"fq_name\": [\"default-domain\", \"testProject\"], \"uuid\": \"7a6580ac-d7dc-4363-a342-47a473a32884\"}}";
		final JsonParser parser = new JsonParser();
		final JsonObject js_obj = parser.parse(content).getAsJsonObject();
		final JsonElement element = js_obj.get("project");
		final Project result = (Project) ApiSerializer.deserialize(element.toString(), Project.class);
		assertEquals("testProject", result.getName());
		assertNotNull(result.getNetworkPolicys());
	}

	@Test
	public void testPort() {
		final Port port = new Port();
		port.setName("Port1");
		port.setId("VIF_UUID");
		port.setUuid("VIF_UUID");
		port.setInstance_id("VM_UUID");
		port.setSystem_name("INTF_NAME");
		port.setIp_address("INTF_IP");
		port.setMac_address("MAC_ADDRESS");
		port.setVn_id("NW_UUID");
		port.setRx_vlan_id((short) 1000);
		port.setTx_vlan_id((short) 1001);
		port.setDisplay_name("VM-NAME");
		port.setVm_project_id("PROJ_UUID");

		final String jsdata = ApiSerializer.serializeObject("port", port, null, null);

		final JsonParser parser = new JsonParser();
		final JsonObject js_obj = parser.parse(jsdata).getAsJsonObject();
		final Port result = (Port) ApiSerializer.deserialize(js_obj.toString(), Port.class);

		assertEquals("VIF_UUID", result.getUuid());
		assertEquals("VIF_UUID", result.getId());
	}
}
