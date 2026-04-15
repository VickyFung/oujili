package com.mars.biz.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mars.biz.entity.University;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 大学 Service
 * 
 * @author Mars
 * @date 2026-04-15
 */
public interface UniversityService {

    /**
     * 分页查询
     */
    Page<University> page(Integer page, Integer pageSize, Integer id, String name);

    /**
     * 根据ID查询
     */
    University getById(Integer id);

    /**
     * 新增
     */
    void create(University university);

    /**
     * 修改
     */
    void update(University university);

    /**
     * 删除
     */
    void delete(Integer[] ids);

    /**
     * 导出数据列表
     */
    List<University> listForExport(java.util.List<Integer> ids, Integer id, String name);

    /**
     * 导入数据
     */
    Map<String, Object> importData(MultipartFile file);
}
