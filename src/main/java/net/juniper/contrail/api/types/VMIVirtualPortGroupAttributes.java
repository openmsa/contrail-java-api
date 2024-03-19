//
// Automatically generated.
//
package net.juniper.contrail.api.types;

import java.util.List;
import java.util.ArrayList;

import net.juniper.contrail.api.ApiPropertyBase;

public class VMIVirtualPortGroupAttributes extends ApiPropertyBase {
    Integer vlan_tag;
    Integer native_vlan_tag;
    public VMIVirtualPortGroupAttributes() {
    }
    public VMIVirtualPortGroupAttributes(Integer vlan_tag, Integer native_vlan_tag) {
        this.vlan_tag = vlan_tag;
        this.native_vlan_tag = native_vlan_tag;
    }
    public VMIVirtualPortGroupAttributes(Integer vlan_tag) {
        this(vlan_tag, null);    }
    
    public Integer getVlanTag() {
        return vlan_tag;
    }
    
    public void setVlanTag(Integer vlan_tag) {
        this.vlan_tag = vlan_tag;
    }
    
    
    public Integer getNativeVlanTag() {
        return native_vlan_tag;
    }
    
    public void setNativeVlanTag(Integer native_vlan_tag) {
        this.native_vlan_tag = native_vlan_tag;
    }
    
}
