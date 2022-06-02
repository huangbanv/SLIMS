package com.zhangjun.classdesign.slims.enums;

import org.omg.CORBA.NO_PERMISSION;

/**
 * @author 张钧
 */

public enum HttpStatus {
    /**
     *
     */
    ERROR(500,"请求错误"),
    NO_PERMISSION(400,"没有权限，您的行为将被记录"),
    SUCCESS(200,"请求成功");
    
    
    private final Integer code;
    private final String message;
    HttpStatus(Integer code,String message){
        this.code = code;
        this.message = message;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
}
