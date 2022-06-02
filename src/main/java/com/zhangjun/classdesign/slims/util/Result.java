package com.zhangjun.classdesign.slims.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.zhangjun.classdesign.slims.enums.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 张钧
 * @Description
 * @create 2022-05-27 16:45
 */
public class Result extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public <T> T getData(String name,TypeReference<T> typeReference){
        Object data = get(name);
        String s = JSON.toJSONString(data);
        return JSON.parseObject(s,typeReference);

    }

    public <T> T getData(TypeReference<T> typeReference){
        Object data = get("data");
        String s = JSON.toJSONString(data);
        return JSON.parseObject(s,typeReference);

    }
    public Result setData(Object data){
        put("data",data);
        return this;
    }
    public Result() {
        put("code", HttpStatus.SUCCESS.getCode());
        put("msg", HttpStatus.SUCCESS.getMessage());
    }

    public static Result error() {
        return error(HttpStatus.ERROR.getMessage());
    }

    public static Result error(String msg) {
        return error(HttpStatus.ERROR.getCode(), msg);
    }

    public static Result error(int code, String msg) {
        Result r = new Result();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static Result ok(String msg) {
        Result r = new Result();
        r.put("msg", msg);
        return r;
    }

    public static Result ok(Map<String, Object> map) {
        Result r = new Result();
        r.putAll(map);
        return r;
    }

    public static Result ok() {
        return new Result();
    }

    @Override
    public Result put(String key, Object value) {
        super.put(key, value);
        return this;
    }
    public  Integer getCode() {
        return (Integer) this.get("code");
    }
}
