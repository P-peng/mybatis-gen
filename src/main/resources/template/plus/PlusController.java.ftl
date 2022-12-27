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
@Api(tags = "NEW-溯源管理")
public class ${className} {

    @Autowired
    private IFrontTraceService iFrontTraceService;

    @ApiOperation(value = "分页")
    @GetMapping("/selectPage")
    public ${vo}<${pageVo}<${pageOutName}>> selectPage(${pageInName} in) {
        return iFrontTraceService.selectPage(in);
    }

    @ApiOperation(value = "插入")
    @PostMapping("/insertById")
    public ${vo}<Void> insertById(${inName} in) {
        return iFrontTraceService.insertById(in);
    }

    @ApiOperation(value = "修改")
    @PostMapping("/updateById")
    public ${vo}<Void> updateById(${inName} in) {
        return iFrontTraceService.updateById(in);
    }

    @ApiOperation(value = "删除")
    @PostMapping("/deleteById")
    public ${vo}<Void> deleteById(${inName} in) {
        return iFrontTraceService.deleteById(in);
    }
}
