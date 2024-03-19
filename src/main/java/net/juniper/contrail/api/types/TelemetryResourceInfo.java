//
// Automatically generated.
//
package net.juniper.contrail.api.types;

import java.util.List;
import java.util.ArrayList;

import net.juniper.contrail.api.ApiPropertyBase;

public class TelemetryResourceInfo extends ApiPropertyBase {
    String name;
    String path;
    String rate;
    public TelemetryResourceInfo() {
    }
    public TelemetryResourceInfo(String name, String path, String rate) {
        this.name = name;
        this.path = path;
        this.rate = rate;
    }
    public TelemetryResourceInfo(String name) {
        this(name, null, null);    }
    public TelemetryResourceInfo(String name, String path) {
        this(name, path, null);    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    
    public String getRate() {
        return rate;
    }
    
    public void setRate(String rate) {
        this.rate = rate;
    }
    
}
