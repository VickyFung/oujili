package com.mars.biz.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mars.biz.entity.University;
import com.mars.biz.mapper.UniversityMapper;
import com.mars.biz.service.UniversityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * 大学 Service 实现
 * 
 * @author Mars
 * @date 2026-04-15
 */
@Service
@RequiredArgsConstructor
public class UniversityServiceImpl implements UniversityService {

    private final UniversityMapper universityMapper;

    @Override
    public Page<University> page(Integer page, Integer pageSize, Integer id, String name) {
        Page<University> pageParam = new Page<>(page, pageSize);
        LambdaQueryWrapper<University> wrapper = new LambdaQueryWrapper<>();
        if (id != null) {
            wrapper.eq(University::getId, id);
        }
        if (StringUtils.hasText(name)) {
            wrapper.eq(University::getName, name);
        }
        wrapper.orderByDesc(University::getId);
        return universityMapper.selectPage(pageParam, wrapper);
    }

    @Override
    public University getById(Integer id) {
        return universityMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(University university) {
        universityMapper.insert(university);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(University university) {
        universityMapper.updateById(university);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer[] ids) {
        universityMapper.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public List<University> listForExport(List<Integer> ids, Integer id, String name) {
        LambdaQueryWrapper<University> wrapper = new LambdaQueryWrapper<>();
        if (ids != null && !ids.isEmpty()) {
            wrapper.in(University::getId, ids);
        } else {
            if (id != null) {
                wrapper.eq(University::getId, id);
            }
            if (StringUtils.hasText(name)) {
                wrapper.like(University::getName, name);
            }
        }
        wrapper.orderByDesc(University::getId);
        return universityMapper.selectList(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importData(MultipartFile file) {
        int success = 0;
        int fail = 0;
        List<String> errors = new ArrayList<>();
        try {
            List<University> list = EasyExcel.read(file.getInputStream(), University.class, null).sheet().doReadSync();
            for (int i = 0; i < list.size(); i++) {
                try {
                    University item = list.get(i);
                    if (item != null) {
                        item.setId(null);
                        universityMapper.insert(item);
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
