package com.mars.common.enums.user;

public enum MeetingExpectationEnum {
    MeetFirst("想先见面"),
    ChatFirst("先聊聊"),
    MeetIfAppropriate("合适再见面"),
    CasualDate("轻松约会");
    private String value;
    MeetingExpectationEnum(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
