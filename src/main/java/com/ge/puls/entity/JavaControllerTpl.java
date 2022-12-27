package com.ge.puls.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 生成java out 出餐所用的对象
 * @author dengzhipeng
 * @date 2019/06/21
 */
@Getter
@Setter
public class JavaControllerTpl {
    /**
     * 公共属性
     */
    private PlusProperty commonProperty;

    /**
     * 包名
     */
    private String packageName;

    /**
     * 需要导入的java包
     */
    private List<String> importJavaPackage;

    /**
     * 表注释
     */
    private String tableComment;

    /**
     * 文件名
     */
    private String className;

    /**
     * 父类
     */
    private String fatherName;

    /**
     * 泛型
     */
    private String entityName;

    /**
     * 实体vo
     */
    private String vo;

    /**
     * 实体分页
     */
    private String pageVo;

    /**
     * 分页出参
     */
    private String pageOutName;

    /**
     * 分页入参
     */
    private String pageInName;

    /**
     * 入参
     */
    private String inName;

    /**
     * serviceName
     */
    private String serviceName;

    /**
     * serviceName 首字母小写
     */
    private String serviceNameLower;




}
