package com.mars.biz.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.alibaba.excel.EasyExcel;
import com.mars.common.result.PageResult;
import com.mars.common.result.Result;
import com.mars.system.annotation.Log;
import com.mars.system.annotation.Log.BusinessType;
import com.mars.biz.entity.University;
import com.mars.biz.service.UniversityService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 大学
 * 
 * @author Mars
 * @date 2026-04-15
 */
@RestController
@RequestMapping("/biz/university")
@RequiredArgsConstructor
public class UniversityController {

    private final UniversityService universityService;

    /**
     * 分页查询
     */
    @GetMapping("/page")
    @SaCheckPermission("biz:university:list")
    public Result<PageResult<University>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String name) {
        var result = universityService.page(page, pageSize, id, name);
        return Result.ok(PageResult.of(result));
    }

    /**
     * 获取详情
     */
    @GetMapping("/{id}")
    @SaCheckPermission("biz:university:query")
    public Result<University> getInfo(@PathVariable Integer id) {
        return Result.ok(universityService.getById(id));
    }

    /**
     * 新增
     */
    @PostMapping
    @SaCheckPermission("biz:university:add")
    @Log(title = "大学", businessType = BusinessType.INSERT)
    public Result<Void> add(@RequestBody University university) {
        universityService.create(university);
        return Result.ok();
    }

    /**
     * 修改
     */
    @PutMapping
    @SaCheckPermission("biz:university:edit")
    @Log(title = "大学", businessType = BusinessType.UPDATE)
    public Result<Void> edit(@RequestBody University university) {
        universityService.update(university);
        return Result.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/{ids}")
    @SaCheckPermission("biz:university:remove")
    @Log(title = "大学", businessType = BusinessType.DELETE)
    public Result<Void> remove(@PathVariable Integer[] ids) {
        universityService.delete(ids);
        return Result.ok();
    }

    /**
     * 导出
     */
    @GetMapping("/export")
    @SaCheckPermission("biz:university:export")
    @Log(title = "大学", businessType = BusinessType.EXPORT)
    public void export(HttpServletResponse response,
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String ids) throws IOException {
        List<Integer> idList = null;
        if (ids != null && !ids.isEmpty()) {
            idList = Arrays.stream(ids.split(",")).map(String::trim).filter(s -> !s.isEmpty())
                    .map(s ->  Integer.parseInt(s)).collect(Collectors.toList());
        }
        List<University> list = universityService.listForExport(idList, id, name);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("大学数据", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), University.class).sheet("大学").doWrite(list);
    }

    /**
     * 导入
     */
    @PostMapping("/import")
    @SaCheckPermission("biz:university:import")
    @Log(title = "大学", businessType = BusinessType.IMPORT)
    public Result<Map<String, Object>> importData(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = universityService.importData(file);
        return Result.ok(result);
    }

    /**
     * 下载导入模板
     */
    @GetMapping("/template")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("大学导入模板", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), University.class).sheet("大学").doWrite(new ArrayList<>());
    }
}
