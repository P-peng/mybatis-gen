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
        // 此处可不使用mybatis框架，换成原生jdbc
        analyzeTableColumn(templateBaseJava, commonProperty.getTableName());
        generateBaseJava(templateBaseJava);

        // 生成扩展 java文件
        TemplateJava templateJava = new TemplateJava();
        // 父类包名和名字
        templateJava.setFatherName(templateBaseJava.getFileName());
        templateJava.setFatherPackage(templateBaseJava.getPackageName() + "." + templateBaseJava.getFileName());
        templateJava.setCommonProperty(commonProperty);
        generateJava(templateJava);

        // 生成基础 base mapper.java 和 base mapper.xml文件
        TemplateBaseMapper templateBaseMapper = new TemplateBaseMapper();
        // 读取BaseCrudMapper接口所在的包地址
        templateBaseMapper.setFatherPackage(ReadPropertiesUtil.readByName("baseCrudMapperPath"));
        templateBaseMapper.setCommonProperty(commonProperty);
        generateBaseMapperAndBaseMapperXml(templateBaseMapper, null);
    }


    /**
     * 生成生成基础 base mapper.java 和 base mapper.xml文件
     * @param templateBaseMapper
     * @param templateBaseMapperBo
     */
    public void generateBaseMapperAndBaseMapperXml(TemplateBaseMapper templateBaseMapper, TemplateBaseMapperBo templateBaseMapperBo) throws IOException, TemplateException {
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
        // 生成base mapper文件
        template.process(templateBaseMapper, new FileWriter(file));
        /**** 生成base mapper java文件 end  ****/

    }

    /**
     * 生成扩展mapper文件
     */
    public void generateMapper(){

    }

    /**
     * 生成基础mapper interface文件
     */
    public void generateBaseMapperInterface(){

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
        templateJava.setPackageName(assemblyPackageName(templateJava.getCommonProperty()) + ".dto" );
        // 要生成java文件所在的全相对地址
        String fileFullName = templateJava.getCommonProperty().getModulePath() + "/java/"
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
        templateBaseJava.setPackageName(assemblyPackageName(templateBaseJava.getCommonProperty()) + ".dto.base" );
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
        // 生成文件
        template.process(templateBaseJava, new FileWriter(file));
    }

    /**
     * 解析表字段
     * @param templateBaseJava
     * @param tableName
     * @throws Exception
     */
    public void analyzeTableColumn(TemplateBaseJava templateBaseJava ,String tableName) throws Exception{
        /**** 此处可换成原生jdbc start ****/
        TableSchemaMapper mapper = (TableSchemaMapper) MapperUtil.getMapper(TableSchemaMapper.class);
        List<TableSchemaPo> list =  mapper.select(tableName);
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

    /**
     * 拼装前置包和模块名字
     * @param commonPropertyBo
     * @return
     */
    private String assemblyPackageName(CommonPropertyBo commonPropertyBo){
        return commonPropertyBo.getPackagePath() + "." + commonPropertyBo.getModuleName();
    }


}
