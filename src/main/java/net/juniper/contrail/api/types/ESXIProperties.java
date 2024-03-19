//
// Automatically generated.
//
package net.juniper.contrail.api.types;

import java.util.List;
import java.util.ArrayList;

import net.juniper.contrail.api.ApiPropertyBase;

public class ESXIProperties extends ApiPropertyBase {
    String dvs_name;
    String dvs_id;
    public ESXIProperties() {
    }
    public ESXIProperties(String dvs_name, String dvs_id) {
        this.dvs_name = dvs_name;
        this.dvs_id = dvs_id;
    }
    public ESXIProperties(String dvs_name) {
        this(dvs_name, null);    }
    
    public String getDvsName() {
        return dvs_name;
    }
    
    public void setDvsName(String dvs_name) {
        this.dvs_name = dvs_name;
    }
    
    
    public String getDvsId() {
        return dvs_id;
    }
    
    public void setDvsId(String dvs_id) {
        this.dvs_id = dvs_id;
    }
    
}
