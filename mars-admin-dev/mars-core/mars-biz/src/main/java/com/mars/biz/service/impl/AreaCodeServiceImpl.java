package com.mars.biz.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mars.biz.entity.AreaCode;
import com.mars.biz.mapper.AreaCodeMapper;
import com.mars.biz.service.AreaCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * 行政区划表 Service 实现
 * 
 * @author Mars
 * @date 2026-04-15
 */
@Service
@RequiredArgsConstructor
public class AreaCodeServiceImpl implements AreaCodeService {

    private final AreaCodeMapper areaCodeMapper;

    @Override
    public Page<AreaCode> page(Integer page, Integer pageSize, Integer code, String name) {
        Page<AreaCode> pageParam = new Page<>(page, pageSize);
        LambdaQueryWrapper<AreaCode> wrapper = new LambdaQueryWrapper<>();
        if (code != null) {
            wrapper.eq(AreaCode::getCode, code);
        }
        if (StringUtils.hasText(name)) {
            wrapper.eq(AreaCode::getName, name);
        }
        wrapper.orderByDesc(AreaCode::getCode);
        return areaCodeMapper.selectPage(pageParam, wrapper);
    }

    @Override
    public AreaCode getById(Integer code) {
        return areaCodeMapper.selectById(code);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(AreaCode areaCode) {
        areaCodeMapper.insert(areaCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AreaCode areaCode) {
        areaCodeMapper.updateById(areaCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer[] codes) {
        areaCodeMapper.deleteBatchIds(Arrays.asList(codes));
    }

    @Override
    public List<AreaCode> listForExport(List<Integer> ids, Integer code, String name, String level, Integer pcode) {
        LambdaQueryWrapper<AreaCode> wrapper = new LambdaQueryWrapper<>();
        if (ids != null && !ids.isEmpty()) {
            wrapper.in(AreaCode::getCode, ids);
        } else {
            if (code != null) {
                wrapper.eq(AreaCode::getCode, code);
            }
            if (StringUtils.hasText(name)) {
                wrapper.eq(AreaCode::getName, name);
            }
            if (StringUtils.hasText(level)) {
                wrapper.eq(AreaCode::getLevel, level);
            }
            if (pcode != null) {
                wrapper.eq(AreaCode::getPcode, pcode);
            }
        }
        wrapper.orderByDesc(AreaCode::getCode);
        return areaCodeMapper.selectList(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importData(MultipartFile file) {
        int success = 0;
        int fail = 0;
        List<String> errors = new ArrayList<>();
        try {
            List<AreaCode> list = EasyExcel.read(file.getInputStream(), AreaCode.class, null).sheet().doReadSync();
            for (int i = 0; i < list.size(); i++) {
                try {
                    AreaCode item = list.get(i);
                    if (item != null) {
                        item.setCode(null);
                        areaCodeMapper.insert(item);
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
