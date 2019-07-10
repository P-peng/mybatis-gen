package com.ge.generate.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author dengzhipeng
 * @date 2019/06/21
 */
@Getter
@Setter
public class TemplateBaseMapperXml {
    private String fileName;

    private String namespace;

    /**
     * java实体类所在的地址
     */
    private String type;

    private CommonPropertyBo commonPropertyBo;

    /**
     * 存储的字段信息
     */
    private List<ColumnBo> columnBos;

    private String sqlColumn;

}
