package com.ge.generate.mian;

import com.ge.generate.entity.*;
import com.ge.generate.utils.MysqlUtil;
import com.ge.generate.utils.ReadPropertiesUtil;
import com.ge.generate.utils.StringUtil;
import com.ge.mybatis.entity.TableSchemaPo;
import com.ge.mybatis.mapper.TableSchemaMapper;
import com.ge.mybatis.utils.MapperUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
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
    private static String templateJavaPath = "./src/main/resources/template/TemplateJava.java";
    private static String templateBaseMapperPath = "./src/main/resources/template/TemplateBaseMapper.java";
    private static String templateBaseMapperXmlPath = "./src/main/resources/template/TemplateBaseMapperXml.xml";
    private static String templateMapperPath = "./src/main/resources/template/TemplateMapper.java";
    private static String templateMapperXmlPath = "./src/main/resources/template/TemplateMapperXml.xml";

    public static void main(String[] args){
        GenerateMain main = new GenerateMain();
        try {
            main.go();
        } catch (Exception e) {
            System.out.println("生成java文件失败");
            e.printStackTrace();
        }
        System.out.println("is ok");
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
        commonProperty.setDtoModulePath(ReadPropertiesUtil.readByName("dtoModulePath"));
        commonProperty.setDtoPackagePath(ReadPropertiesUtil.readByName("dtoPackagePath"));
        commonProperty.setTableName(ReadPropertiesUtil.readByName("tableName"));
        commonProperty.setModuleName(ReadPropertiesUtil.readByName("moduleName"));
        commonProperty.setDatabaseName(ReadPropertiesUtil.readByName("databaseName"));
        commonProperty.setDate(new SimpleDateFormat("yyyy/MM/dd").format(new Date()));

        /** 解析mysql表字段，生成 base java实体文件 start */
        TemplateBaseJava templateBaseJava = new TemplateBaseJava();
        templateBaseJava.setCommonProperty(commonProperty);
        // 此处可不使用mybatis框架，换成原生jdbc
        analyzeTableColumn(templateBaseJava, commonProperty.getTableName(), commonProperty.getDatabaseName());
        generateBaseJava(templateBaseJava);
        /** 解析mysql表字段，生成 base java实体文件 end   */


        /** 生成扩展 java文件 start */
        TemplateJava templateJava = new TemplateJava();
        // 父类包名和名字
        templateJava.setFatherName(templateBaseJava.getFileName());
        templateJava.setFatherPackage(templateBaseJava.getPackageName() + "." + templateBaseJava.getFileName());
        templateJava.setCommonProperty(commonProperty);
        generateJava(templateJava);
        /** 生成扩展 java文件 end */

        /** 生成基础 base mapper.java 和 base mapper.xml文件 start */
        TemplateBaseMapper templateBaseMapper = new TemplateBaseMapper();
        // 读取BaseCrudMapper接口所在的包地址
        templateBaseMapper.setFatherPackage(ReadPropertiesUtil.readByName("baseCrudMapperPath"));
        templateBaseMapper.setCommonProperty(commonProperty);

        TemplateBaseMapperXml templateBaseMapperXml = new TemplateBaseMapperXml();
        templateBaseMapperXml.setCommonPropertyBo(commonProperty);
        templateBaseMapperXml.setColumnBos(templateBaseJava.getColumnBos());
        generateBaseMapperAndBaseMapperXml(templateBaseMapper, templateBaseMapperXml);
        /** 生成基础 base mapper.java 和 base mapper.xml文件 end   */

        /** 生成扩展 mapper.java 和 mapper.xml文件 */
        TemplateMapper templateMapper = new TemplateMapper();
        templateMapper.setCommonProperty(commonProperty);
        templateMapper.setFatherName(templateBaseMapper.getFileName());
        templateMapper.setFatherPackage(templateBaseMapper.getPackageName()+ "." + templateBaseMapper.getFileName());

        TemplateMapperXml templateMapperXml = new TemplateMapperXml();
        templateMapperXml.setType(templateJava.getPackageName() + "." + templateJava.getFileName());
        generateMapper(templateMapper, templateMapperXml);

    }

    /**
     * 生成扩展mapper文件
     */
    public void generateMapper(TemplateMapper templateMapper, TemplateMapperXml templateMapperXml) throws Exception {
        /** 生成 mapper interface java文件 start */
        // java文件名
        String fileName = StringUtil.underlineToHump(StringUtil.toUpperCaseFirstOne(templateMapper.getCommonProperty().getTableName()));
        templateMapper.setFileName(fileName + "Mapper");
        templateMapper.setPackageName(assemblyPackageName(templateMapper.getCommonProperty()) + ".mapper");
        // 要生成java文件所在的全相对地址
        String fileFullName = templateMapper.getCommonProperty().getModulePath() + "/java/"
                + templateMapper.getPackageName().replace(".", "/") + "/" + templateMapper.getFileName() + ".java";
        Version version = new Version("2.3.0");
        Configuration config = new Configuration(version);
        Template template = config.getTemplate(templateMapperPath);
        String targetFile = MessageFormat.format(fileFullName, fileFullName);
        File file = new File(targetFile);
        File parentFile = file.getParentFile();
        // 创建文件目录
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        // 生成base mapper java文件
        template.process(templateMapper, new FileWriter(file));
        /** 生成 mapper interface java文件 end */

        /** 生成 mapper xml文件 */
        String xmlFileName = StringUtil.underlineToHump(StringUtil.toUpperCaseFirstOne(templateMapper.getCommonProperty().getTableName()));
        templateMapperXml.setFileName(xmlFileName + "Mapper");
        templateMapperXml.setNamespace(templateMapper.getPackageName() + "." + templateMapper.getFileName());

        // 要生成xml文件所在的全相对地址
//        String xmlFileFullName = templateBaseMapper.getCommonProperty().getModulePath() + "/resources/mappers/"
//                + templateBaseMapperXml.getCommonPropertyBo().getModuleName()+ "/" + templateBaseMapperXml.getFileName()
//                + ".xml";
//
//        Template xmlTemplate = config.getTemplate(templateBaseMapperXmlPath);
//        String xmlTargetFile = MessageFormat.format(xmlFileFullName, xmlFileFullName);
//        File xmlFile = new File(xmlTargetFile);
//        File xmlParentFile = xmlFile.getParentFile();
//        // 创建文件目录
//        if (!xmlParentFile.exists()) {
//            xmlParentFile.mkdirs();
//        }
//        // 生成base mapper xml文件
//        xmlTemplate.process(templateBaseMapperXml, new FileWriter(xmlFile));

    }


    /**
     * 生成扩展 java 文件
     * @param templateJava
     * @throws IOException
     * @throws TemplateException
     */
    public void generateJava(TemplateJava templateJava) throws IOException, TemplateException {
        // java文件名
        String fileName = StringUtil.underlineToHump(StringUtil.toUpperCaseFirstOne(templateJava.getCommonProperty().getTableName()));
        templateJava.setFileName(fileName + "Dto");
        // 包名
        templateJava.setPackageName(assemblyDtoPackageName(templateJava.getCommonProperty()) + ".dto" );
        // 要生成java文件所在的全相对地址
        String fileFullName = templateJava.getCommonProperty().getDtoModulePath() + "/java/"
                + templateJava.getPackageName().replace(".", "/") + "/" + templateJava.getFileName() + ".java";
        Version version = new Version("2.3.0");
        Configuration config = new Configuration(version);
        Template template = config.getTemplate(templateJavaPath);
        String targetFile = MessageFormat.format(fileFullName, fileFullName);
        File file = new File(targetFile);
        File parentFile = file.getParentFile();
        // 创建文件目录
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        // 生成文件
        template.process(templateJava, new FileWriter(file));
    }

    /**
     * 生成base java文件
     * @param templateBaseJava
     * @throws IOException
     */
    public void generateBaseJava(TemplateBaseJava templateBaseJava) throws Exception {
        // java文件名
        String fileName = StringUtil.underlineToHump(StringUtil.toUpperCaseFirstOne(templateBaseJava.getCommonProperty().getTableName()));
        templateBaseJava.setFileName("Base" + fileName + "Dto");
        // 包名
        templateBaseJava.setPackageName(assemblyDtoPackageName(templateBaseJava.getCommonProperty()) + ".dto.base" );
        // 要生成java文件所在的全相对地址
        String fileFullName = templateBaseJava.getCommonProperty().getDtoModulePath() + "/java/"
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
        // 生成文件
        template.process(templateBaseJava, new FileWriter(file));
    }

    /**
     * 解析表字段
     * @param templateBaseJava
     * @param tableName
     * @throws Exception
     */
    public void analyzeTableColumn(TemplateBaseJava templateBaseJava ,String tableName, String databaseName) throws Exception{
        /**** 此处可换成原生jdbc start ****/
        TableSchemaMapper mapper = (TableSchemaMapper) MapperUtil.getMapper(TableSchemaMapper.class);
        List<TableSchemaPo> list =  mapper.select(tableName, databaseName);
        /**** 此处可换成原生jdbc end ****/
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
            // 分析mybatis字段类型
            bo.setJdbcType(MysqlUtil.analyzeColumnJdbcType(po.getDataType()));
            bo.setJdbcName(po.getColumnName());
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

    /**
     * 拼装前置包和模块名字
     * @param commonPropertyBo
     * @return
     */
    private String assemblyPackageName(CommonPropertyBo commonPropertyBo){
        return commonPropertyBo.getPackagePath() + "." + commonPropertyBo.getModuleName();
    }

    /**
     * 拼装dto前置包和dto模块名字
     * @param commonPropertyBo
     * @return
     */
    private String assemblyDtoPackageName(CommonPropertyBo commonPropertyBo){
        return commonPropertyBo.getDtoPackagePath() + "." + commonPropertyBo.getModuleName();
    }

    /**
     * 生成生成基础 base mapper.java 和 base mapper.xml文件
     * @param templateBaseMapper
     * @param templateBaseMapperXml
     */
    public void generateBaseMapperAndBaseMapperXml(TemplateBaseMapper templateBaseMapper, TemplateBaseMapperXml templateBaseMapperXml) throws IOException, TemplateException {
        /**** 生成base mapper java文件 start ****/
        String fileName = StringUtil.underlineToHump(StringUtil.toUpperCaseFirstOne(templateBaseMapper.getCommonProperty().getTableName()));
        templateBaseMapper.setFileName("Base" + fileName + "Mapper");
        templateBaseMapper.setPackageName(assemblyPackageName(templateBaseMapper.getCommonProperty()) + ".mapper.base");
        // 要生成java文件所在的全相对地址
        String fileFullName = templateBaseMapper.getCommonProperty().getModulePath() + "/java/"
                + templateBaseMapper.getPackageName().replace(".", "/") + "/" + templateBaseMapper.getFileName() + ".java";
        Version version = new Version("2.3.0");
        Configuration config = new Configuration(version);
        Template template = config.getTemplate(templateBaseMapperPath);
        String targetFile = MessageFormat.format(fileFullName, fileFullName);
        File file = new File(targetFile);
        File parentFile = file.getParentFile();
        // 创建文件目录
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        // 生成base mapper java文件
        template.process(templateBaseMapper, new FileWriter(file));
        /**** 生成base mapper java文件 end  ****/

        /**** 生成base mapper.xml文件 start ****/
        templateBaseMapperXml.setFileName(templateBaseMapper.getFileName());
        templateBaseMapperXml.setNamespace(templateBaseMapper.getPackageName() + "." + templateBaseMapperXml.getFileName());
        templateBaseMapperXml.setType(templateBaseMapper.getCommonProperty().getDtoPackagePath() + "."
                + templateBaseMapper.getCommonProperty().getTableName() + ".dto.base.Base"
                + StringUtil.underlineToHumpAndFirstToUpper(templateBaseMapper.getCommonProperty().getTableName()) + "Dto");

        StringBuffer sqlColumn = new StringBuffer();
        for (ColumnBo columnBo : templateBaseMapperXml.getColumnBos()) {
            sqlColumn.append("a." + columnBo.getJdbcName() + " AS a_" + columnBo.getJdbcName() + ",");
        }
        // 去除最后一个字符
        templateBaseMapperXml.setSqlColumn(sqlColumn.toString().substring(0, sqlColumn.length() - 1));

        // 要生成xml文件所在的全相对地址
        String xmlFileFullName = templateBaseMapper.getCommonProperty().getModulePath() + "/resources/mappers/"
                + templateBaseMapperXml.getCommonPropertyBo().getModuleName()+ "/" + templateBaseMapperXml.getFileName()
                + ".xml";

        Template xmlTemplate = config.getTemplate(templateBaseMapperXmlPath);
        String xmlTargetFile = MessageFormat.format(xmlFileFullName, xmlFileFullName);
        File xmlFile = new File(xmlTargetFile);
        File xmlParentFile = xmlFile.getParentFile();
        // 创建文件目录
        if (!xmlParentFile.exists()) {
            xmlParentFile.mkdirs();
        }
        // 生成base mapper xml文件
        xmlTemplate.process(templateBaseMapperXml, new FileWriter(xmlFile));
        /**** 生成base mapper.xml文件 end   ****/

    }
}
