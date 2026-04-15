package com.mars.common.enums.user;

public enum RelationshipGoalEnum {
    Relationship("恋爱关系"),
    Marriage("婚姻"),
    CasualDate("轻松约会");
    private String value;
    RelationshipGoalEnum(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
