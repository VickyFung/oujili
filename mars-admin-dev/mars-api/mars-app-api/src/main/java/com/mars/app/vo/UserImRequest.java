package com.mars.app.vo;

import lombok.Data;

@Data
public class UserImRequest {
    private String userName;
    private String faceUrl;
    private Long toUserId;
    private String content;
}
