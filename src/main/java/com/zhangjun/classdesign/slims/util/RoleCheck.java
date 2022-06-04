package com.zhangjun.classdesign.slims.util;

import com.zhangjun.classdesign.slims.entity.User;
import com.zhangjun.classdesign.slims.enums.RoleEnum;
import com.zhangjun.classdesign.slims.interceptor.MyInterceptor;

/**
 * @author 张钧
 * @Description
 * @create 2022-05-29 21:09
 */
public class RoleCheck {
    public static boolean isAdmin() {
        return MyInterceptor.threadLocal.get().getRoleId().equals(RoleEnum.SYSTEM_ADMIN.getCode());
    }
    public static boolean isCollegeAdmin() {
        return MyInterceptor.threadLocal.get().getRoleId().equals(RoleEnum.COLLEGE_ADMIN.getCode());
    }
    public static boolean isCollegeInstructor() {
        return MyInterceptor.threadLocal.get().getRoleId().equals(RoleEnum.COLLEGE_INSTRUCTOR.getCode());
    }
    public static boolean isStudent() {
        return MyInterceptor.threadLocal.get().getRoleId().equals(RoleEnum.STUDENT.getCode());
    }
    public static User getUser(){
        return MyInterceptor.threadLocal.get();
    }
}
