//
// Automatically generated.
//
package net.juniper.contrail.api.types;

import java.util.List;
import java.util.ArrayList;


import net.juniper.contrail.api.ApiObjectBase;
import net.juniper.contrail.api.ApiPropertyBase;
import net.juniper.contrail.api.ObjectReference;

public class NetworkPolicy extends ApiObjectBase {
    private PolicyEntriesType network_policy_entries;
    private IdPermsType id_perms;
    private PermType2 perms2;
    private KeyValuePairs annotations;
    private String display_name;
    private List<ObjectReference<ApiPropertyBase>> tag_refs;
    private transient List<ObjectReference<SecurityLoggingObjectRuleListType>> security_logging_object_back_refs;
    private transient List<ObjectReference<VirtualNetworkPolicyType>> virtual_network_back_refs;

    @Override
    public String getObjectType() {
        return "network-policy";
    }

    @Override
    public List<String> getDefaultParent() {
        return List.of("default-domain", "default-project");
    }

    @Override
    public String getDefaultParentType() {
        return "project";
    }

    public void setParent(Project parent) {
        super.setParent(parent);
    }
    
    public PolicyEntriesType getEntries() {
        return network_policy_entries;
    }
    
    public void setEntries(PolicyEntriesType network_policy_entries) {
        this.network_policy_entries = network_policy_entries;
    }
    
    
    public IdPermsType getIdPerms() {
        return id_perms;
    }
    
    public void setIdPerms(IdPermsType id_perms) {
        this.id_perms = id_perms;
    }
    
    
    public PermType2 getPerms2() {
        return perms2;
    }
    
    public void setPerms2(PermType2 perms2) {
        this.perms2 = perms2;
    }
    
    
    public KeyValuePairs getAnnotations() {
        return annotations;
    }
    
    public void setAnnotations(KeyValuePairs annotations) {
        this.annotations = annotations;
    }
    
    
    public String getDisplayName() {
        return display_name;
    }
    
    public void setDisplayName(String display_name) {
        this.display_name = display_name;
    }
    

    public List<ObjectReference<ApiPropertyBase>> getTag() {
        return tag_refs;
    }

    public void setTag(Tag obj) {
        tag_refs = new ArrayList<ObjectReference<ApiPropertyBase>>();
        tag_refs.add(new ObjectReference<ApiPropertyBase>(obj.getQualifiedName(), null));
    }
    
    public void addTag(Tag obj) {
        if (tag_refs == null) {
            tag_refs = new ArrayList<ObjectReference<ApiPropertyBase>>();
        }
        tag_refs.add(new ObjectReference<ApiPropertyBase>(obj.getQualifiedName(), null));
    }
    
    public void removeTag(Tag obj) {
        if (tag_refs != null) {
            tag_refs.remove(new ObjectReference<ApiPropertyBase>(obj.getQualifiedName(), null));
        }
    }

    public void clearTag() {
        if (tag_refs != null) {
            tag_refs.clear();
            return;
        }
        tag_refs = null;
    }

    public List<ObjectReference<SecurityLoggingObjectRuleListType>> getSecurityLoggingObjectBackRefs() {
        return security_logging_object_back_refs;
    }

    public List<ObjectReference<VirtualNetworkPolicyType>> getVirtualNetworkBackRefs() {
        return virtual_network_back_refs;
    }
}