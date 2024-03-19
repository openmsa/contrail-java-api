//
// Automatically generated.
//
package net.juniper.contrail.api.types;

import java.util.List;
import java.util.ArrayList;

import net.juniper.contrail.api.ApiPropertyBase;

public class ExecutableInfoType extends ApiPropertyBase {
    String executable_path;
    String executable_args;
    Integer job_completion_weightage;
    public ExecutableInfoType() {
    }
    public ExecutableInfoType(String executable_path, String executable_args, Integer job_completion_weightage) {
        this.executable_path = executable_path;
        this.executable_args = executable_args;
        this.job_completion_weightage = job_completion_weightage;
    }
    public ExecutableInfoType(String executable_path) {
        this(executable_path, null, 100);    }
    public ExecutableInfoType(String executable_path, String executable_args) {
        this(executable_path, executable_args, 100);    }
    
    public String getExecutablePath() {
        return executable_path;
    }
    
    public void setExecutablePath(String executable_path) {
        this.executable_path = executable_path;
    }
    
    
    public String getExecutableArgs() {
        return executable_args;
    }
    
    public void setExecutableArgs(String executable_args) {
        this.executable_args = executable_args;
    }
    
    
    public Integer getJobCompletionWeightage() {
        return job_completion_weightage;
    }
    
    public void setJobCompletionWeightage(Integer job_completion_weightage) {
        this.job_completion_weightage = job_completion_weightage;
    }
    
}
