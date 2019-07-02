package com.ge.generate.mian;

import com.ge.generate.constant.GenerateConstant;
import com.ge.generate.entity.ColumnBo;
import com.ge.generate.entity.CommonPropertyBo;
import com.ge.generate.entity.TemplateBaseJava;
import com.ge.generate.entity.TemplateBaseMapperBo;
import com.ge.generate.utils.MysqlUtil;
import com.ge.generate.utils.ReadPropertiesUtil;
import com.ge.generate.utils.StringUtil;
import com.ge.mybatis.entity.TableSchemaPo;
import com.ge.mybatis.mapper.TableSchemaMapper;
import com.ge.mybatis.utils.MapperUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author dengzhipeng
 * @date 2019/06/21
 */
public class GenerateMain {
    private static String templateBaseJavaPath = "./src/main/resources/template/TemplateBaseJava.java";

    private static String newJavaPath = "src/newjava.java";

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


    public void go() throws Exception {
        // 读取配置文件信息
        String genPath = "./src/main/resources/gen.properties";
        ReadPropertiesUtil.intiReadProperties(genPath);
        CommonPropertyBo commonProperty = new CommonPropertyBo();
        commonProperty.setAuthor(ReadPropertiesUtil.readByName("author"));
        commonProperty.setVersion(ReadPropertiesUtil.readByName("version"));
        commonProperty.setModulePath(ReadPropertiesUtil.readByName("modulePath"));
        commonProperty.setPackagePath(ReadPropertiesUtil.readByName("packagePath"));
        commonProperty.setTableName(ReadPropertiesUtil.readByName("tableName"));
        commonProperty.setModuleName(ReadPropertiesUtil.readByName("moduleName"));
        commonProperty.setDate(new SimpleDateFormat("yyyy/MM/dd").format(new Date()));

        // 解析mysql表字段，生成 base java实体文件
        TemplateBaseJava templateBaseJava = new TemplateBaseJava();
        templateBaseJava.setCommonProperty(commonProperty);
        analyzeTableColumn(templateBaseJava, commonProperty.getTableName());
        generateBaseJava(templateBaseJava);

        // 生成mapper.xml文件
    }

    /**
     * 生成基础mapper文件
     */
    public void generateBaseMapper(TemplateBaseMapperBo templateBaseMapperBo){

    }

    /**
     * 生成基础mapper interface文件
     */
    public void generateBaseMapperInterface(){

    }

    /**
     * 生成java文件
     * @param templateBaseJava
     * @throws IOException
     */
    public void generateBaseJava(TemplateBaseJava templateBaseJava) throws Exception {
        // java文件名
        String fileName = StringUtil.underlineToHump(StringUtil.toUpperCaseFirstOne(templateBaseJava.getCommonProperty().getTableName()));
        templateBaseJava.setFileName(fileName + "Dto");
        // 包名
        templateBaseJava.setPackageName(templateBaseJava.getCommonProperty().getPackagePath() + "."
        + templateBaseJava.getCommonProperty().getModuleName() + "." + GenerateConstant.ENTITY_PACKAGE_NAME);
        // 要生成java文件所在的全相对地址
        String fileFullName = templateBaseJava.getCommonProperty().getModulePath() + "/java/"
                + templateBaseJava.getPackageName().replace(".", "/") + "/" + templateBaseJava.getFileName() + ".java";

        Version version = new Version("2.3.0");
        Configuration config = new Configuration(version);
        Template template = config.getTemplate(templateBaseJavaPath);
        String targetFile = MessageFormat.format(fileFullName, fileFullName);
        File file = new File(targetFile);
        File parentFile = file.getParentFile();
        // 创建文件目录
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        template.process(templateBaseJava, new FileWriter(file));
    }

    /**
     * 解析表字段
     * @param templateBaseJava
     */
    public void analyzeTableColumn(TemplateBaseJava templateBaseJava ,String tableName) throws Exception{
        TableSchemaMapper mapper = (TableSchemaMapper) MapperUtil.getMapper(TableSchemaMapper.class);
        List<TableSchemaPo> list =  mapper.select(tableName);
        if (list.size() == 0){
            throw new RuntimeException(tableName + "表不存在，或" + tableName + "表字段无字段");
        }
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
