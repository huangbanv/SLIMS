package com.zhangjun.classdesign.slims.interceptor;

import com.alibaba.fastjson.JSON;
import com.zhangjun.classdesign.slims.entity.User;
import com.zhangjun.classdesign.slims.util.Result;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 张钧
 * @Description 权限拦截器，拦截除了登录以外的所有请求
 * @create 2022-05-29 12:19
 */
public class MyInterceptor implements HandlerInterceptor {

    public static ThreadLocal<User> threadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        if(loginUser == null){
            response.getWriter().write(Result.error(300, "请先登录").toString());
            return false;
        }
        threadLocal.set(loginUser);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
