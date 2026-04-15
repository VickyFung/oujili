package com.mars.app.service;

import com.mars.app.vo.AreaResponse;
import com.mars.app.vo.UserInfoRequest;
import com.mars.app.vo.UserInfoResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IAppUserService {
    /**
     * 更新用户信息
     */
    void updateUser(UserInfoRequest userInfo);

    UserInfoResponse getInfo(Long id);

    List<UserInfoResponse> recommendList();

    void upload(MultipartFile file, Long groupId);

    List<String> university(String name);

    List<AreaResponse> province();

    List<AreaResponse> city(Integer provinceCode);
}
