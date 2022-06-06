package com.zhangjun.classdesign.slims.interceptor;

import com.alibaba.fastjson.JSON;
import com.zhangjun.classdesign.slims.entity.User;
import com.zhangjun.classdesign.slims.util.Result;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

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
        response.setHeader("Access-Control-Allow-Origin",request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        response.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        final HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");
        if(loginUser == null){
            response.getWriter().write(JSON.toJSONString(Result.error(300, "请先登录")));
            return false;
        }
        ServletContext application = session.getServletContext();
        Map<String, String> loginMap = (Map<String, String>) application.getAttribute("loginMap");
        if (loginMap == null || loginMap.size() == 0) {
            response.getWriter().write(JSON.toJSONString(Result.error(300, "请先登录")));
            return false;
        } else {
            for (String key : loginMap.keySet()) {
                if (loginUser.getAccount().equals(key)) {
                    if (!session.getId().equals(loginMap.get(key))) {
                        session.invalidate();
                        response.getWriter().write(JSON.toJSONString(Result.error(300, "您的账户已在其他设备上登录")));
                        return false;
                    }
                }
            }
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
