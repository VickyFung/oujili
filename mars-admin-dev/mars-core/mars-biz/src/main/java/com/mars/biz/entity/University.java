package com.mars.biz.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * 大学
 * 
 * @author Mars
 * @date 2026-04-15
 */
@Data
@TableName("university")
public class University implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键 */
    @ExcelProperty(value = "主键", index = 0)
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 创建者 */
    @ExcelProperty(value = "创建者", index = 1)
    private Integer creator;

    /** 创建时间 */
    @ExcelProperty(value = "创建时间", index = 2)
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 修改者 */
    @ExcelProperty(value = "修改者", index = 3)
    private Integer modifyer;

    /** 修改时间 */
    @ExcelProperty(value = "修改时间", index = 4)
    private LocalDateTime modifyTime;

    /** 删除标志（0代表存在 1代表删除） */
    private Integer delFlag;

    /** 乐观锁版本号 */
    @ExcelProperty(value = "乐观锁版本号", index = 5)
    private Integer version;

    /** 学校名称 */
    @ExcelProperty(value = "学校名称", index = 6)
    private String name;

}
