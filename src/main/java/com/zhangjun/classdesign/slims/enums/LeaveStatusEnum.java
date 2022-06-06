package com.zhangjun.classdesign.slims.enums;

/**
 * @author 张钧
 * @Description
 * @create 2022-05-29 14:09
 */
public enum LeaveStatusEnum {
    /**
     *0：未批准 1：已批准 2：已拒绝 3：已取消 4：已销假
     */
    NOT_APPROVED(0, "未批准"),
    APPROVED(1, "已批准"),
    REFUSED(2, "已拒绝"),
    CANCELED(3, "已取消"),
    TERMINATED(4, "已销假");
    private final Integer code;
    private final String roleName;

    LeaveStatusEnum(Integer code, String roleName) {
        this.code = code;
        this.roleName = roleName;
    }

    public Integer getCode() {
        return code;
    }

    public String getRoleName() {
        return roleName;
    }
}
