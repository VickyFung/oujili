package com.mars.biz.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.alibaba.excel.EasyExcel;
import com.mars.common.result.PageResult;
import com.mars.common.result.Result;
import com.mars.system.annotation.Log;
import com.mars.system.annotation.Log.BusinessType;
import com.mars.biz.entity.AreaCode;
import com.mars.biz.service.AreaCodeService;
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
 * 行政区划表
 * 
 * @author Mars
 * @date 2026-04-15
 */
@RestController
@RequestMapping("/biz/code")
@RequiredArgsConstructor
public class AreaCodeController {

    private final AreaCodeService areaCodeService;

    /**
     * 分页查询
     */
    @GetMapping("/page")
    @SaCheckPermission("biz:code:list")
    public Result<PageResult<AreaCode>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer code,
            @RequestParam(required = false) String name) {
        var result = areaCodeService.page(page, pageSize, code, name);
        return Result.ok(PageResult.of(result));
    }

    /**
     * 获取详情
     */
    @GetMapping("/{code}")
    @SaCheckPermission("biz:code:query")
    public Result<AreaCode> getInfo(@PathVariable Integer code) {
        return Result.ok(areaCodeService.getById(code));
    }

    /**
     * 新增
     */
    @PostMapping
    @SaCheckPermission("biz:code:add")
    @Log(title = "行政区划表", businessType = BusinessType.INSERT)
    public Result<Void> add(@RequestBody AreaCode areaCode) {
        areaCodeService.create(areaCode);
        return Result.ok();
    }

    /**
     * 修改
     */
    @PutMapping
    @SaCheckPermission("biz:code:edit")
    @Log(title = "行政区划表", businessType = BusinessType.UPDATE)
    public Result<Void> edit(@RequestBody AreaCode areaCode) {
        areaCodeService.update(areaCode);
        return Result.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/{codes}")
    @SaCheckPermission("biz:code:remove")
    @Log(title = "行政区划表", businessType = BusinessType.DELETE)
    public Result<Void> remove(@PathVariable Integer[] codes) {
        areaCodeService.delete(codes);
        return Result.ok();
    }

    /**
     * 导出
     */
    @GetMapping("/export")
    @SaCheckPermission("biz:code:export")
    @Log(title = "行政区划表", businessType = BusinessType.EXPORT)
    public void export(HttpServletResponse response,
            @RequestParam(required = false) Integer code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String ids) throws IOException {
        List<Integer> idList = null;
        if (ids != null && !ids.isEmpty()) {
            idList = Arrays.stream(ids.split(",")).map(String::trim).filter(s -> !s.isEmpty())
                    .map(s ->  Integer.parseInt(s)).collect(Collectors.toList());
        }
        List<AreaCode> list = areaCodeService.listForExport(idList, code, name, null, null);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("行政区划表数据", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), AreaCode.class).sheet("行政区划表").doWrite(list);
    }

    /**
     * 导入
     */
    @PostMapping("/import")
    @SaCheckPermission("biz:code:import")
    @Log(title = "行政区划表", businessType = BusinessType.IMPORT)
    public Result<Map<String, Object>> importData(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = areaCodeService.importData(file);
        return Result.ok(result);
    }

    /**
     * 下载导入模板
     */
    @GetMapping("/template")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("行政区划表导入模板", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), AreaCode.class).sheet("行政区划表").doWrite(new ArrayList<>());
    }
}
