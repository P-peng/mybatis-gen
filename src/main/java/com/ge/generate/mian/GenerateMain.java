package com.ge.generate.mian;

import com.ge.generate.entity.ColumnBo;
import com.ge.mybatis.entity.TableSchemaPo;
import com.ge.mybatis.mapper.TableSchemaMapper;
import com.ge.mybatis.utils.MapperUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dengzhipeng
 * @date 2019/06/21
 */
public class GenerateMain {
    private static String templateBaseJava = "src/template/TemplateBaseJava.java";

    private static String generatePath = "com.ge.generate.test";

    public static void main(String[] args){
        GenerateMain main = new GenerateMain();
        try {
            main.generateJava();
        } catch (IOException e) {
            System.out.println("生成java文件失败");
            e.printStackTrace();
        }
    }

    public void generateJava() throws IOException {
        Version version = new Version("2.3.0");
        Configuration config = new Configuration(version);
        Template template = config.getTemplate(templateBaseJava);
        TableSchemaMapper mapper = (TableSchemaMapper) MapperUtil.getMapper(TableSchemaMapper.class);
        List<TableSchemaPo> list =  mapper.select();
        List<ColumnBo> columnBoList = new ArrayList();
        for (TableSchemaPo po : list) {
            ColumnBo bo = new ColumnBo();
            bo.setComment(po.getColumnComment());
            bo.setJavaName(po.getColumnName());
            // sql字段加前缀a_
            bo.setJdbcName("a_" + po.getColumnName());
        }
    }
}
