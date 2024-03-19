//
// Automatically generated.
//
package net.juniper.contrail.api.types;

import java.util.List;
import java.util.ArrayList;

import net.juniper.contrail.api.ApiPropertyBase;

public class AlarmAndList extends ApiPropertyBase {
    List<AlarmExpression> and_list;
    public AlarmAndList() {
    }
    public AlarmAndList(List<AlarmExpression> and_list) {
        this.and_list = and_list;
    }
    
    public List<AlarmExpression> getAndList() {
        return and_list;
    }
    
    
    public void addAnd(AlarmExpression obj) {
        if (and_list == null) {
            and_list = new ArrayList<AlarmExpression>();
        }
        and_list.add(obj);
    }
    public void clearAnd() {
        and_list = null;
    }
    
    
    public void addAnd(String operation, String operand1, AlarmOperand2 operand2, List<String> variables) {
        if (and_list == null) {
            and_list = new ArrayList<AlarmExpression>();
        }
        and_list.add(new AlarmExpression(operation, operand1, operand2, variables));
    }
    
}
