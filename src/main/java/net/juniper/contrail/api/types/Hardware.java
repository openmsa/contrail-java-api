//
// Automatically generated.
//
package net.juniper.contrail.api.types;

import java.util.List;
import java.util.ArrayList;


import net.juniper.contrail.api.ApiObjectBase;
import net.juniper.contrail.api.ApiPropertyBase;
import net.juniper.contrail.api.ObjectReference;

public class Hardware extends ApiObjectBase {
    private IdPermsType id_perms;
    private PermType2 perms2;
    private KeyValuePairs annotations;
    private String display_name;
    private List<ObjectReference<ApiPropertyBase>> card_refs;
    private List<ObjectReference<ApiPropertyBase>> tag_refs;
    private transient List<ObjectReference<ApiPropertyBase>> node_profile_back_refs;
    private transient List<ObjectReference<ApiPropertyBase>> device_image_back_refs;

    @Override
    public String getObjectType() {
        return "hardware";
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
    

    public List<ObjectReference<ApiPropertyBase>> getCard() {
        return card_refs;
    }

    public void setCard(Card obj) {
        card_refs = new ArrayList<ObjectReference<ApiPropertyBase>>();
        card_refs.add(new ObjectReference<ApiPropertyBase>(obj.getQualifiedName(), null));
    }
    
    public void addCard(Card obj) {
        if (card_refs == null) {
            card_refs = new ArrayList<ObjectReference<ApiPropertyBase>>();
        }
        card_refs.add(new ObjectReference<ApiPropertyBase>(obj.getQualifiedName(), null));
    }
    
    public void removeCard(Card obj) {
        if (card_refs != null) {
            card_refs.remove(new ObjectReference<ApiPropertyBase>(obj.getQualifiedName(), null));
        }
    }

    public void clearCard() {
        if (card_refs != null) {
            card_refs.clear();
            return;
        }
        card_refs = null;
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

    public List<ObjectReference<ApiPropertyBase>> getNodeProfileBackRefs() {
        return node_profile_back_refs;
    }

    public List<ObjectReference<ApiPropertyBase>> getDeviceImageBackRefs() {
        return device_image_back_refs;
    }
}