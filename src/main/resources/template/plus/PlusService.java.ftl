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

    /**
    * 分页查询
    *
    * @param in
    * @return
    */
    ${vo}<${pageVo}<${pageOutName}>> page(${pageInName} in);

    /**
    * 插入
    *
    * @param in
    * @return
    */
    ${vo}<Void> add(${inName} in);

    /**
    * 修改
    *
    * @param in
    * @return
    */
    ${vo}<Void> edit(${inName} in);

    /**
    * 删除
    *
    * @param in
    * @return
    */
    ${vo}<Void> del(${inName} in);
}
