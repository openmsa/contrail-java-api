//
// Automatically generated.
//
package net.juniper.contrail.api.types;

import java.util.ArrayList;
import java.util.List;

import net.juniper.contrail.api.ApiObjectBase;
import net.juniper.contrail.api.ApiPropertyBase;
import net.juniper.contrail.api.ObjectReference;

public class AliasIp extends ApiObjectBase {
	private String alias_ip_address;
	private String alias_ip_address_family;
	private IdPermsType id_perms;
	private PermType2 perms2;
	private KeyValuePairs annotations;
	private String display_name;
	private List<ObjectReference<ApiPropertyBase>> project_refs;
	private List<ObjectReference<ApiPropertyBase>> virtual_machine_interface_refs;
	private List<ObjectReference<ApiPropertyBase>> tag_refs;

	@Override
	public String getObjectType() {
		return "alias-ip";
	}

	@Override
	public List<String> getDefaultParent() {
		return List.of("default-domain", "default-project", "default-virtual-network", "default-alias-ip-pool");
	}

	@Override
	public String getDefaultParentType() {
		return "alias-ip-pool";
	}

	public void setParent(final AliasIpPool parent) {
		super.setParent(parent);
	}

	public String getAddress() {
		return alias_ip_address;
	}

	public void setAddress(final String alias_ip_address) {
		this.alias_ip_address = alias_ip_address;
	}

	public String getAddressFamily() {
		return alias_ip_address_family;
	}

	public void setAddressFamily(final String alias_ip_address_family) {
		this.alias_ip_address_family = alias_ip_address_family;
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

	public List<ObjectReference<ApiPropertyBase>> getProject() {
		return project_refs;
	}

	public void setProject(final Project obj) {
		project_refs = new ArrayList<>();
		project_refs.add(new ObjectReference<>(obj.getQualifiedName(), null));
	}

	public void addProject(final Project obj) {
		if (project_refs == null) {
			project_refs = new ArrayList<>();
		}
		project_refs.add(new ObjectReference<>(obj.getQualifiedName(), null));
	}

	public void removeProject(final Project obj) {
		if (project_refs != null) {
			project_refs.remove(new ObjectReference<>(obj.getQualifiedName(), null));
		}
	}

	public void clearProject() {
		if (project_refs != null) {
			project_refs.clear();
			return;
		}
		project_refs = null;
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