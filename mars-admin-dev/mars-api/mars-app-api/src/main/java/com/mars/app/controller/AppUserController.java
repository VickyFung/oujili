package com.mars.app.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.mars.app.service.impl.AppUserServiceImpl;
import com.mars.app.vo.AreaResponse;
import com.mars.app.vo.UserInfoRequest;
import com.mars.app.vo.UserInfoResponse;
import com.mars.biz.service.impl.UserServiceImpl;
import com.mars.common.result.Result;
import com.mars.file.service.impl.SysFileServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/app/user")
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserServiceImpl userService;


    /**
     * 新增用户信息
     */
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Valid UserInfoRequest user) {
        userService.updateUser(user);
        return Result.ok();
    }

    /**
     * 上传用户头像
     */
    @PostMapping("/upload")
    public Result<Void> upload(@RequestParam("file") MultipartFile file, @RequestParam(required = false) Long groupId) {
        userService.upload(file, groupId);
        return Result.ok();
    }

    /**
     * 获取用户信息
     */
    @PostMapping("/getInfo")
    public Result<UserInfoResponse> getInfo() {
        Long userId = StpUtil.getLoginIdAsLong();
        UserInfoResponse response= userService.getInfo(userId);
        return Result.ok(response);
    }

    /**
     * 用户推荐列表
     */
    @GetMapping("/recommend")
    public Result<List<UserInfoResponse>> recommend() {
        List<UserInfoResponse> list = userService.recommendList();
        return Result.ok(list);
    }

    /**
     * 大学信息
     */
    @GetMapping("/university")
    public Result<List<String>> university(@RequestParam String name) {
        List<String> list = userService.university(name);
        return Result.ok(list);
    }

    /**
     *查询省级地区
     */
    @GetMapping("/province")
    public Result<List<AreaResponse>> province() {
        List<AreaResponse> list = userService.province();
        return Result.ok(list);
    }
    /**
     * 查询市级地区
     */
    @GetMapping("/city")
    public Result<List<AreaResponse>> city(@RequestParam Integer provinceCode) {
        List<AreaResponse> list = userService.city(provinceCode);
        return Result.ok(list);
    }
}
