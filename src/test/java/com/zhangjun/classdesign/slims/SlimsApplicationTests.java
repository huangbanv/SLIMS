package com.zhangjun.classdesign.slims;

import com.zhangjun.classdesign.slims.entity.Leave;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SlimsApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(new Leave().setStartTime("2022-06-01 15:41:15").setEndTime("2022-06-03 21:41:15").setDays().getDays());
    }

}
