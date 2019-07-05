package com.ge.generate.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dengzhipeng
 * @date 2019/07/05
 */
@Getter
@Setter
public class TemplateBaseMapper {
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
     * 父接口的java包
     */
    private String fatherPackage;

}
