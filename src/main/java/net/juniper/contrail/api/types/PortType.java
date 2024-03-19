//
// Automatically generated.
//
package net.juniper.contrail.api.types;

import java.util.List;
import java.util.ArrayList;

import net.juniper.contrail.api.ApiPropertyBase;

public class PortType extends ApiPropertyBase {
    Integer start_port;
    Integer end_port;
    public PortType() {
    }
    public PortType(Integer start_port, Integer end_port) {
        this.start_port = start_port;
        this.end_port = end_port;
    }
    public PortType(Integer start_port) {
        this(start_port, 65535);    }
    
    public Integer getStartPort() {
        return start_port;
    }
    
    public void setStartPort(Integer start_port) {
        this.start_port = start_port;
    }
    
    
    public Integer getEndPort() {
        return end_port;
    }
    
    public void setEndPort(Integer end_port) {
        this.end_port = end_port;
    }
    
}
