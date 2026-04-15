package com.mars.tencentyunIm.util;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mars.common.config.TencentCloudConfig;
import com.mars.tencentyunIm.constant.TencentCloudImConstant;
import com.mars.tencentyunIm.enums.TencentCloudImApiEnum;
import com.tencentyun.TLSSigAPIv2;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TencentCloudImUtil {
    private static final String HTTPS_URL_PREFIX = "https://console.tim.qq.com/";
    private static final String APP_MANAGER = "administrator";
    private static final String REDIS_IM_USER_SIG = "silence:im_user_sig:";
    private static final String CACHE_KEY_PREFIX = "sys:config:group:";

    @Autowired
    private StringRedisTemplate redisTemplate;
 
    /**
     * 获取腾讯云用户签名
     */
    public String getTxCloudUserSig() {
        TencentCloudConfig config = this.getConfig();
        String userSig = redisTemplate.opsForValue().get(REDIS_IM_USER_SIG + APP_MANAGER);;
        if (StringUtils.isEmpty(userSig)) {
            TLSSigAPIv2 tlsSigApi = new TLSSigAPIv2(new Long(config.getSdkAppId()), config.getChatSecretKey());
            userSig = tlsSigApi.genUserSig(APP_MANAGER, 86400);
            // 存入 Redis，有效期 86400 秒（24小时）
            redisTemplate.opsForValue().set(REDIS_IM_USER_SIG + APP_MANAGER, userSig, 86400, TimeUnit.SECONDS);
        }
        return userSig;
    }

    /**
     * 获取用户的会话列表（最近联系人）
     * @param userId 用户ID
     * @param limit 拉取数量，默认100
     * @return 会话列表 JSON 字符串
     */
    public String getRecentContactList(String userId, Integer limit) {
        Integer random = RandomUtils.nextInt(0, 999999999);
        String httpsUrl = getHttpsUrl("v4/recentcontact/get_list", random);  // 腾讯云 IM 获取会话列表 API
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("From_Account", userId);
        jsonObject.put("TimeStamp", 0);   // 0 表示从最新开始拉取
        jsonObject.put("StartIndex", 0);  // 从第0条开始
        jsonObject.put("MaxSize", limit == null ? 100 : limit);
        log.info("腾讯云im获取会话列表，请求参数：{}", jsonObject.toString());
        String result = HttpClientUtils.doPost(httpsUrl, jsonObject);
        log.info("腾讯云im获取会话列表，返回结果：{}", result);
        return result;
    }
 
    /**
     * 获取腾讯im请求路径
     */
    private String getHttpsUrl(String imServiceApi, Integer random) {
        TencentCloudConfig config = this.getConfig();
        return String.format("%s%s?sdkappid=%s&identifier=%s&usersig=%s&random=%s&contenttype=json",
                HTTPS_URL_PREFIX, imServiceApi, config.getSdkAppId(), APP_MANAGER, this.getTxCloudUserSig(), random);
    }

    public TencentCloudConfig getConfig() {
        String jsonStr = redisTemplate.opsForValue().get(CACHE_KEY_PREFIX + "tencentyunMp");
        // 将字符串
        ObjectMapper mapper = new ObjectMapper();
        TencentCloudConfig config = null;
        try {
            config = mapper.readValue(jsonStr, TencentCloudConfig.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return config;
    }
    /**
     * 导入单个账号
     * @param userId 用户id
     */
    public void accountImport(String userId) {
        accountImport(userId, null);
    }
 
    public void accountImport(String userId, String userName) {
        accountImport(userId, userName, null);
    }
 
    public void accountImport(String userId, String userName, String faceUrl) {
        Integer random = RandomUtils.nextInt(0, 999999999);
        String httpsUrl = getHttpsUrl(TencentCloudImApiEnum.ACCOUNT_IMPORT.getUrl(), random);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Identifier", userId);
        if (StringUtils.isNotEmpty(userName)) {
            jsonObject.put("Nick", userName);
        }
        if (StringUtils.isNotEmpty(faceUrl)) {
            jsonObject.put("FaceUrl", faceUrl);
        }
        log.info("腾讯云im导入单个账号，请求参数：{}", jsonObject.toString());
        String result = HttpClientUtils.doPost(httpsUrl, jsonObject);
        log.info("腾讯云im导入单个账号，返回结果：{}", result);
    }
 
    /**
     * 导入多个账号
     * @param userIds 用户id集合
     */
    public void multiAccountImport(List<String> userIds) {
        Integer random = RandomUtils.nextInt(0, 999999999);
        String httpsUrl = getHttpsUrl(TencentCloudImApiEnum.MULTI_ACCOUNT_IMPORT.getUrl(), random);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Accounts", userIds);
        log.info("腾讯云im导入多个账号，请求参数：{}", jsonObject.toString());
        String result = HttpClientUtils.doPost(httpsUrl, jsonObject);
        log.info("腾讯云im导入单个账户，返回结果：{}", result);
    }
 
    /**
     * 批量删除账号
     * @param userIds 用户id集合
     */
    public void accountDeleteBatch(List<String> userIds) {
        Integer random = RandomUtils.nextInt(0, 999999999);
        String httpsUrl = getHttpsUrl(TencentCloudImApiEnum.ACCOUNT_DELETE.getUrl(), random);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("DeleteItem", getUserIdJsonList(userIds));
        log.info("腾讯云im删除账号，请求参数：{}", jsonObject.toString());
        String result = HttpClientUtils.doPost(httpsUrl, jsonObject);
        log.info("腾讯云im删除账户，返回结果：{}", result);
    }
 
    /**
     * 查询账号是否已经导入im
     * @param userIds 用户id集合
     */
    public String accountCheck(List<String> userIds) {
        Integer random = RandomUtils.nextInt(0, 999999999);
        String httpsUrl = getHttpsUrl(TencentCloudImApiEnum.ACCOUNT_CHECK.getUrl(), random);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("CheckItem", getUserIdJsonList(userIds));
        log.info("腾讯云im查询账号，请求参数：{}", jsonObject.toString());
        String result = HttpClientUtils.doPost(httpsUrl, jsonObject);
        log.info("腾讯云im查询账号，返回结果：{}", result);
        return result;
    }
 
    private List<JSONObject> getUserIdJsonList(List<String> userIds) {
        return userIds.stream().map(v -> {
            JSONObject userIdJson = new JSONObject();
            userIdJson.put("UserID", v);
            return userIdJson;
        }).collect(Collectors.toList());
    }
 
    /**
     * 单发单聊消息
     * @param syncOtherMachine 是否同步消息到发送方（1-同步，2-不同步）
     * @param fromUserId 发送方用户id
     * @param toUserId 接收方用户id
     * @param msgType 消息对象类型
     * @param msgContent 消息内容
     */
    public String sendMsg(Integer syncOtherMachine, String fromUserId, String toUserId, String msgType, String msgContent) {
        Integer random = RandomUtils.nextInt(0, 999999999);
        String httpsUrl = getHttpsUrl(TencentCloudImApiEnum.SEND_MSG.getUrl(), random);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("SyncOtherMachine", syncOtherMachine);
        if (StringUtils.isNotEmpty(fromUserId)) {
            // 发送方不为空表示指定发送用户，为空表示为管理员发送消息
            jsonObject.put("From_Account", fromUserId);
        }
        jsonObject.put("To_Account", toUserId);
        jsonObject.put("MsgRandom", random);
        List<JSONObject> msgBody = getMsgBody(msgType, msgContent);
        jsonObject.put("MsgBody", msgBody);
        log.info("腾讯云im单发单聊消息，请求参数：{}", jsonObject.toString());
        String result = HttpClientUtils.doPost(httpsUrl, jsonObject);
        log.info("腾讯云im单发单聊消息，返回结果：{}", result);
        return result;
    }
 
    /**
     * 批量发单聊消息
     * @param syncOtherMachine 是否同步消息到发送方（1-同步，2-不同步）
     * @param fromUserId 发送方用户id
     * @param toUserIds 接收方用户id集合
     * @param msgType 消息对象类型
     * @param msgContent 消息内容
     */
    public String batchSendMsg(Integer syncOtherMachine, String fromUserId, List<String> toUserIds, String msgType, String msgContent) {
        Integer random = RandomUtils.nextInt(0, 999999999);
        String httpsUrl = getHttpsUrl(TencentCloudImApiEnum.BATCH_SEND_MSG.getUrl(), random);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("SyncOtherMachine", syncOtherMachine);
        if (StringUtils.isNotEmpty(fromUserId)) {
            // 发送方不为空表示指定发送用户，为空表示为管理员发送消息
            jsonObject.put("From_Account", fromUserId);
        }
        jsonObject.put("To_Account", toUserIds);
        jsonObject.put("MsgRandom", random);
        List<JSONObject> msgBody = getMsgBody(msgType, msgContent);
        jsonObject.put("MsgBody", msgBody);
        log.info("腾讯云im批量发单聊消息，请求参数：{}", jsonObject.toString());
        String result = HttpClientUtils.doPost(httpsUrl, jsonObject);
        log.info("腾讯云im批量发单聊消息，返回结果：{}", result);
        return result;
    }

    /**
     * 为指定用户生成UserSig（供小程序端登录用）
     * @param userId 用户ID（推荐使用微信openId）
     * @return UserSig字符串
     */
    public String generateUserSigForUser(String userId) {
        TencentCloudConfig config = getConfig();
        TLSSigAPIv2 tlsSigApi = new TLSSigAPIv2(new Long(config.getSdkAppId()), config.getChatSecretKey());
        // 有效期86400秒（24小时），可根据需求调整
        return tlsSigApi.genUserSig(userId, 86400);
    }
 
    /**
     * 拼接发送消息内容
     * @param msgType 消息类型
     * @param msgContent 发送消息内容
     * @return 消息内容
     */
    private List<JSONObject> getMsgBody(String msgType, String msgContent) {
        List<JSONObject> msgBody = new ArrayList<>();
        if (msgType.equals(TencentCloudImConstant.TIM_TEXT_ELEM)) {
            // 文本类型
            JSONObject msgBodyJson = new JSONObject();
            msgBodyJson.put("MsgType", msgType);
            JSONObject msgContentObj = new JSONObject();
            msgContentObj.put("Text", msgContent);
            msgBodyJson.put("MsgContent", msgContentObj);
            msgBody.add(msgBodyJson);
        }
        return msgBody;
    }
 
    /**
     * 查询单聊消息
     * @param fromUserId 发送方用户id
     * @param toUserId 接收方用户id
     * @param maxCnt 查询条数
     * @param startTime 起始时间（单位：秒）
     * @param endTime 结束时间（单位：秒）
     * @param lastMsgKey 最后一条消息的 MsgKey
     * @return 单聊消息列表
     */
    public String adminGetRoamMsg(String fromUserId, String toUserId, Integer maxCnt, Long startTime, Long endTime, String lastMsgKey) {
        Integer random = RandomUtils.nextInt(0, 999999999);
        String httpsUrl = getHttpsUrl(TencentCloudImApiEnum.ADMIN_GET_ROAM_MSG.getUrl(), random);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("From_Account", fromUserId);
        jsonObject.put("To_Account", toUserId);
        jsonObject.put("MaxCnt", maxCnt);
        jsonObject.put("MinTime", startTime);
        jsonObject.put("MaxTime", endTime);
        if (StringUtils.isNotEmpty(lastMsgKey)){
            jsonObject.put("LastMsgKey", lastMsgKey);
        }
        log.info("腾讯云im查询单聊消息，请求参数：{}", jsonObject.toString());
        String result = HttpClientUtils.doPost(httpsUrl, jsonObject);
        log.info("腾讯云im查询单聊消息，返回结果：{}", result);
        return result;
    }
 
    /**
     * 撤回单聊消息
     * @param fromUserId 发送方用户id
     * @param toUserId 接收方用户id
     * @param msgKey MsgKey
     */
    public void adminMsgWithDraw(String fromUserId, String toUserId, String msgKey) {
        Integer random = RandomUtils.nextInt(0, 999999999);
        String httpsUrl = getHttpsUrl(TencentCloudImApiEnum.ADMIN_MSG_WITH_DRAW.getUrl(), random);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("From_Account", fromUserId);
        jsonObject.put("To_Account", toUserId);
        jsonObject.put("MsgKey", msgKey);
        log.info("腾讯云im撤回单聊消息，请求参数：{}", jsonObject.toString());
        String result = HttpClientUtils.doPost(httpsUrl, jsonObject);
        log.info("腾讯云im撤回单聊消息，返回结果：{}", result);
    }
 
    /**
     * 设置单聊消息已读
     * @param reportUserId 读取消息的用户
     * @param peerUserId 发送消息的用户
     */
    public void adminSetMsgRead(String reportUserId, String peerUserId) {
        Integer random = RandomUtils.nextInt(0, 999999999);
        String httpsUrl = getHttpsUrl(TencentCloudImApiEnum.ADMIN_SET_MSG_READ.getUrl(), random);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Report_Account", reportUserId);
        jsonObject.put("Peer_Account", peerUserId);
        log.info("腾讯云im设置单聊消息已读，请求参数：{}", jsonObject.toString());
        String result = HttpClientUtils.doPost(httpsUrl, jsonObject);
        log.info("腾讯云im设置单聊消息已读，返回结果：{}", result);
    }
}