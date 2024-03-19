//
// Automatically generated.
//
package net.juniper.contrail.api.types;

import java.util.ArrayList;
import java.util.List;

import net.juniper.contrail.api.ApiObjectBase;
import net.juniper.contrail.api.ApiPropertyBase;
import net.juniper.contrail.api.ObjectReference;

public class E2ServiceProvider extends ApiObjectBase {
	private Boolean e2_service_provider_promiscuous;
	private IdPermsType id_perms;
	private PermType2 perms2;
	private KeyValuePairs annotations;
	private String display_name;
	private List<ObjectReference<ApiPropertyBase>> peering_policy_refs;
	private List<ObjectReference<ApiPropertyBase>> physical_router_refs;
	private List<ObjectReference<ApiPropertyBase>> tag_refs;

	@Override
	public String getObjectType() {
		return "e2-service-provider";
	}

	@Override
	public List<String> getDefaultParent() {
		return List.of();
	}

	@Override
	public String getDefaultParentType() {
		return null;
	}

	public Boolean getPromiscuous() {
		return e2_service_provider_promiscuous;
	}

	public void setPromiscuous(final Boolean e2_service_provider_promiscuous) {
		this.e2_service_provider_promiscuous = e2_service_provider_promiscuous;
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

	public List<ObjectReference<ApiPropertyBase>> getPeeringPolicy() {
		return peering_policy_refs;
	}

	public void setPeeringPolicy(final PeeringPolicy obj) {
		peering_policy_refs = new ArrayList<>();
		peering_policy_refs.add(new ObjectReference<>(obj.getQualifiedName(), null));
	}

	public void addPeeringPolicy(final PeeringPolicy obj) {
		if (peering_policy_refs == null) {
			peering_policy_refs = new ArrayList<>();
		}
		peering_policy_refs.add(new ObjectReference<>(obj.getQualifiedName(), null));
	}

	public void removePeeringPolicy(final PeeringPolicy obj) {
		if (peering_policy_refs != null) {
			peering_policy_refs.remove(new ObjectReference<>(obj.getQualifiedName(), null));
		}
	}

	public void clearPeeringPolicy() {
		if (peering_policy_refs != null) {
			peering_policy_refs.clear();
			return;
		}
		peering_policy_refs = null;
	}

	public List<ObjectReference<ApiPropertyBase>> getPhysicalRouter() {
		return physical_router_refs;
	}

	public void setPhysicalRouter(final PhysicalRouter obj) {
		physical_router_refs = new ArrayList<>();
		physical_router_refs.add(new ObjectReference<>(obj.getQualifiedName(), null));
	}

	public void addPhysicalRouter(final PhysicalRouter obj) {
		if (physical_router_refs == null) {
			physical_router_refs = new ArrayList<>();
		}
		physical_router_refs.add(new ObjectReference<>(obj.getQualifiedName(), null));
	}

	public void removePhysicalRouter(final PhysicalRouter obj) {
		if (physical_router_refs != null) {
			physical_router_refs.remove(new ObjectReference<>(obj.getQualifiedName(), null));
		}
	}

	public void clearPhysicalRouter() {
		if (physical_router_refs != null) {
			physical_router_refs.clear();
			return;
		}
		physical_router_refs = null;
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