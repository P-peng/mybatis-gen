package com.ge.generate.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 生成扩展 java所用的对象
 * @author dengzhipeng
 * @date 2019/06/21
 */
@Getter
@Setter
public class TemplateJava {
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 包名
     */
    private String packageName;
    /**
     * 公共属性
     */
    private CommonPropertyBo commonProperty;
    /**
     * 父类的名字
     */
    private String fatherName;
    /**
     * 父类所在的包名
     */
    private String fatherPackage;

    public TemplateJava(){}
}
