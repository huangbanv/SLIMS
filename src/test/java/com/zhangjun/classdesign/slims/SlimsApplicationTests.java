package com.zhangjun.classdesign.slims;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhangjun.classdesign.slims.entity.*;
import com.zhangjun.classdesign.slims.enums.RoleEnum;
import com.zhangjun.classdesign.slims.mapper.*;
import com.zhangjun.classdesign.slims.service.LeaveService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@SpringBootTest
class SlimsApplicationTests {

    @Resource
    UserMapper userMapper;
    
    @Resource
    RoleUserGroupMapper roleUserGroupMapper;
    
    @Resource
    ClazzMapper clazzMapper;
    
    @Resource
    UserClazzGroupMapper userClazzGroupMapper;
    
    @Resource
    LeaveService leaveService;
    
    @Test
    void contextLoads() {
        List<Leave> leaves = new ArrayList<>(28*3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Random random = new Random();
        Map<Long, Long> clazzInstructorId = clazzMapper.selectList(new QueryWrapper<>()).stream().collect(Collectors.toMap(Clazz::getId, Clazz::getInstructorId));
        List<Long> userIds = roleUserGroupMapper.selectList(new QueryWrapper<RoleUserGroup>().eq("role_id", RoleEnum.STUDENT.getCode())).stream().map(RoleUserGroup::getUserId).collect(Collectors.toList());
        userIds.forEach(userid -> {
            for (int i = 0; i < 3; i++) {
                Leave leave = new Leave();
                UserClazzGroup studentClazz = userClazzGroupMapper.selectOne(new QueryWrapper<UserClazzGroup>().eq("student_id", userid));
                leave.setStudentId(userid).setInstructorId(clazzInstructorId.get(studentClazz.getClazzId()))
                        .setStatus(random.nextInt(5)).setType(random.nextInt(2)).setReason(userid+"---"+random.nextBoolean()+"---"+leave.getInstructorId())
                        .setStartTime(formatter.format(LocalDateTime.now())).setEndTime(formatter.format(LocalDateTime.now().minusDays(1).minusHours(2).minusMinutes(15)))
                        .setDays();
                leaves.add(leave);
                System.out.println(leave);
            }
        });
        leaveService.saveBatch(leaves);
    }

}
