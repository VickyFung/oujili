package com.mars.biz.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mars.biz.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 用户信息表 Service
 * 
 * @author Mars
 * @date 2026-04-12
 */
public interface UserService {

    /**
     * 分页查询
     */
    Page<User> page(Integer page, Integer pageSize, Integer id, String nickname, String status, String authStatus);

    /**
     * 根据ID查询
     */
    User getById(Long id);

    /**
     * 新增
     */
    void create(User user);

    /**
     * 修改
     */
    void update(User user);

    /**
     * 删除
     */
    void delete(Long[] ids);

    /**
     * 导出数据列表
     */
    List<User> listForExport(java.util.List<Long> ids, User user);

    /**
     * 导入数据
     */
    Map<String, Object> importData(MultipartFile file);
}
