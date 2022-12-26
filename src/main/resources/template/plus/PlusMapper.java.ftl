package ${packageName};

<#list importJavaPackage as javaPackage>
import ${javaPackage};
</#list>

/**
 * @author ${commonProperty.author}
 * @date ${commonProperty.date}
 * @version ${commonProperty.version}
 */
public interface ${className} extends ${fatherName}<${entityName}>{
}
