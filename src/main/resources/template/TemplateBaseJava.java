package ${packageName};

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
<#list importJavapackage as javapackage>
import ${javapackage};
</#list>
/**
 * @author ${commonProperty.author}
 * @date ${commonProperty.date}
 * @version ${commonProperty.version}
 */
@Getter
@Setter
public class ${fileName} implements Serializable {
    private static final long serialVersionUID = 19970718L;
    <#list columnBos as columnBo>

    /** ${columnBo.comment} */
    private ${columnBo.javaType} ${columnBo.javaName};
    </#list>
}
