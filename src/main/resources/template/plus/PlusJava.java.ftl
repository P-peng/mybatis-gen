package ${packageName};

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
public class ${className} implements Serializable {

    private static final long serialVersionUID = 1L;
    <#list columnBos as columnBo>

    /** ${columnBo.comment} */
    <#list columnBo.rs as r>
    ${r}
    </#list>
    private ${columnBo.javaType} ${columnBo.javaName};
    </#list>
}
