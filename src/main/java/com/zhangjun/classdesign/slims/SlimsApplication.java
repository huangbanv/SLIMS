package com.zhangjun.classdesign.slims;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author 张钧的电脑
 */
@SpringBootApplication
@MapperScan("com.zhangjun.classdesign.slims.mapper")
public class SlimsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SlimsApplication.class, args);
    }

}
