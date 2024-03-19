//
// Automatically generated.
//
package net.juniper.contrail.api.types;

import java.util.ArrayList;
import java.util.List;

import net.juniper.contrail.api.ApiObjectBase;
import net.juniper.contrail.api.ApiPropertyBase;
import net.juniper.contrail.api.ObjectReference;

public class DeviceFunctionalGroup extends ApiObjectBase {
	private String device_functional_group_description;
	private String device_functional_group_os_version;
	private RoutingBridgingRolesType device_functional_group_routing_bridging_roles;
	private IdPermsType id_perms;
	private PermType2 perms2;
	private KeyValuePairs annotations;
	private String display_name;
	private List<ObjectReference<ApiPropertyBase>> physical_role_refs;
	private List<ObjectReference<ApiPropertyBase>> tag_refs;
	private transient List<ObjectReference<ApiPropertyBase>> physical_router_back_refs;

	@Override
	public String getObjectType() {
		return "device-functional-group";
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

	public String getDescription() {
		return device_functional_group_description;
	}

	public void setDescription(final String device_functional_group_description) {
		this.device_functional_group_description = device_functional_group_description;
	}

	public String getOsVersion() {
		return device_functional_group_os_version;
	}

	public void setOsVersion(final String device_functional_group_os_version) {
		this.device_functional_group_os_version = device_functional_group_os_version;
	}

	public RoutingBridgingRolesType getRoutingBridgingRoles() {
		return device_functional_group_routing_bridging_roles;
	}

	public void setRoutingBridgingRoles(final RoutingBridgingRolesType device_functional_group_routing_bridging_roles) {
		this.device_functional_group_routing_bridging_roles = device_functional_group_routing_bridging_roles;
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

	public List<ObjectReference<ApiPropertyBase>> getPhysicalRole() {
		return physical_role_refs;
	}

	public void setPhysicalRole(final PhysicalRole obj) {
		physical_role_refs = new ArrayList<>();
		physical_role_refs.add(new ObjectReference<>(obj.getQualifiedName(), null));
	}

	public void addPhysicalRole(final PhysicalRole obj) {
		if (physical_role_refs == null) {
			physical_role_refs = new ArrayList<>();
		}
		physical_role_refs.add(new ObjectReference<>(obj.getQualifiedName(), null));
	}

	public void removePhysicalRole(final PhysicalRole obj) {
		if (physical_role_refs != null) {
			physical_role_refs.remove(new ObjectReference<>(obj.getQualifiedName(), null));
		}
	}

	public void clearPhysicalRole() {
		if (physical_role_refs != null) {
			physical_role_refs.clear();
			return;
		}
		physical_role_refs = null;
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

	public List<ObjectReference<ApiPropertyBase>> getPhysicalRouterBackRefs() {
		return physical_router_back_refs;
	}
}