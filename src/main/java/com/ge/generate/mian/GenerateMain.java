package com.ge.generate.mian;

import com.ge.generate.entity.ColumnBo;
import com.ge.generate.entity.CommonPropertyBo;
import com.ge.generate.entity.TemplateBaseJava;
import com.ge.generate.utils.MysqlUtil;
import com.ge.generate.utils.ReadPropertiesUtil;
import com.ge.generate.utils.StringUtil;
import com.ge.mybatis.entity.TableSchemaPo;
import com.ge.mybatis.mapper.TableSchemaMapper;
import com.ge.mybatis.utils.MapperUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author dengzhipeng
 * @date 2019/06/21
 */
public class GenerateMain {
    private static String templateBaseJavaPath = "src/template/TemplateBaseJava.java";

    private static String generatePath = "com.ge.generate.test";

    public static void main(String[] args){
        GenerateMain main = new GenerateMain();
        try {
            main.go();
        } catch (Exception e) {
            System.out.println("生成java文件失败");
            e.printStackTrace();
        }
    }


    public void go() throws IOException {
        // 读取配置文件信息
        String genPath = "./src/main/resources/gen.properties";
        ReadPropertiesUtil.intiReadProperties(genPath);
        String author = ReadPropertiesUtil.readByName("author");
        String version = ReadPropertiesUtil.readByName("version");
        CommonPropertyBo commonProperty = new CommonPropertyBo();
        commonProperty.setAuthor(author);
        commonProperty.setVersion(version);
        commonProperty.setDate(new SimpleDateFormat("yyyy/MM/dd").format(new Date()));

        // 解析mysql表字段，生成 base java实体文件
        TemplateBaseJava templateBaseJava = new TemplateBaseJava();
        templateBaseJava.setCommonProperty(commonProperty);
        analyzeTableColumn(templateBaseJava);
        generateJava(templateBaseJava);

        // 生成mapper.xml文件
    }

    /**
     * 生成java文件
     * @throws IOException
     */
    public void generateJava(TemplateBaseJava templateBaseJava) throws IOException {
        Version version = new Version("2.3.0");
        Configuration config = new Configuration(version);
        Template template = config.getTemplate(templateBaseJavaPath);

    }

    /**
     * 解析表字段
     */
    public void analyzeTableColumn(TemplateBaseJava templateBaseJava){
        TableSchemaMapper mapper = (TableSchemaMapper) MapperUtil.getMapper(TableSchemaMapper.class);
        List<TableSchemaPo> list =  mapper.select("ge");
        List<ColumnBo> columnBoList = new ArrayList();
        List<String> importJavapackage = new ArrayList<String>();
        for (TableSchemaPo po : list) {
            ColumnBo bo = new ColumnBo();
            // 下滑线_命名法转为驼峰命名
            bo.setJavaName(StringUtil.underlineToHump(po.getColumnName()));
            // 分析对应的java类型
            bo.setJavaType(MysqlUtil.analyzeColumnName(po.getDataType()));
            // 分析对应所在的java包
            bo.setJavaPackage(MysqlUtil.analyzeColumnJavaPackage(po.getDataType()));
            // sql字段加前缀a_
            bo.setJdbcName("a_" + po.getColumnName());
            // 注释
            bo.setComment(po.getColumnComment());
            // 分析是否属于费java.lang的包
            if (bo.getJavaPackage().indexOf("java.lang") < 0){
                importJavapackage.add(bo.getJavaPackage());
            }
            columnBoList.add(bo);
        }
        templateBaseJava.setImportJavapackage(importJavapackage);
        templateBaseJava.setColumnBos(columnBoList);

    }



}
