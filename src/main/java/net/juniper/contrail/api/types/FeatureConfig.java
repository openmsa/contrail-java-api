//
// Automatically generated.
//
package net.juniper.contrail.api.types;

import java.util.ArrayList;
import java.util.List;

import net.juniper.contrail.api.ApiObjectBase;
import net.juniper.contrail.api.ApiPropertyBase;
import net.juniper.contrail.api.ObjectReference;

public class FeatureConfig extends ApiObjectBase {
	private KeyValuePairs feature_config_additional_params;
	private KeyValuePairs feature_config_vendor_config;
	private IdPermsType id_perms;
	private PermType2 perms2;
	private KeyValuePairs annotations;
	private String display_name;
	private List<ObjectReference<ApiPropertyBase>> tag_refs;

	@Override
	public String getObjectType() {
		return "feature-config";
	}

	@Override
	public List<String> getDefaultParent() {
		return List.of("default-global-system-config", "default-role-definition");
	}

	@Override
	public String getDefaultParentType() {
		return "role-definition";
	}

	public void setParent(final RoleDefinition parent) {
		super.setParent(parent);
	}

	public KeyValuePairs getAdditionalParams() {
		return feature_config_additional_params;
	}

	public void setAdditionalParams(final KeyValuePairs feature_config_additional_params) {
		this.feature_config_additional_params = feature_config_additional_params;
	}

	public KeyValuePairs getVendorConfig() {
		return feature_config_vendor_config;
	}

	public void setVendorConfig(final KeyValuePairs feature_config_vendor_config) {
		this.feature_config_vendor_config = feature_config_vendor_config;
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
}