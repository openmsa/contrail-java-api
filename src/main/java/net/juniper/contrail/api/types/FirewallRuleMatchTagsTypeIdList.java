//
// Automatically generated.
//
package net.juniper.contrail.api.types;

import java.util.List;
import java.util.ArrayList;

import net.juniper.contrail.api.ApiPropertyBase;

public class FirewallRuleMatchTagsTypeIdList extends ApiPropertyBase {
    List<Integer> tag_type;
    public FirewallRuleMatchTagsTypeIdList() {
    }
    public FirewallRuleMatchTagsTypeIdList(List<Integer> tag_type) {
        this.tag_type = tag_type;
    }
    
    public List<Integer> getTagType() {
        return tag_type;
    }
    
    
    public void addTagType(Integer obj) {
        if (tag_type == null) {
            tag_type = new ArrayList<Integer>();
        }
        tag_type.add(obj);
    }
    public void clearTagType() {
        tag_type = null;
    }
    
}
