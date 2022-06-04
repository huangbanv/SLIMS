package com.zhangjun.classdesign.slims.enums;

/**
 * @author 张钧
 * @Description
 * @create 2022-05-28 8:54
 */
public enum RoleEnum {
    /**
     *
     */
    SYSTEM_ADMIN("0", "系统管理员"),
    COLLEGE_ADMIN("1_1", "二级学院管理员"),
    SAD_ADMIN("1_2", "学工处管理员"),
    COLLEGE_INSTRUCTOR("2_1", "学院辅导员"),
    SAD_WORKER("2_2", "学工处工作人员"),
    STUDENT("3_1", "学生");
    private final String code;
    private final String roleName;

    RoleEnum(String code, String roleName) {
        this.code = code;
        this.roleName = roleName;
    }

    public String getCode() {
        return code;
    }

    public String getRoleName() {
        return roleName;
    }

}
