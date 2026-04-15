package com.mars.biz.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mars.biz.entity.User;
import com.mars.biz.mapper.UserMapper;
import com.mars.biz.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * 用户信息表 Service 实现
 * 
 * @author Mars
 * @date 2026-04-12
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public Page<User> page(Integer page, Integer pageSize, Integer id, String nickname, String status, String authStatus) {
        Page<User> pageParam = new Page<>(page, pageSize);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (id != null) {
            wrapper.eq(User::getId, id);
        }
        if (StringUtils.hasText(nickname)) {
            wrapper.eq(User::getNickname, nickname);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(User::getStatus, status);
        }
        if (StringUtils.hasText(authStatus)) {
            wrapper.eq(User::getAuthStatus, authStatus);
        }
        wrapper.orderByDesc(User::getId);
        return userMapper.selectPage(pageParam, wrapper);
    }

    @Override
    public User getById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(User user) {
        userMapper.insert(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(User user) {
        userMapper.updateById(user);
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long[] ids) {
        userMapper.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public List<User> listForExport(List<Long> ids, User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (ids != null && !ids.isEmpty()) {
            wrapper.in(User::getId, ids);
        } else {
            if (user.getId() != null) {
                wrapper.eq(User::getId, user.getId());
            }
            if (StringUtils.hasText(user.getNickname())) {
                wrapper.eq(User::getNickname, user.getNickname());
            }
            if (StringUtils.hasText(user.getStatus())) {
                wrapper.eq(User::getStatus, user.getStatus());
            }
            if (StringUtils.hasText(user.getAuthStatus())) {
                wrapper.eq(User::getAuthStatus, user.getAuthStatus());
            }
            if (StringUtils.hasText(String.valueOf(user.getGender()))){
                wrapper.eq(User::getGender, user.getGender());
            }
        }
        wrapper.orderByDesc(User::getId);
        return userMapper.selectList(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importData(MultipartFile file) {
        int success = 0;
        int fail = 0;
        List<String> errors = new ArrayList<>();
        try {
            List<User> list = EasyExcel.read(file.getInputStream(), User.class, null).sheet().doReadSync();
            for (int i = 0; i < list.size(); i++) {
                try {
                    User item = list.get(i);
                    if (item != null) {
                        item.setId(null);
                        userMapper.insert(item);
                        success++;
                    }
                } catch (Exception e) {
                    fail++;
                    errors.add("第" + (i + 2) + "行: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("读取文件失败: " + e.getMessage());
        }
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("fail", fail);
        result.put("errors", errors);
        return result;
    }
}
