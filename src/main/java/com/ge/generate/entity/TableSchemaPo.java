package com.ge.generate.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author dengzhipeng
 * @date 2019/06/21
 */
@Getter
@Setter
@ToString
public class TableSchemaPo {
    /**
     * 字段名字
     */
    private String columnName;
    /**
     * 字段类型
     */
    private String dataType;
    /**
     * 字段注释
     */
    private String columnComment;

    /**
     * 字段关键字
     */
    private boolean columnKey = false;
}
