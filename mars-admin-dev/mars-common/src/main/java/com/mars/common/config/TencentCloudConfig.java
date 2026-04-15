package com.mars.common.config;

import lombok.Data;

@Data
public class TencentCloudConfig {
    /**
     * 全局人脸启用开关
     */
    private Boolean globalFaceEnable;
    
    /**
     * 腾讯云人脸核身规则ID
     */
    private String ruleId;
    
    /**
     * 腾讯云API密钥SecretId
     */
    private String secretId;

    /**
     * 腾讯云API密钥SecretKey
     */
    private String secretKey;
    
    /**
     * 人脸API地址，默认 https://face.tencentcloudapi.com
     */
    private String faceApi;
    
    /**
     * 核身完成后的重定向URL
     */
    private String redirectUrl;

    /**
     * SDKAppID
     */
    private String sdkAppId;

    /**
     * 腾讯云通信密钥
     */
    private String chatSecretKey;
}