package com.opsera.ansible.ssh.exception;


/**
 * Timeout Exception
 * @author sreeni
 *
 */
public class TimeoutException extends SshException{

        /**
     * 
     */
    private static final long serialVersionUID = 6797622717030211748L;

        public TimeoutException(String message) {
                this(message, null);
        }
        
        public TimeoutException(String message, Throwable error) {
                super(message, error);
        }

        /**
         * 
         */


}
