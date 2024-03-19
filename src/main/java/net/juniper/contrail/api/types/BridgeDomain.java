//
// Automatically generated.
//
package net.juniper.contrail.api.types;

import java.util.ArrayList;
import java.util.List;

import net.juniper.contrail.api.ApiObjectBase;
import net.juniper.contrail.api.ApiPropertyBase;
import net.juniper.contrail.api.ObjectReference;

public class BridgeDomain extends ApiObjectBase {
	private Boolean mac_learning_enabled;
	private MACLimitControlType mac_limit_control;
	private MACMoveLimitControlType mac_move_control;
	private Integer mac_aging_time;
	private Integer isid;
	private IdPermsType id_perms;
	private PermType2 perms2;
	private KeyValuePairs annotations;
	private String display_name;
	private List<ObjectReference<ApiPropertyBase>> tag_refs;
	private transient List<ObjectReference<BridgeDomainMembershipType>> virtual_machine_interface_back_refs;

	@Override
	public String getObjectType() {
		return "bridge-domain";
	}

	@Override
	public List<String> getDefaultParent() {
		return List.of("default-domain", "default-project", "default-virtual-network");
	}

	@Override
	public String getDefaultParentType() {
		return "virtual-network";
	}

	public void setParent(final VirtualNetwork parent) {
		super.setParent(parent);
	}

	public Boolean getMacLearningEnabled() {
		return mac_learning_enabled;
	}

	public void setMacLearningEnabled(final Boolean mac_learning_enabled) {
		this.mac_learning_enabled = mac_learning_enabled;
	}

	public MACLimitControlType getMacLimitControl() {
		return mac_limit_control;
	}

	public void setMacLimitControl(final MACLimitControlType mac_limit_control) {
		this.mac_limit_control = mac_limit_control;
	}

	public MACMoveLimitControlType getMacMoveControl() {
		return mac_move_control;
	}

	public void setMacMoveControl(final MACMoveLimitControlType mac_move_control) {
		this.mac_move_control = mac_move_control;
	}

	public Integer getMacAgingTime() {
		return mac_aging_time;
	}

	public void setMacAgingTime(final Integer mac_aging_time) {
		this.mac_aging_time = mac_aging_time;
	}

	public Integer getIsid() {
		return isid;
	}

	public void setIsid(final Integer isid) {
		this.isid = isid;
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

	public List<ObjectReference<BridgeDomainMembershipType>> getVirtualMachineInterfaceBackRefs() {
		return virtual_machine_interface_back_refs;
	}
}