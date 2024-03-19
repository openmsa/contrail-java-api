//
// Automatically generated.
//
package net.juniper.contrail.api.types;

import java.util.ArrayList;
import java.util.List;

import net.juniper.contrail.api.ApiObjectBase;
import net.juniper.contrail.api.ApiPropertyBase;
import net.juniper.contrail.api.ObjectReference;

public class CustomerAttachment extends ApiObjectBase {
	private IdPermsType id_perms;
	private PermType2 perms2;
	private KeyValuePairs annotations;
	private String display_name;
	private List<ObjectReference<ApiPropertyBase>> virtual_machine_interface_refs;
	private List<ObjectReference<ApiPropertyBase>> floating_ip_refs;
	private List<ObjectReference<ApiPropertyBase>> tag_refs;

	@Override
	public String getObjectType() {
		return "customer-attachment";
	}

	@Override
	public List<String> getDefaultParent() {
		return List.of();
	}

	@Override
	public String getDefaultParentType() {
		return null;
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

	public List<ObjectReference<ApiPropertyBase>> getFloatingIp() {
		return floating_ip_refs;
	}

	public void setFloatingIp(final FloatingIp obj) {
		floating_ip_refs = new ArrayList<>();
		floating_ip_refs.add(new ObjectReference<>(obj.getQualifiedName(), null));
	}

	public void addFloatingIp(final FloatingIp obj) {
		if (floating_ip_refs == null) {
			floating_ip_refs = new ArrayList<>();
		}
		floating_ip_refs.add(new ObjectReference<>(obj.getQualifiedName(), null));
	}

	public void removeFloatingIp(final FloatingIp obj) {
		if (floating_ip_refs != null) {
			floating_ip_refs.remove(new ObjectReference<>(obj.getQualifiedName(), null));
		}
	}

	public void clearFloatingIp() {
		if (floating_ip_refs != null) {
			floating_ip_refs.clear();
			return;
		}
		floating_ip_refs = null;
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