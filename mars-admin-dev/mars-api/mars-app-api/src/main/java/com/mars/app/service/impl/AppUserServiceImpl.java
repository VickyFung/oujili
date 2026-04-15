package com.mars.app.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.mars.app.service.IAppUserService;
import com.mars.app.vo.AreaResponse;
import com.mars.app.vo.UserInfoRequest;
import com.mars.app.vo.UserInfoResponse;
import com.mars.biz.entity.AreaCode;
import com.mars.biz.entity.University;
import com.mars.biz.entity.User;
import com.mars.biz.service.impl.AreaCodeServiceImpl;
import com.mars.biz.service.impl.UniversityServiceImpl;
import com.mars.biz.service.impl.UserServiceImpl;
import com.mars.common.enums.user.GenderEnum;
import com.mars.common.exception.BusinessException;
import com.mars.file.entity.SysFile;
import com.mars.file.service.impl.SysFileServiceImpl;
import com.mars.system.entity.SysUser;
import com.mars.system.service.impl.SysUserServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppUserServiceImpl implements IAppUserService {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private SysUserServiceImpl sysUserService;
    @Autowired
    private SysFileServiceImpl fileService;
    @Autowired
    private AreaCodeServiceImpl areaCodeService;
    @Autowired
    private UniversityServiceImpl universityService;

    @Override
    public void updateUser(UserInfoRequest userInfo) {
        Long userId = StpUtil.getLoginIdAsLong();
        SysUser byId = sysUserService.getById(userId);
        if (byId == null) {
            throw new BusinessException("用户不存在");
        }
        User user = new User();
        BeanUtils.copyProperties(userInfo, user);
        user.setUpdatedAt(LocalDateTime.now());
        userService.update(user);
    }

    @Override
    public UserInfoResponse getInfo(Long id) {
        User user = userService.getById(id);
        SysUser byId = sysUserService.getById(id);
        UserInfoResponse response = new UserInfoResponse();
        if (user == null){
            throw new BusinessException("用户不存在");
        }
        BeanUtils.copyProperties(user, response);
        response.setPhone(byId.getPhone());
        return response;
    }

    @Override
    public List<UserInfoResponse> recommendList() {
        long userId = StpUtil.getLoginIdAsLong();
        User user = userService.getById(userId);
        User recommendUser = new User();
        if (user != null){
            recommendUser.setGender(user.getGender()== GenderEnum.MALE.getValue()?GenderEnum.FEMALE.getValue():GenderEnum.MALE.getValue());
            List<User> users = userService.listForExport(null, recommendUser);
            List<UserInfoResponse> list = users.stream().map(user1 -> {
                UserInfoResponse response = new UserInfoResponse();
                BeanUtils.copyProperties(user1, response);
                return response;
            }).toList();
            return list;
        }
        return List.of();
    }

    @Override
    public void upload(MultipartFile file, Long groupId) {
        long userId = StpUtil.getLoginIdAsLong();
        User user = userService.getById(userId);
        SysFile upload = fileService.upload(file, null, groupId);
        String filePath = upload.getFilePath();
        user.setAvatar(filePath);
        userService.update(user);
    }

    @Override
    public List<String> university(String name) {
        List<University> universities = universityService.listForExport(null, null, name);
        if (universities != null && universities.size() > 0) {
            return universities.stream().map(University::getName).toList();
        }
        return List.of();
    }

    @Override
    public List<AreaResponse> province() {
        List<AreaCode> list = areaCodeService.listForExport(null, null, null, "province", null);
        if (list != null && list.size() > 0) {
            return list.stream().map(areaCode -> {
                AreaResponse response = new AreaResponse();
                BeanUtils.copyProperties(areaCode, response);
                return response;
            }).toList();
        }
        return List.of();
    }

    @Override
    public List<AreaResponse> city(Integer provinceCode) {
        List<AreaCode> list = areaCodeService.listForExport(null, null, null, "city", provinceCode);
        if (list != null && list.size() > 0) {
            return list.stream().map(areaCode -> {
                AreaResponse response = new AreaResponse();
                BeanUtils.copyProperties(areaCode, response);
                return response;
            }).toList();
        }
        return List.of();
    }
}
