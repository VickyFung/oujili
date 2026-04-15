package com.mars.biz.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.alibaba.excel.EasyExcel;
import com.mars.common.result.PageResult;
import com.mars.common.result.Result;
import com.mars.system.annotation.Log;
import com.mars.system.annotation.Log.BusinessType;
import com.mars.biz.entity.User;
import com.mars.biz.service.UserService;
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
 * 用户信息表
 * 
 * @author Mars
 * @date 2026-04-12
 */
@RestController
@RequestMapping("/biz/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 分页查询
     */
    @GetMapping("/page")
    @SaCheckPermission("biz:user:list")
    public Result<PageResult<User>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String authStatus) {
        var result = userService.page(page, pageSize, id, nickname, status, authStatus);
        return Result.ok(PageResult.of(result));
    }

    /**
     * 获取详情
     */
    @GetMapping("/{id}")
    @SaCheckPermission("biz:user:query")
    public Result<User> getInfo(@PathVariable Long id) {
        return Result.ok(userService.getById(id));
    }

    /**
     * 新增
     */
    @PostMapping
    @SaCheckPermission("biz:user:add")
    @Log(title = "用户信息表", businessType = BusinessType.INSERT)
    public Result<Void> add(@RequestBody User user) {
        userService.create(user);
        return Result.ok();
    }

    /**
     * 修改
     */
    @PutMapping
    @SaCheckPermission("biz:user:edit")
    @Log(title = "用户信息表", businessType = BusinessType.UPDATE)
    public Result<Void> edit(@RequestBody User user) {
        userService.update(user);
        return Result.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/{ids}")
    @SaCheckPermission("biz:user:remove")
    @Log(title = "用户信息表", businessType = BusinessType.DELETE)
    public Result<Void> remove(@PathVariable Long[] ids) {
        userService.delete(ids);
        return Result.ok();
    }

    /**
     * 导出
     */
    @GetMapping("/export")
    @SaCheckPermission("biz:user:export")
    @Log(title = "用户信息表", businessType = BusinessType.EXPORT)
    public void export(HttpServletResponse response,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String authStatus,
            @RequestParam(required = false) String ids) throws IOException {
        List<Long> idList = null;
        if (ids != null && !ids.isEmpty()) {
            idList = Arrays.stream(ids.split(",")).map(String::trim).filter(s -> !s.isEmpty())
                    .map(s ->  Long.parseLong(s)).collect(Collectors.toList());
        }
        User user = new User();
        user.setId(id);
        user.setNickname(nickname);
        user.setStatus(status);
        user.setAuthStatus(authStatus);
        List<User> list = userService.listForExport(idList, user);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("用户信息表数据", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), User.class).sheet("用户信息表").doWrite(list);
    }

    /**
     * 导入
     */
    @PostMapping("/import")
    @SaCheckPermission("biz:user:import")
    @Log(title = "用户信息表", businessType = BusinessType.IMPORT)
    public Result<Map<String, Object>> importData(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = userService.importData(file);
        return Result.ok(result);
    }

    /**
     * 下载导入模板
     */
    @GetMapping("/template")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("用户信息表导入模板", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), User.class).sheet("用户信息表").doWrite(new ArrayList<>());
    }
}
