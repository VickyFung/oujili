package com.mars.app.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.mars.app.vo.UserAuthRequest;
import com.mars.biz.entity.User;
import com.mars.biz.service.impl.UserServiceImpl;
import com.mars.common.result.Result;
import com.mars.service.IFaceVerifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: 人脸核验（目前仅适用微信h5）
 * @Author: yangfeng
 * @Date: 2024-04-01
 * @Version: V1.0
 */
@RestController
@RequestMapping("/app/faceVerify")
@Slf4j
public class ApiFaceVerifyController {
    @Autowired
    private IFaceVerifyService faceVerifyService;

    @Autowired
    private UserServiceImpl userService;
    /**
     * 实名核身鉴权
     *
     * @return
     */
    @PostMapping(value = "/detectAuth")
    public Result<?> detectAuth(@RequestBody UserAuthRequest  request) {
        log.info("考试:{},实名核身鉴权开始...", request.getExamId());
        Long userId = StpUtil.getLoginIdAsLong();
        User user = userService.getById(userId);
        user.setIdCard(request.getIdCard());
        userService.update(user);
        return Result.ok(faceVerifyService.detectAuth(request.getExamId(), user.getNickname(), request.getIdCard()));
    }
 
    /**
     * 获取实名核身结果信息
     *
     * @return
     */
    @PostMapping(value = "/getDetectInfo")
    public Result<?> getDetectInfo(@RequestBody UserAuthRequest  request) {
        log.info("获取实名核身结果信息，bizToken:{}", request.getBizToken());
        return Result.ok(faceVerifyService.getDetectInfo(request.getBizToken()));
    }
 
    /**
     * 实名核身是否通过
     *
     * @return
     */
    @PostMapping(value = "/faceDetectIsPass")
    public Result<?> faceDetectIsPass(@RequestBody UserAuthRequest  request) {
        log.info("调用实名核身是否通过接口，bizToken:{}", request.getBizToken());
        return Result.ok(faceVerifyService.faceDetectIsPass(request.getBizToken()));
    }

    /**
     * 更新认证状态
     */
    @PostMapping(value = "/updateAuthStatus")
    public Result<?> updateAuthStatus(@RequestBody UserAuthRequest request) {
        Long userId = StpUtil.getLoginIdAsLong();
        User user = userService.getById(userId);
        user.setAuthStatus(request.getAuthStatus().name());
        userService.update(user);
        return Result.ok();
    }
}