package com.ge.puls.entity;

import com.ge.generate.entity.ColumnBo;
import com.ge.generate.entity.CommonPropertyBo;
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
public class JavaTpl {
    /**
     * 文件名
     */
    private String className;

    /**
     * 包名
     */
    private String packageName;

    /**
     * 需要导入的java包
     */
    private List<String> importJavaPackage;

    /**
     * 存储的字段信息
     */
    private List<Column> columnBos;

    /**
     * 公共属性
     */
    private PlusProperty commonProperty;

}
