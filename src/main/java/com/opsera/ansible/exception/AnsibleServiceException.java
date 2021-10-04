package com.opsera.ansible.exception;

/**
 * @author sreeni
 *
 */
public class AnsibleServiceException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final String code;

    /**
     * @param message
     */
    public AnsibleServiceException(String message) {
        super(message);
        this.code = "";
    }

    /**
     * 
     * @param message String
     * @param cause   Throwable
     * @param code    String
     */
    public AnsibleServiceException(String message, Throwable cause, String code) {
        super(message, cause);
        this.code = code;
    }

    /**
     * 
     * @param code    String
     * @param message String
     */
    public AnsibleServiceException(String code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 
     * @param cause Throwable
     * @param code  String
     */
    public AnsibleServiceException(Throwable cause, String code) {
        super(cause);
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

}
