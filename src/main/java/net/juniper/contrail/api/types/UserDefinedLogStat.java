//
// Automatically generated.
//
package net.juniper.contrail.api.types;

import java.util.List;
import java.util.ArrayList;

import net.juniper.contrail.api.ApiPropertyBase;

public class UserDefinedLogStat extends ApiPropertyBase {
    String name;
    String pattern;
    public UserDefinedLogStat() {
    }
    public UserDefinedLogStat(String name, String pattern) {
        this.name = name;
        this.pattern = pattern;
    }
    public UserDefinedLogStat(String name) {
        this(name, null);    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    
    public String getPattern() {
        return pattern;
    }
    
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
    
}
