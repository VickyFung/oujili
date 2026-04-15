package com.mars.app.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.mars.app.vo.QueryRoamRequest;
import com.mars.app.vo.SendMessageRequest;
import com.mars.app.vo.WithdrawMessageRequest;
import com.mars.common.result.Result;
import com.mars.tencentyunIm.util.TencentCloudImUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/app/im")
public class TencentCloudImController {

    @Autowired
    private TencentCloudImUtil imUtil;

    /**
     * 1. 发送消息（单聊）
     *
     * 说明：发送方为当前登录用户（从 StpUtil 获取）
     */
    @PostMapping("/send")
    public Result<String> sendMessage(@RequestBody SendMessageRequest request) {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        String fromUserId = currentUserId.toString();
        String toUserId = request.getToUserId();
        String content = request.getContent();
        Integer syncOtherMachine = request.getSyncOtherMachine() == null ? 1 : request.getSyncOtherMachine();

        if (toUserId == null || toUserId.trim().isEmpty()) {
            return Result.fail("接收方用户ID不能为空");
        }
        if (content == null || content.trim().isEmpty()) {
            return Result.fail("消息内容不能为空");
        }

        String result = imUtil.sendMsg(syncOtherMachine, fromUserId, toUserId, "TIMTextElem", content);
        return Result.ok(result);
    }

    /**
     * 2. 撤回消息（管理员或消息发送者）
     *
     */
    @PostMapping("/withdraw")
    public Result<String> withdrawMessage(@RequestBody WithdrawMessageRequest request) {
        String fromUserId = request.getFromUserId();
        String toUserId = request.getToUserId();
        String msgKey = request.getMsgKey();

        if (fromUserId == null || toUserId == null || msgKey == null) {
            return Result.fail("参数不完整：fromUserId, toUserId, msgKey 均为必填");
        }

        imUtil.adminMsgWithDraw(fromUserId, toUserId, msgKey);
        return Result.ok("撤回成功");
    }

    /**
     * 3. 查看聊天记录（管理员查询任意两人的历史消息）
     *
     */
    @PostMapping("/roam")
    public Result<String> queryRoamMessages(@RequestBody QueryRoamRequest request) {
        String fromUserId = request.getFromUserId();
        String toUserId = request.getToUserId();
        Integer maxCnt = request.getMaxCnt() == null ? 20 : request.getMaxCnt();
        Long startTime = request.getStartTime();
        Long endTime = request.getEndTime();
        String lastMsgKey = request.getLastMsgKey();

        if (fromUserId == null || toUserId == null) {
            return Result.fail("发送方和接收方用户ID不能为空");
        }
        if (startTime == null || endTime == null) {
            return Result.fail("起始时间和结束时间不能为空");
        }

        String result = imUtil.adminGetRoamMsg(fromUserId, toUserId, maxCnt, startTime, endTime, lastMsgKey);
        return Result.ok(result);
    }

    /**
     * 4. 聊天列表（获取当前登录用户的会话列表）
     * 请求参数示例：?limit=50
     * 返回当前用户的所有最近联系人会话
     */
    @GetMapping("/conversation/list")
    public Result<String> getConversationList(@RequestParam(required = false, defaultValue = "100") Integer limit) {
        Long currentUserId = StpUtil.getLoginIdAsLong();
        String userId = currentUserId.toString();
        String result = imUtil.getRecentContactList(userId, limit);
        return Result.ok(result);
    }
}