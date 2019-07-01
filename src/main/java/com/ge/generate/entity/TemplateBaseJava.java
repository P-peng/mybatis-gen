package com.ge.generate.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 生成base java所用的对象
 * @author dengzhipeng
 * @date 2019/06/21
 */
@Getter
@Setter
public class TemplateBaseJava{
    /**
     * 包名
     */
    private String packageName;
    /**
     * 需要导入的java包
     */
    private List<String> importJavapackage;
    /**
     * 存储的字段信息
     */
    private List<ColumnBo> columnBos;
    /**
     * 公共属性
     */
    private CommonPropertyBo commonProperty;

    public TemplateBaseJava(){
        // 读取配置属性
    }
}
