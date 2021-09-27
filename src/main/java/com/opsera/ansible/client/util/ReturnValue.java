package com.opsera.ansible.client.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * Formatted return value of Ansible command output
 * 
 * @author sreeni
 *
 */
@Data
@JsonInclude(value = Include.NON_NULL)
@NoArgsConstructor
public class ReturnValue {
        public enum Result {
                failed, changed, success, unreachable, unmanaged, unknown
        }

        private Result result;
        
        private int rc;
        
        private Map<String, Object> value;
        
        private List<String> stdout = new ArrayList<String>();

       
        
        /**
         * @return if result is changed or success, then true, else false
         */
        public boolean isSuccess() {
                return this.getResult()==Result.changed || this.getResult()==Result.success;
        }
        
        @Override
        public String toString() {
                return "rc="+rc+" result="+result+" value="+value;
        }

}
