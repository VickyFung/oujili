package com.mars.common.enums.user;

public enum GenderEnum {
    MALE("男",1),
    FEMALE("女",2);

    private Integer value;

    private String desc;

    private GenderEnum(String desc, Integer value) {
        this.value = value;
        this.desc = desc;
    }

    public String getDesc() {
        return this.desc;
    }
    public Integer getValue() {
        return this.value;
    }
}