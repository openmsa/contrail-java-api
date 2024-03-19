//
// Automatically generated.
//
package net.juniper.contrail.api.types;

import java.util.ArrayList;
import java.util.List;

import net.juniper.contrail.api.ApiObjectBase;
import net.juniper.contrail.api.ApiPropertyBase;
import net.juniper.contrail.api.ObjectReference;

public class ConfigRoot extends ApiObjectBase {
	private IdPermsType id_perms;
	private PermType2 perms2;
	private KeyValuePairs annotations;
	private String display_name;
	private List<ObjectReference<ApiPropertyBase>> global_system_configs;
	private List<ObjectReference<ApiPropertyBase>> domains;
	private List<ObjectReference<ApiPropertyBase>> policy_managements;
	private List<ObjectReference<ApiPropertyBase>> tags;
	private List<ObjectReference<ApiPropertyBase>> tag_refs;

	@Override
	public String getObjectType() {
		return "config-root";
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

	public List<ObjectReference<ApiPropertyBase>> getGlobalSystemConfigs() {
		return global_system_configs;
	}

	public List<ObjectReference<ApiPropertyBase>> getDomains() {
		return domains;
	}

	public List<ObjectReference<ApiPropertyBase>> getPolicyManagements() {
		return policy_managements;
	}

	public List<ObjectReference<ApiPropertyBase>> getTags() {
		return tags;
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