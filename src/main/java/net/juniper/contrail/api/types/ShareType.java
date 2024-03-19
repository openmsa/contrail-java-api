//
// Automatically generated.
//
package net.juniper.contrail.api.types;

import java.util.List;
import java.util.ArrayList;

import net.juniper.contrail.api.ApiPropertyBase;

public class ShareType extends ApiPropertyBase {
    String tenant;
    Integer tenant_access;
    public ShareType() {
    }
    public ShareType(String tenant, Integer tenant_access) {
        this.tenant = tenant;
        this.tenant_access = tenant_access;
    }
    public ShareType(String tenant) {
        this(tenant, null);    }
    
    public String getTenant() {
        return tenant;
    }
    
    public void setTenant(String tenant) {
        this.tenant = tenant;
    }
    
    
    public Integer getTenantAccess() {
        return tenant_access;
    }
    
    public void setTenantAccess(Integer tenant_access) {
        this.tenant_access = tenant_access;
    }
    
}
