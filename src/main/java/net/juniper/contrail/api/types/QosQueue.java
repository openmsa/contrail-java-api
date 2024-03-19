//
// Automatically generated.
//
package net.juniper.contrail.api.types;

import java.util.List;
import java.util.ArrayList;


import net.juniper.contrail.api.ApiObjectBase;
import net.juniper.contrail.api.ApiPropertyBase;
import net.juniper.contrail.api.ObjectReference;

public class QosQueue extends ApiObjectBase {
    private Integer min_bandwidth;
    private Integer max_bandwidth;
    private Integer qos_queue_identifier;
    private IdPermsType id_perms;
    private PermType2 perms2;
    private KeyValuePairs annotations;
    private String display_name;
    private List<ObjectReference<ApiPropertyBase>> tag_refs;
    private transient List<ObjectReference<ApiPropertyBase>> forwarding_class_back_refs;

    @Override
    public String getObjectType() {
        return "qos-queue";
    }

    @Override
    public List<String> getDefaultParent() {
        return List.of("default-global-system-config", "default-global-qos-config");
    }

    @Override
    public String getDefaultParentType() {
        return "global-qos-config";
    }

    public void setParent(GlobalQosConfig parent) {
        super.setParent(parent);
    }
    
    public Integer getMinBandwidth() {
        return min_bandwidth;
    }
    
    public void setMinBandwidth(Integer min_bandwidth) {
        this.min_bandwidth = min_bandwidth;
    }
    
    
    public Integer getMaxBandwidth() {
        return max_bandwidth;
    }
    
    public void setMaxBandwidth(Integer max_bandwidth) {
        this.max_bandwidth = max_bandwidth;
    }
    
    
    public Integer getIdentifier() {
        return qos_queue_identifier;
    }
    
    public void setIdentifier(Integer qos_queue_identifier) {
        this.qos_queue_identifier = qos_queue_identifier;
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

    public List<ObjectReference<ApiPropertyBase>> getForwardingClassBackRefs() {
        return forwarding_class_back_refs;
    }
}