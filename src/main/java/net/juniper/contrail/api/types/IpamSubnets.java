//
// Automatically generated.
//
package net.juniper.contrail.api.types;

import java.util.List;
import java.util.ArrayList;

import net.juniper.contrail.api.ApiPropertyBase;

public class IpamSubnets extends ApiPropertyBase {
    List<IpamSubnetType> subnets;
    public IpamSubnets() {
    }
    public IpamSubnets(List<IpamSubnetType> subnets) {
        this.subnets = subnets;
    }
    
    public List<IpamSubnetType> getSubnets() {
        return subnets;
    }
    
    
    public void addSubnets(IpamSubnetType obj) {
        if (subnets == null) {
            subnets = new ArrayList<IpamSubnetType>();
        }
        subnets.add(obj);
    }
    public void clearSubnets() {
        subnets = null;
    }
    
}
