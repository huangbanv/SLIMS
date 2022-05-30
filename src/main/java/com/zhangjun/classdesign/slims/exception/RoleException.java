package com.zhangjun.classdesign.slims.exception;

/**
 * @author 张钧
 */
public class RoleException extends Exception{
    
    public RoleException() {
        super();
    }
    
    public RoleException(String message) {
        super(message);
    }
    
    public RoleException(String message, Throwable cause) {
        super(message, cause);
    }
}
