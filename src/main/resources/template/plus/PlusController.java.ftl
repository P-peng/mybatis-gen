package ${packageName};

<#list importJavaPackage as javaPackage>
import ${javaPackage};
</#list>
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ${tableComment}
 *
 * @author ${commonProperty.author}
 * @date ${commonProperty.date}
 * @version ${commonProperty.version}
 */
@RestController
@RequestMapping("/${commonProperty.moduleName}/${commonProperty.humpTableName}")
@Api(tags = "${tableComment}")
public class ${className} {

    @Autowired
    private ${serviceName} ${serviceNameLower};

    @ApiOperation(value = "分页")
    @GetMapping("/page")
    public ${vo}<${pageVo}<${pageOutName}>> page(${pageInName} in) {
        return ${serviceNameLower}.page(in);
    }

    @ApiOperation(value = "新增")
    @PostMapping("/add")
    public ${vo}<Void> add(${inName} in) {
        return ${serviceNameLower}.add(in);
    }

    @ApiOperation(value = "修改")
    @PostMapping("/edit")
    public ${vo}<Void> edit(${inName} in) {
        return ${serviceNameLower}.edit(in);
    }

    @ApiOperation(value = "删除")
    @PostMapping("/del")
    public ${vo}<Void> del(${inName} in) {
        return ${serviceNameLower}.del(in);
    }
}
