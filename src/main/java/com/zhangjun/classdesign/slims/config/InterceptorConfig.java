package com.zhangjun.classdesign.slims.config;

import com.zhangjun.classdesign.slims.interceptor.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 张钧
 * @Description
 * @create 2022-05-29 12:21
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MyInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/slims/auth");
        registry.addInterceptor(new StudentInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/slims/auth","/slims/leave","/slims/role/getRole","/slims/leave/**");
        registry.addInterceptor(new SADWorkerInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/slims/auth","/slims/clazz/listAll","/slims/leave","/slims/leave/byClazzAndTime",
                        "/slims/export","/slims/export/byCondition");
        registry.addInterceptor(new InstructorInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/slims/auth","/slims/class/**","/slims/class","/slims/leave","/slims/leave/**",
                        "/slims/export","/slims/export/byCondition","/slims/user","/slims/user/**","/slims/instructor/list",
                        "/slims/role/getRole","/slims/department/list","/slims/role","/slims/clazz/list");
        registry.addInterceptor(new SADAdminInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/slims/auth","/slims/clazz/listAll","/slims/leave","/slims/leave/byClazzAndTime",
                        "/slims/export","/slims/export/byCondition","/slims/user","/slims/user/**","/slims/department/list",
                        "/slims/role","/slims/clazz/list");
    }
}
