package com.ge.puls.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *
 * @author dengzhipeng
 * @date 2019/06/21
 */
@Getter
@Setter
public class Column {
    /**
     * java名称
     */
    private String javaName;

    /**
     * java类型
     */
    private String javaType;

    /**
     * java所在包
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

    /**
     * jdbc字段类型
     */
    private String jdbcType;

    /**
     * 注解集合
     */
    private List<String> rs;

    /**
     * 字段 get Lambda
     */
    private String getLambda;

}
