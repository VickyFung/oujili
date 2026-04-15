package com.mars.common.enums.user;

public enum AuthStatusEnum {

    Unverified("未认证"),
    Verified("已认证"),
    Failed("认证失败");

    private String desc;

    AuthStatusEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
