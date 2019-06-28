package com.ge.generate.entity;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author dengzhipeng
 * @date 2019/06/21
 */
@Getter
@Setter
public class ColumnBo {
    /**
     * java名称
     */
    private String javaName;
    /**
     * java类型
     */
    private String javaType;
    /**
     * java所在包,此字段为空，说明是 java.lang 下的包
     */
    private String javaPackage;
    /**
     * 注释
     */
    private String comment;
    /**
     * jdbc字段名
     */
    private String jdbcName;

}
