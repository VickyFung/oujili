package com.mars.app.vo;

import lombok.Data;

@Data
public class WithdrawMessageRequest {
        private String fromUserId;
        private String toUserId;
        private String msgKey;
}