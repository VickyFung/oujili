package com.mars.biz.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;

/**
 * 行政区划表
 * 
 * @author Mars
 * @date 2026-04-15
 */
@Data
@TableName("area_code")
public class AreaCode implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 行政区划代码 */
    @ExcelProperty(value = "行政区划代码", index = 0)
    @TableId(type = IdType.AUTO)
    private Integer code;

    /** 名字 */
    @ExcelProperty(value = "名字", index = 1)
    private String name;

    /** country:国家、province:省份（直辖市会在province和city显示）、city:市（直辖市会在province和city显示）、district:区县 */
    @ExcelProperty(value = "country:国家、province:省份（直辖市会在province和city显示）、city:市（直辖市会在province和city显示）、district:区县", index = 2)
    private String level;

    /** 所属行政区划代码 */
    @ExcelProperty(value = "所属行政区划代码", index = 3)
    private Integer pcode;

    /** 所属行政区划名字 */
    @ExcelProperty(value = "所属行政区划名字", index = 4)
    private String pname;

    /** 行政区划完整名字 */
    @ExcelProperty(value = "行政区划完整名字", index = 5)
    private String fullname;

    /** 经度 */
    @ExcelProperty(value = "经度", index = 6)
    private Double longitude;

    /** 纬度 */
    @ExcelProperty(value = "纬度", index = 7)
    private Double latitude;

}
