//
// Automatically generated.
//
package net.juniper.contrail.api.types;

import java.util.ArrayList;
import java.util.List;

import net.juniper.contrail.api.ApiObjectBase;
import net.juniper.contrail.api.ApiPropertyBase;
import net.juniper.contrail.api.ObjectReference;

public class DataCenterInterconnect extends ApiObjectBase {
	private String data_center_interconnect_bgp_hold_time;
	private String data_center_interconnect_mode;
	private String data_center_interconnect_bgp_address_families;
	private RouteTargetList data_center_interconnect_configured_route_target_list;
	private String data_center_interconnect_type;
	private LogicalRouterPRListType destination_physical_router_list;
	private IdPermsType id_perms;
	private PermType2 perms2;
	private KeyValuePairs annotations;
	private String display_name;
	private List<ObjectReference<ApiPropertyBase>> logical_router_refs;
	private List<ObjectReference<ApiPropertyBase>> virtual_network_refs;
	private List<ObjectReference<ApiPropertyBase>> routing_policy_refs;
	private List<ObjectReference<ApiPropertyBase>> fabric_refs;
	private List<ObjectReference<ApiPropertyBase>> tag_refs;

	@Override
	public String getObjectType() {
		return "data-center-interconnect";
	}

	@Override
	public List<String> getDefaultParent() {
		return List.of("default-global-system-config");
	}

	@Override
	public String getDefaultParentType() {
		return "global-system-config";
	}

	public void setParent(final GlobalSystemConfig parent) {
		super.setParent(parent);
	}

	public String getBgpHoldTime() {
		return data_center_interconnect_bgp_hold_time;
	}

	public void setBgpHoldTime(final String data_center_interconnect_bgp_hold_time) {
		this.data_center_interconnect_bgp_hold_time = data_center_interconnect_bgp_hold_time;
	}

	public String getMode() {
		return data_center_interconnect_mode;
	}

	public void setMode(final String data_center_interconnect_mode) {
		this.data_center_interconnect_mode = data_center_interconnect_mode;
	}

	public String getBgpAddressFamilies() {
		return data_center_interconnect_bgp_address_families;
	}

	public void setBgpAddressFamilies(final String data_center_interconnect_bgp_address_families) {
		this.data_center_interconnect_bgp_address_families = data_center_interconnect_bgp_address_families;
	}

	public RouteTargetList getConfiguredRouteTargetList() {
		return data_center_interconnect_configured_route_target_list;
	}

	public void setConfiguredRouteTargetList(final RouteTargetList data_center_interconnect_configured_route_target_list) {
		this.data_center_interconnect_configured_route_target_list = data_center_interconnect_configured_route_target_list;
	}

	public String getType() {
		return data_center_interconnect_type;
	}

	public void setType(final String data_center_interconnect_type) {
		this.data_center_interconnect_type = data_center_interconnect_type;
	}

	public LogicalRouterPRListType getDestinationPhysicalRouterList() {
		return destination_physical_router_list;
	}

	public void setDestinationPhysicalRouterList(final LogicalRouterPRListType destination_physical_router_list) {
		this.destination_physical_router_list = destination_physical_router_list;
	}

	public IdPermsType getIdPerms() {
		return id_perms;
	}

	public void setIdPerms(final IdPermsType id_perms) {
		this.id_perms = id_perms;
	}

	public PermType2 getPerms2() {
		return perms2;
	}

	public void setPerms2(final PermType2 perms2) {
		this.perms2 = perms2;
	}

	public KeyValuePairs getAnnotations() {
		return annotations;
	}

	public void setAnnotations(final KeyValuePairs annotations) {
		this.annotations = annotations;
	}

	public String getDisplayName() {
		return display_name;
	}

	public void setDisplayName(final String display_name) {
		this.display_name = display_name;
	}

	public List<ObjectReference<ApiPropertyBase>> getLogicalRouter() {
		return logical_router_refs;
	}

	public void setLogicalRouter(final LogicalRouter obj) {
		logical_router_refs = new ArrayList<>();
		logical_router_refs.add(new ObjectReference<>(obj.getQualifiedName(), null));
	}

