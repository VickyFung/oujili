package com.mars.app.vo;

import lombok.Data;

@Data
public class QueryRoamRequest {
        private String fromUserId;
        private String toUserId;
        private Integer maxCnt;
        private Long startTime;
        private Long endTime;
        private String lastMsgKey;
}