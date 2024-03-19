//
// Automatically generated.
//
package net.juniper.contrail.api.types;

import java.util.List;
import java.util.ArrayList;

import net.juniper.contrail.api.ApiPropertyBase;

public class AlarmOperand2 extends ApiPropertyBase {
    String uve_attribute;
    String json_value;
    public AlarmOperand2() {
    }
    public AlarmOperand2(String uve_attribute, String json_value) {
        this.uve_attribute = uve_attribute;
        this.json_value = json_value;
    }
    public AlarmOperand2(String uve_attribute) {
        this(uve_attribute, null);    }
    
    public String getUveAttribute() {
        return uve_attribute;
    }
    
    public void setUveAttribute(String uve_attribute) {
        this.uve_attribute = uve_attribute;
    }
    
    
    public String getJsonValue() {
        return json_value;
    }
    
    public void setJsonValue(String json_value) {
        this.json_value = json_value;
    }
    
}
