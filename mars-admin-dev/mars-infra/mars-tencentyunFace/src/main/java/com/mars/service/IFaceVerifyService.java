package com.mars.service;

import com.tencentcloudapi.faceid.v20180301.models.DetectAuthResponse;
import com.tencentcloudapi.faceid.v20180301.models.GetDetectInfoEnhancedResponse;

public interface IFaceVerifyService {
    DetectAuthResponse detectAuth(String examId, String realName, String idCard);
    boolean faceDetectIsPass(String bizToken);
    GetDetectInfoEnhancedResponse getDetectInfo(String bizToken);
}
