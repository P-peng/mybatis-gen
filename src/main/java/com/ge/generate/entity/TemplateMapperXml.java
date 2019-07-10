package com.ge.generate.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dengzhipeng
 * @date 2019/06/21
 */
@Getter
@Setter
public class TemplateMapperXml {
    private String fileName;

    private String namespace;

    /** java实体类所在的地址 */
    private String type;

    /** resultMap 继承类全地址 */
    private String resultMapExtend;

    private CommonPropertyBo commonPropertyBo;
}
