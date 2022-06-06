package com.zhangjun.classdesign.slims.interceptor;

import com.alibaba.fastjson.JSON;
import com.zhangjun.classdesign.slims.enums.RoleEnum;
import com.zhangjun.classdesign.slims.util.Result;
import com.zhangjun.classdesign.slims.util.RoleCheck;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 张钧
 * @Description
 * @create 2022-06-06 17:16
 */
public class SADAdminInterceptor implements HandlerInterceptor{

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(RoleCheck.getUser().getRoleId().equals(RoleEnum.SAD_ADMIN.getCode())){
            response.getWriter().write(JSON.toJSONString(Result.error(500, "您的权限不足以访问该请求")));
            return false;
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
