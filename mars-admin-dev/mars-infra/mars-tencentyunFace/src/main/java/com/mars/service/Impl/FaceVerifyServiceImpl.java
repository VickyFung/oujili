package com.mars.service.Impl;
 
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mars.common.config.TencentCloudConfig;
import com.mars.service.IFaceVerifyService;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.faceid.v20180301.FaceidClient;
import com.tencentcloudapi.faceid.v20180301.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
 
 
/**
 * @Description: 人脸核验
 * @Author: yangfeng
 * @Date: 2024-04-01
 * @Version: V1.0
 */
@Slf4j
@Service
public class FaceVerifyServiceImpl implements IFaceVerifyService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String CACHE_KEY_PREFIX = "sys:config:group:";
 
    /**
     * 实名核身鉴权
     *
     * @return
     */
    @Override
    public DetectAuthResponse detectAuth(String examId, String realName, String idCard) {
        DetectAuthResponse resp = null;
        TencentCloudConfig config = getConfig();
        // 将字符串
        try {
            String secretId = config.getSecretId();
            String secretKey = config.getSecretKey();
            String faceApi = config.getFaceApi();
            String ruleId = config.getRuleId();
            String redirectUrl = config.getRedirectUrl();
            Credential cred = new Credential(secretId, secretKey);
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint(faceApi);
            httpProfile.setConnTimeout(10); // 请求连接超时时间，单位为秒(默认60秒)
            httpProfile.setWriteTimeout(10);  // 设置写入超时时间，单位为秒(默认0秒)
            httpProfile.setReadTimeout(10);
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            FaceidClient client = new FaceidClient(cred, "", clientProfile);
            DetectAuthRequest req = new DetectAuthRequest();
            req.setRuleId(ruleId);
            req.setIdCard(idCard);
            req.setName(realName);
            req.setRedirectUrl(redirectUrl + examId + "&isNeedFaceDetect=" + true);
            resp = client.DetectAuth(req);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }

    public TencentCloudConfig getConfig() {
        String jsonStr = redisTemplate.opsForValue().get(CACHE_KEY_PREFIX + "tencentyunMp");
        // 将字符串
        ObjectMapper mapper = new ObjectMapper();
        TencentCloudConfig config = null;
        try {
            config = mapper.readValue(jsonStr, TencentCloudConfig.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return config;
    }


    /**
     * 获取实名核身结果信息
     *
     * @param bizToken
     * @return
     */
    @Override
    public GetDetectInfoEnhancedResponse getDetectInfo(String bizToken) {
        GetDetectInfoEnhancedResponse resp = null;
        TencentCloudConfig config = getConfig();
        String secretId = config.getSecretId();
        String secretKey = config.getSecretKey();
        String faceApi = config.getFaceApi();
        String ruleId = config.getRuleId();
        try {
            Credential cred = new Credential(secretId, secretKey);
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint(faceApi);
            httpProfile.setConnTimeout(10); // 请求连接超时时间，单位为秒(默认60秒)
            httpProfile.setWriteTimeout(10);  // 设置写入超时时间，单位为秒(默认0秒)
            httpProfile.setReadTimeout(10);
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            FaceidClient client = new FaceidClient(cred, "", clientProfile);
            GetDetectInfoEnhancedRequest req = new GetDetectInfoEnhancedRequest();
            req.setBizToken(bizToken);
            req.setInfoType("0");
            req.setRuleId(ruleId);
            resp = client.GetDetectInfoEnhanced(req);
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }
        return resp;
    }
 
    /**
     * 实名核身是否通过
     *
     * @param bizToken
     * @return
     */
    @Override
    public boolean faceDetectIsPass(String bizToken) {
        GetDetectInfoEnhancedResponse resp = getDetectInfo(bizToken);
        if (!Objects.isNull(resp)) {
            DetectInfoText text = resp.getText();
            return !Objects.isNull(text) && text.getErrCode().intValue() == 0;
        }
        return false;
    }
 
 
}