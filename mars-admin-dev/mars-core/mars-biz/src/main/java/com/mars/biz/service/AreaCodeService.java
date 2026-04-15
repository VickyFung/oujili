package com.mars.biz.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mars.biz.entity.AreaCode;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 行政区划表 Service
 * 
 * @author Mars
 * @date 2026-04-15
 */
public interface AreaCodeService {

    /**
     * 分页查询
     */
    Page<AreaCode> page(Integer page, Integer pageSize, Integer code, String name);

    /**
     * 根据ID查询
     */
    AreaCode getById(Integer code);

    /**
     * 新增
     */
    void create(AreaCode areaCode);

    /**
     * 修改
     */
    void update(AreaCode areaCode);

    /**
     * 删除
     */
    void delete(Integer[] codes);

    /**
     * 导出数据列表
     */
    List<AreaCode> listForExport(java.util.List<Integer> ids, Integer code, String name, String level, Integer pcode);

    /**
     * 导入数据
     */
    Map<String, Object> importData(MultipartFile file);
}
