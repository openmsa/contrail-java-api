//
// Automatically generated.
//
package net.juniper.contrail.api.types;

import java.util.ArrayList;
import java.util.List;

import net.juniper.contrail.api.ApiObjectBase;
import net.juniper.contrail.api.ApiPropertyBase;
import net.juniper.contrail.api.ObjectReference;

public class BgpAsAService extends ApiObjectBase {
	private Integer autonomous_system;
	private Boolean bgpaas_shared;
	private String bgpaas_ip_address;
	private String bgpaas_session_attributes;
	private Boolean bgpaas_ipv4_mapped_ipv6_nexthop;
	private Boolean bgpaas_suppress_route_advertisement;
	private IdPermsType id_perms;
	private PermType2 perms2;
	private KeyValuePairs annotations;
	private String display_name;
	private List<ObjectReference<BGPaaSControlNodeZoneAttributes>> control_node_zone_refs;
	private List<ObjectReference<ApiPropertyBase>> virtual_machine_interface_refs;
	private List<ObjectReference<ApiPropertyBase>> service_health_check_refs;
	private List<ObjectReference<ApiPropertyBase>> tag_refs;

	@Override
	public String getObjectType() {
		return "bgp-as-a-service";
	}

	@Override
	public List<String> getDefaultParent() {
		return List.of("default-domain", "default-project");
	}

	@Override
	public String getDefaultParentType() {
		return "project";
	}

	public void setParent(final Project parent) {
		super.setParent(parent);
	}

	public Integer getAutonomousSystem() {
		return autonomous_system;
	}

	public void setAutonomousSystem(final Integer autonomous_system) {
		this.autonomous_system = autonomous_system;
	}

	public Boolean getBgpaasShared() {
		return bgpaas_shared;
	}

	public void setBgpaasShared(final Boolean bgpaas_shared) {
		this.bgpaas_shared = bgpaas_shared;
	}

	public String getBgpaasIpAddress() {
		return bgpaas_ip_address;
	}

	public void setBgpaasIpAddress(final String bgpaas_ip_address) {
		this.bgpaas_ip_address = bgpaas_ip_address;
	}

	public String getBgpaasSessionAttributes() {
		return bgpaas_session_attributes;
	}

	public void setBgpaasSessionAttributes(final String bgpaas_session_attributes) {
		this.bgpaas_session_attributes = bgpaas_session_attributes;
	}

	public Boolean getBgpaasIpv4MappedIpv6Nexthop() {
		return bgpaas_ipv4_mapped_ipv6_nexthop;
	}

	public void setBgpaasIpv4MappedIpv6Nexthop(final Boolean bgpaas_ipv4_mapped_ipv6_nexthop) {
		this.bgpaas_ipv4_mapped_ipv6_nexthop = bgpaas_ipv4_mapped_ipv6_nexthop;
	}

	public Boolean getBgpaasSuppressRouteAdvertisement() {
		return bgpaas_suppress_route_advertisement;
	}

	public void setBgpaasSuppressRouteAdvertisement(final Boolean bgpaas_suppress_route_advertisement) {
		this.bgpaas_suppress_route_advertisement = bgpaas_suppress_route_advertisement;
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

	public List<ObjectReference<BGPaaSControlNodeZoneAttributes>> getControlNodeZone() {
		return control_node_zone_refs;
	}

	public void setControlNodeZone(final ControlNodeZone obj, final BGPaaSControlNodeZoneAttributes data) {
		control_node_zone_refs = new ArrayList<>();
		control_node_zone_refs.add(new ObjectReference<>(obj.getQualifiedName(), data));
	}

	public void addControlNodeZone(final ControlNodeZone obj, final BGPaaSControlNodeZoneAttributes data) {
		if (control_node_zone_refs == null) {
			control_node_zone_refs = new ArrayList<>();
		}
		control_node_zone_refs.add(new ObjectReference<>(obj.getQualifiedName(), data));
	}

	public void removeControlNodeZone(final ControlNodeZone obj, final BGPaaSControlNodeZoneAttributes data) {
		if (control_node_zone_refs != null) {
			control_node_zone_refs.remove(new ObjectReference<>(obj.getQualifiedName(), data));
		}
	}

	public void clearControlNodeZone() {
		if (control_node_zone_refs != null) {
			control_node_zone_refs.clear();
			return;
		}
		control_node_zone_refs = null;
	}

	public List<ObjectReference<ApiPropertyBase>> getVirtualMachineInterface() {
		return virtual_machine_interface_refs;
	}

	public void setVirtualMachineInterface(final VirtualMachineInterface obj) {
		virtual_machine_interface_refs = new ArrayList<>();
		virtual_machine_interface_refs.add(new ObjectReference<>(obj.getQualifiedName(), null));
	}

	public void addVirtualMachineInterface(final VirtualMachineInterface obj) {
		if (virtual_machine_interface_refs == null) {
			virtual_machine_interface_refs = new ArrayList<>();
		}
		virtual_machine_interface_refs.add(new ObjectReference<>(obj.getQualifiedName(), null));
	}

	public void removeVirtualMachineInterface(final VirtualMachineInterface obj) {
		if (virtual_machine_interface_refs != null) {
			virtual_machine_interface_refs.remove(new ObjectReference<>(obj.getQualifiedName(), null));
		}
	}

	public void clearVirtualMachineInterface() {
		if (virtual_machine_interface_refs != null) {
			virtual_machine_interface_refs.clear();
			return;
		}
		virtual_machine_interface_refs = null;
	}

	public List<ObjectReference<ApiPropertyBase>> getServiceHealthCheck() {
		return service_health_check_refs;
	}

	public void setServiceHealthCheck(final ServiceHealthCheck obj) {
		service_health_check_refs = new ArrayList<>();
		service_health_check_refs.add(new ObjectReference<>(obj.getQualifiedName(), null));
	}

	public void addServiceHealthCheck(final ServiceHealthCheck obj) {
		if (service_health_check_refs == null) {
			service_health_check_refs = new ArrayList<>();
		}
		service_health_check_refs.add(new ObjectReference<>(obj.getQualifiedName(), null));
	}

	public void removeServiceHealthCheck(final ServiceHealthCheck obj) {
		if (service_health_check_refs != null) {
			service_health_check_refs.remove(new ObjectReference<>(obj.getQualifiedName(), null));
		}
	}

	public void clearServiceHealthCheck() {
		if (service_health_check_refs != null) {
			service_health_check_refs.clear();
			return;
		}
		service_health_check_refs = null;
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