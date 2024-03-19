//
// Automatically generated.
//
package net.juniper.contrail.api.types;

import java.util.List;
import java.util.ArrayList;


import net.juniper.contrail.api.ApiObjectBase;
import net.juniper.contrail.api.ApiPropertyBase;
import net.juniper.contrail.api.ObjectReference;

public class InterfaceRouteTable extends ApiObjectBase {
    private RouteTableType interface_route_table_routes;
    private IdPermsType id_perms;
    private PermType2 perms2;
    private KeyValuePairs annotations;
    private String display_name;
    private List<ObjectReference<ServiceInterfaceTag>> service_instance_refs;
    private List<ObjectReference<ApiPropertyBase>> tag_refs;
    private transient List<ObjectReference<ApiPropertyBase>> routing_policy_back_refs;
    private transient List<ObjectReference<ApiPropertyBase>> virtual_machine_interface_back_refs;

    @Override
    public String getObjectType() {
        return "interface-route-table";
    }

    @Override
    public List<String> getDefaultParent() {
        return List.of("default-domain", "default-project");
    }

    @Override
    public String getDefaultParentType() {
        return "project";
    }

    public void setParent(Project parent) {
        super.setParent(parent);
    }
    
    public RouteTableType getRoutes() {
        return interface_route_table_routes;
    }
    
    public void setRoutes(RouteTableType interface_route_table_routes) {
        this.interface_route_table_routes = interface_route_table_routes;
    }
    
    
    public IdPermsType getIdPerms() {
        return id_perms;
    }
    
    public void setIdPerms(IdPermsType id_perms) {
        this.id_perms = id_perms;
    }
    
    
    public PermType2 getPerms2() {
        return perms2;
    }
    
    public void setPerms2(PermType2 perms2) {
        this.perms2 = perms2;
    }
    
    
    public KeyValuePairs getAnnotations() {
        return annotations;
    }
    
    public void setAnnotations(KeyValuePairs annotations) {
        this.annotations = annotations;
    }
    
    
    public String getDisplayName() {
        return display_name;
    }
    
    public void setDisplayName(String display_name) {
        this.display_name = display_name;
    }
    

    public List<ObjectReference<ServiceInterfaceTag>> getServiceInstance() {
        return service_instance_refs;
    }

    public void setServiceInstance(ServiceInstance obj, ServiceInterfaceTag data) {
        service_instance_refs = new ArrayList<ObjectReference<ServiceInterfaceTag>>();
        service_instance_refs.add(new ObjectReference<ServiceInterfaceTag>(obj.getQualifiedName(), data));
    }

    public void addServiceInstance(ServiceInstance obj, ServiceInterfaceTag data) {
        if (service_instance_refs == null) {
            service_instance_refs = new ArrayList<ObjectReference<ServiceInterfaceTag>>();
        }
        service_instance_refs.add(new ObjectReference<ServiceInterfaceTag>(obj.getQualifiedName(), data));
    }

    public void removeServiceInstance(ServiceInstance obj, ServiceInterfaceTag data) {
        if (service_instance_refs != null) {
            service_instance_refs.remove(new ObjectReference<ServiceInterfaceTag>(obj.getQualifiedName(), data));
        }
    }

    public void clearServiceInstance() {
        if (service_instance_refs != null) {
            service_instance_refs.clear();
            return;
        }
        service_instance_refs = null;
    }


    public List<ObjectReference<ApiPropertyBase>> getTag() {
        return tag_refs;
    }

    public void setTag(Tag obj) {
        tag_refs = new ArrayList<ObjectReference<ApiPropertyBase>>();
        tag_refs.add(new ObjectReference<ApiPropertyBase>(obj.getQualifiedName(), null));
    }
    
    public void addTag(Tag obj) {
        if (tag_refs == null) {
            tag_refs = new ArrayList<ObjectReference<ApiPropertyBase>>();
        }
        tag_refs.add(new ObjectReference<ApiPropertyBase>(obj.getQualifiedName(), null));
    }
    
    public void removeTag(Tag obj) {
        if (tag_refs != null) {
            tag_refs.remove(new ObjectReference<ApiPropertyBase>(obj.getQualifiedName(), null));
        }
    }

    public void clearTag() {
        if (tag_refs != null) {
            tag_refs.clear();
            return;
        }
        tag_refs = null;
    }

    public List<ObjectReference<ApiPropertyBase>> getRoutingPolicyBackRefs() {
        return routing_policy_back_refs;
    }

    public List<ObjectReference<ApiPropertyBase>> getVirtualMachineInterfaceBackRefs() {
        return virtual_machine_interface_back_refs;
    }
}