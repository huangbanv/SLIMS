package com.zhangjun.classdesign.slims.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 张钧
 * @Description
 * @create 2022-05-30 22:31
 */
@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 允许向该服务器提交请求的URI，*表示全部允许。。这里尽量限制来源域，比如http://xxxx:8080 ,以降低安全风险。。
                .allowedOriginPatterns("*")
                // 允许提交请求的方法，*表示全部允许，也可以单独设置GET、PUT等
                .allowedMethods("GET","POST","OPTIONS","PUT","DELETE")
                // 允许cookies跨域
                .allowCredentials(true)
                // 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
                .maxAge(18000L)
                // 允许访问的头信息,*表示全部
                .allowedHeaders("*");
    }
}
