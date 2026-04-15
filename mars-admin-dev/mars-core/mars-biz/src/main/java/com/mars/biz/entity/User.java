package com.mars.biz.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.io.Serializable;

/**
 * 用户信息表
 * 
 * @author Mars
 * @date 2026-04-12
 */
@Data
@TableName("fr_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 用户ID */
    @ExcelProperty(value = "用户ID", index = 0)
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 头像 */
    @ExcelProperty(value = "头像", index = 1)
    private String avatar;

    /** 昵称 */
    @ExcelProperty(value = "昵称", index = 2)
    private String nickname;

    /** 出生日期 */
    private LocalDate birthDate;

    /** 身高 */
    private Integer height;

    /** 职业 */
    @ExcelProperty(value = "职业", index = 3)
    private String occupation;

    /** 性别 */
    @ExcelProperty(value = "性别", index = 4)
    private Integer gender;

    /** 毕业学校 */
    private String graduationSchool;

    /** 最高学历 */
    @ExcelProperty(value = "最高学历", index = 5)
    private String highestDegree;

    /** 学历类型 */
    private String degreeType;

    /** 个人介绍 */
    private String bio;

    /** 兴趣爱好 */
    private String hobbies;

    /** 择偶要求 */
    private String preferences;

    /** 当前位置 */
    private String currentLocation;

    /** 见面预期 */
    private String meetingExpectation;

    /** 期望关系 */
    private String relationshipGoal;

    /** 情感状态 */
    private String maritalStatus;

    /** 家乡 */
    private String hometown;

    /** 用户状态 */
    @ExcelProperty(value = "用户状态", index = 6)
    private String status;

    /** 用户认证状态 */
    @ExcelProperty(value = "用户认证状态", index = 7)
    private String authStatus;

    /** 相册 */
    private String imgList;

    /** 身份证号 */
    private String idCard;

    /** 创建时间 */
    @ExcelProperty(value = "创建时间", index = 8)
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;

}