	public void addLogicalRouter(final LogicalRouter obj) {
		if (logical_router_refs == null) {
			logical_router_refs = new ArrayList<>();
		}
		logical_router_refs.add(new ObjectReference<>(obj.getQualifiedName(), null));
	}

	public void removeLogicalRouter(final LogicalRouter obj) {
		if (logical_router_refs != null) {
			logical_router_refs.remove(new ObjectReference<>(obj.getQualifiedName(), null));
		}
	}

	public void clearLogicalRouter() {
		if (logical_router_refs != null) {
			logical_router_refs.clear();
			return;
		}
		logical_router_refs = null;
	}

	public List<ObjectReference<ApiPropertyBase>> getVirtualNetwork() {
		return virtual_network_refs;
	}

	public void setVirtualNetwork(final VirtualNetwork obj) {
		virtual_network_refs = new ArrayList<>();
		virtual_network_refs.add(new ObjectReference<>(obj.getQualifiedName(), null));
	}

	public void addVirtualNetwork(final VirtualNetwork obj) {
		if (virtual_network_refs == null) {
			virtual_network_refs = new ArrayList<>();
		}
		virtual_network_refs.add(new ObjectReference<>(obj.getQualifiedName(), null));
	}

	public void removeVirtualNetwork(final VirtualNetwork obj) {
		if (virtual_network_refs != null) {
			virtual_network_refs.remove(new ObjectReference<>(obj.getQualifiedName(), null));
		}
	}

	public void clearVirtualNetwork() {
		if (virtual_network_refs != null) {
			virtual_network_refs.clear();
			return;
		}
		virtual_network_refs = null;
	}

	public List<ObjectReference<ApiPropertyBase>> getRoutingPolicy() {
		return routing_policy_refs;
	}

	public void setRoutingPolicy(final RoutingPolicy obj) {
		routing_policy_refs = new ArrayList<>();
		routing_policy_refs.add(new ObjectReference<>(obj.getQualifiedName(), null));
	}

	public void addRoutingPolicy(final RoutingPolicy obj) {
		if (routing_policy_refs == null) {
			routing_policy_refs = new ArrayList<>();
		}
		routing_policy_refs.add(new ObjectReference<>(obj.getQualifiedName(), null));
	}

	public void removeRoutingPolicy(final RoutingPolicy obj) {
		if (routing_policy_refs != null) {
			routing_policy_refs.remove(new ObjectReference<>(obj.getQualifiedName(), null));
		}
	}

	public void clearRoutingPolicy() {
		if (routing_policy_refs != null) {
			routing_policy_refs.clear();
			return;
		}
		routing_policy_refs = null;
	}

	public List<ObjectReference<ApiPropertyBase>> getFabric() {
		return fabric_refs;
	}

	public void setFabric(final Fabric obj) {
		fabric_refs = new ArrayList<>();
		fabric_refs.add(new ObjectReference<>(obj.getQualifiedName(), null));
	}

	public void addFabric(final Fabric obj) {
		if (fabric_refs == null) {
			fabric_refs = new ArrayList<>();
		}
		fabric_refs.add(new ObjectReference<>(obj.getQualifiedName(), null));
	}

	public void removeFabric(final Fabric obj) {
		if (fabric_refs != null) {
			fabric_refs.remove(new ObjectReference<>(obj.getQualifiedName(), null));
		}
	}

	public void clearFabric() {
		if (fabric_refs != null) {
			fabric_refs.clear();
			return;
		}
		fabric_refs = null;
	}

	public List<ObjectReference<ApiPropertyBase>> getTag() {
		return tag_refs;
	}

	public void setTag(final Tag obj) {
		tag_refs = new ArrayList<>();
		tag_refs.add(new ObjectReference<>(obj.getQualifiedName(), null));
	}

	public void addTag(final Tag obj) {
		if (tag_refs == null) {
			tag_refs = new ArrayList<>();
		}
		tag_refs.add(new ObjectReference<>(obj.getQualifiedName(), null));
	}

	public void removeTag(final Tag obj) {
		if (tag_refs != null) {
			tag_refs.remove(new ObjectReference<>(obj.getQualifiedName(), null));
		}
	}

	public void clearTag() {
		if (tag_refs != null) {
			tag_refs.clear();
			return;
		}
		tag_refs = null;
	}
}