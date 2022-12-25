package com.ge.puls.entity;

import lombok.Data;

import java.util.List;

@Data
public class MapperTpl {

    /**
     * 包名
     */
    private String packageName;

    /**
     * 引入包
     */
    private List<String> importJavaPackage;

    /**
     * 类名
     */
    private String className;

    /**
     * 继承父类类名
     */
    private String fatherName;

    /**
     * 实体类泛形
     */
    private String entityName;

    /**
     * 公共配置
     */
    private PlusProperty commonProperty;
}
