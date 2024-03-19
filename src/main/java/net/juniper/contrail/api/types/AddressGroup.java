//
// Automatically generated.
//
package net.juniper.contrail.api.types;

import java.util.ArrayList;
import java.util.List;

import net.juniper.contrail.api.ApiObjectBase;
import net.juniper.contrail.api.ApiPropertyBase;
import net.juniper.contrail.api.ObjectReference;

public class AddressGroup extends ApiObjectBase {
	private String draft_mode_state;
	private SubnetListType address_group_prefix;
	private IdPermsType id_perms;
	private PermType2 perms2;
	private KeyValuePairs annotations;
	private String display_name;
	private List<ObjectReference<ApiPropertyBase>> tag_refs;
	private transient List<ObjectReference<ApiPropertyBase>> firewall_rule_back_refs;

	@Override
	public String getObjectType() {
		return "address-group";
	}

	@Override
	public List<String> getDefaultParent() {
		return null;
	}

	@Override
	public String getDefaultParentType() {
		return null;
	}

	public void setParent(final PolicyManagement parent) {
		super.setParent(parent);
	}

	public void setParent(final Project parent) {
		super.setParent(parent);
	}

	public String getDraftModeState() {
		return draft_mode_state;
	}

	public void setDraftModeState(final String draft_mode_state) {
		this.draft_mode_state = draft_mode_state;
	}

	public SubnetListType getPrefix() {
		return address_group_prefix;
	}

	public void setPrefix(final SubnetListType address_group_prefix) {
		this.address_group_prefix = address_group_prefix;
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

	public List<ObjectReference<ApiPropertyBase>> getFirewallRuleBackRefs() {
		return firewall_rule_back_refs;
	}
}