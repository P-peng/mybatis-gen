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
     * 作者
     */
    private String author;
    /**
     * 日期
     */
    private String date;
    /**
     * 版本号
     */
    private String version;
    /**
     * 需要导入的java包
     */
    private List<String> importJavapackage;
    /**
     * 存储的字段信息
     */
    private List<ColumnBo> columnBos;

    public TemplateBaseJava(){
        // 读取配置属性
    }
}
