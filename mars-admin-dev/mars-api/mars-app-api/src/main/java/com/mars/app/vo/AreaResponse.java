package com.mars.app.vo;

import jdk.jfr.Name;
import lombok.Data;

@Data
public class AreaResponse {

    /**
     * 区域代码
     */
    @Name(value = "区域代码")
    private Integer code;

    /**
     * 区域名称
     */
    @Name(value = "区域名称")
    private String name;

    /**
     * 父级代码
     */
    @Name(value = "父级代码")
    private Integer pcode;

}
