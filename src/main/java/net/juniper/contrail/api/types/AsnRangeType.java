//
// Automatically generated.
//
package net.juniper.contrail.api.types;

import java.util.List;
import java.util.ArrayList;

import net.juniper.contrail.api.ApiPropertyBase;

public class AsnRangeType extends ApiPropertyBase {
    Integer asn_min;
    Integer asn_max;
    public AsnRangeType() {
    }
    public AsnRangeType(Integer asn_min, Integer asn_max) {
        this.asn_min = asn_min;
        this.asn_max = asn_max;
    }
    public AsnRangeType(Integer asn_min) {
        this(asn_min, null);    }
    
    public Integer getAsnMin() {
        return asn_min;
    }
    
    public void setAsnMin(Integer asn_min) {
        this.asn_min = asn_min;
    }
    
    
    public Integer getAsnMax() {
        return asn_max;
    }
    
    public void setAsnMax(Integer asn_max) {
        this.asn_max = asn_max;
    }
    
}
