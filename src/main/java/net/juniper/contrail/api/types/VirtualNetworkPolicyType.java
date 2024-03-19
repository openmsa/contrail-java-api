//
// Automatically generated.
//
package net.juniper.contrail.api.types;

import java.util.List;
import java.util.ArrayList;

import net.juniper.contrail.api.ApiPropertyBase;

public class VirtualNetworkPolicyType extends ApiPropertyBase {
    SequenceType sequence;
    TimerType timer;
    public VirtualNetworkPolicyType() {
    }
    public VirtualNetworkPolicyType(SequenceType sequence, TimerType timer) {
        this.sequence = sequence;
        this.timer = timer;
    }
    public VirtualNetworkPolicyType(SequenceType sequence) {
        this(sequence, null);    }
    
    public SequenceType getSequence() {
        return sequence;
    }
    
    public void setSequence(SequenceType sequence) {
        this.sequence = sequence;
    }
    
    
    public TimerType getTimer() {
        return timer;
    }
    
    public void setTimer(TimerType timer) {
        this.timer = timer;
    }
    
}
