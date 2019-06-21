package com.ge.generate.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author dengzhipeng
 * @date 2019/06/21
 */
@Getter
@Setter
public class TemplateBaseJava{
    private String packageName;

    private String author;

    private String data;

    private String version;

    private List<String> importJavapackage;

    private List<ColumnBo> columnBos;

    public TemplateBaseJava(){
        // 读取配置属性
    }
}
