package com.mars.app.vo;

import lombok.Data;

@Data
public class SendMessageRequest {
        private String toUserId;
        private String content;
        private Integer syncOtherMachine; // 1-同步 2-不同步，默认1
}