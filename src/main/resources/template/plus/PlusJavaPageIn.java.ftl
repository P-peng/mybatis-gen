package ${packageName};

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
<#list importJavaPackage as javapackage>
import ${javapackage};
</#list>

/**
 * @author ${commonProperty.author}
 * @date ${commonProperty.date}
 * @version ${commonProperty.version}
 */
@Getter
@Setter
public class ${className} extends ${fatherName} implements Serializable {

    private static final long serialVersionUID = 19970718L;
    <#list columnBos as columnBo>

    @ApiModelProperty(value = "${columnBo.comment}")
    <#list columnBo.rs as r>
    ${r}
    </#list>
    private ${columnBo.javaType} ${columnBo.javaName};
    </#list>
}
