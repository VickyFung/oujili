package com.mars.app.vo;

import com.mars.common.enums.user.AuthStatusEnum;
import lombok.Data;

@Data
public class UserAuthRequest {
    private String examId;
    private String IdCard;
    private String bizToken;
    private AuthStatusEnum authStatus;
}
