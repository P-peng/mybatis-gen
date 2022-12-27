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
    @GetMapping("/selectPage")
    public ${vo}<${pageVo}<${pageOutName}>> selectPage(${pageInName} in) {
        return ${serviceNameLower}.selectPage(in);
    }

    @ApiOperation(value = "插入")
    @PostMapping("/insertById")
    public ${vo}<Void> insertById(${inName} in) {
        return ${serviceNameLower}.insertById(in);
    }

    @ApiOperation(value = "修改")
    @PostMapping("/updateById")
    public ${vo}<Void> updateById(${inName} in) {
        return ${serviceNameLower}.updateById(in);
    }

    @ApiOperation(value = "删除")
    @PostMapping("/deleteById")
    public ${vo}<Void> deleteById(${inName} in) {
        return ${serviceNameLower}.deleteById(in);
    }
}
