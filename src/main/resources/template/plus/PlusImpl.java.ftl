package ${packageName};

<#list importJavaPackage as javaPackage>
import ${javaPackage};
</#list>
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;


/**
 * @author ${commonProperty.author}
 * @date ${commonProperty.date}
 * @version ${commonProperty.version}
 */
@Service
public class ${className} extends ${fatherName}<${mapperName}, ${entityName}> implements ${serviceName} {

    @Override
    public ${vo}<${pageVo}<${pageOutName}>> page(${pageInName} in) {
        var vo = new ${vo}<${pageVo}<${pageOutName}>>();
        var ew = this.ewPage(in);

        var page = this.page(new Page<>(in.getPage(), in.getRows()), ew);
        var pageOut = new PageOut<${pageOutName}>();
        pageOut.cpNoRows(page);

        var list = new ArrayList<${pageOutName}>();

        for (${entityName} e : page.getRecords()) {
            var out = new ${pageOutName}();
            BeanUtils.copyProperties(e, out);
            list.add(out);
        }
        pageOut.setRows(list);
        vo.setData(pageOut);
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ${vo}<Void> add(${inName} in) {
        var vo = new ${vo}<Void>();
        var e = new ${entityName}();
        BeanUtils.copyProperties(in, e);
        this.save(e);
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ${vo}<Void> edit(${inName} in) {
        var vo = new ${vo}<Void>();
        var e = new ${entityName}();
        BeanUtils.copyProperties(in, e);
        this.updateById(e);
        return vo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ${vo}<Void> del(${inName} in) {
        var vo = new ${vo}<Void>();
        this.removeById(in.${keyFunc});
        return vo;
    }

    private ${wrapperName}<${entityName}> ewPage(${pageInName} in) {
        var ew = new ${wrapperName}<${entityName}>();
        <#list columnBos as columnBo>
        ew.eq(${entityName}::${columnBo.getLambda}, in.${columnBo.getLambda}());
        </#list>
        return ew;
    }
}
