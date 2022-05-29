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
    SYSTEM_ADMIN("0", "系统管理处"),
    COLLEGE("1_1", "二级学院"),
    STUDENT_AFFAIRS_DIVISION("1_2", "学生工作处"),
    COLLEGE_ADMIN("2_1", "学院管理员"),
    SAD_ADMIN("2_2", "学工处管理员"),
    COLLEGE_INSTRUCTOR("3_1", "学院辅导员"),
    SAD_WORKER("3_2", "学工处工作人员"),
    STUDENT("4_1", "学生");
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
