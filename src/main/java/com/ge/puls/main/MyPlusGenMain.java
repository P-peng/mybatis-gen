package com.ge.puls.main;

import com.ge.generate.db.DBUtil;
import com.ge.generate.entity.ColumnBo;
import com.ge.generate.entity.TableSchemaPo;
import com.ge.generate.utils.MysqlUtil;
import com.ge.generate.utils.StringUtil;
import com.ge.puls.entity.*;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyPlusGenMain {

    // 模板路径
    final static String FTL_PATH = "/template/plus/";
    // XML 模板
    final static String FTL_XML = "PlusMapperXml.xml.ftl";
    // 实体 模板
    final static String FTL_JAVA = "PlusJava.java.ftl";
    // 实体 OUT 模板
    final static String FTL_JAVA_OUT = "PlusJavaOut.java.ftl";
    // 实体 PAGE IN 模板
    final static String FTL_JAVA_PAGE_IN = "PlusJavaPageIn.java.ftl";
    // Mapper 模板
    final static String FTL_MAPPER = "PlusMapper.java.ftl";
    // Mapper 模板
    final static String FTL_SERVICE = "PlusService.java.ftl";
    // Mapper 模板
    final static String FTL_IMPL = "PlusImpl.java.ftl";
    // Mapper 模板
    final static String FTL_CONTROLLER = "PlusController.java.ftl";

    // 创建时间字段
    final static String CREATE_TIME = "create_time";
    // 修改时间时间字段
    final static String UPDATE_TIME = "update_time";
    // 删除标记字段
    final static String IS_DELETE = "is_delete";
    // 删除标记字段
    final static String DELETE_VAL = "1";
    // 删除标记字段
    final static String UN_DELETE = "0";

    // 公共配置
    private PlusProperty commonProperty;

    public void setCommonProperty(PlusProperty commonProperty) {
        this.commonProperty = commonProperty;
    }

    public static void main(String[] args) throws Exception {
//        // 单表
        var tableName = "front_car";
        var moduleName = "ge";
//
//        // 数据库信息
        var drive = "com.mysql.jdbc.Driver";
        var url = "jdbc:mysql://120.77.152.4:3306/insure_dev?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useUnicode=true&useSSL=false";
        var user = "root";
        var password = "nts1688=8d2c3f5a608d452c8850645fe39ae2ec";


        // 数据库信息
//        var drive = "com.mysql.jdbc.Driver";
//        var url = "jdbc:mysql://127.0.0.1:3306/ge?useSSL=false";
//        var user = "root";
//        var password = "";
//        var dbName = "";

        // 公共配置
        var author = "17 farmer";
        var version = "1.0";

        // 基础目录
        var modulePath = System.getProperty("user.dir") + "/src/main";
        // 包名
        var packagePath = "com.ge";
        // java目录
        var javaPath = modulePath + "/java";
        // xml目录
        var mapperPath = modulePath + "/resources/mappers";

        // In java继承的类
        // 分页继承类
        var inFatherClass = "TokenBaseIn";
        var inFatherClassPackage = "com.et.load.modules.entity.base.TokenBaseIn";
        var inFatherPageClass = "TokenPageBaseIn";
        var inFatherPageClassPackage = "com.et.load.modules.entity.base.TokenPageBaseIn";

        // service类配置
        // 实体vo
        var vo = "ResultVo";
        // 实体vo包
        var voPackage = "com.et.load.core.base.ResultVo";
        // 实体分页vo
        var pageVo = "PageOut";
        // 实体分页vo包
        var pageVoPackage = "com.et.load.modules.entity.base.PageOut";
        // service 继承类
        var serviceFatherName = "IService";
        // service 继承包
        var serviceFatherNamePackage = "com.baomidou.mybatisplus.extension.service.IService";
        // impl 继承类
        var implFatherName = "ServiceImpl";
        // impl 继承包
        var implFatherNamePackage = "com.baomidou.mybatisplus.extension.service.impl.ServiceImpl";
        // impl 包装类
        var wrapperName = "MyLambdaQueryWrapper";
        var wrapperNamePackage = "com.et.load.core.plus.mybatis.MyLambdaQueryWrapper";

        // 配置文件
        var commonProperty = new PlusProperty();
        commonProperty.setDrive(drive);
        commonProperty.setUrl(url);
        commonProperty.setUser(user);
        commonProperty.setPassword(password);
        commonProperty.setTableName(tableName);
        commonProperty.setAuthor(author);
        commonProperty.setVersion(version);
        commonProperty.setModulePath(modulePath);
        commonProperty.setPackagePath(packagePath);
        commonProperty.setModuleName(moduleName);
        commonProperty.setMapperPath(mapperPath);
        commonProperty.setJavaPath(javaPath);
        commonProperty.setDate(new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
        // 入参配置文件
        commonProperty.setInFatherClass(inFatherClass);
        commonProperty.setInFatherClassPackage(inFatherClassPackage);
        commonProperty.setInFatherPageClass(inFatherPageClass);
        commonProperty.setInFatherPageClassPackage(inFatherPageClassPackage);
        // service类配置
        commonProperty.setVo(vo);
        commonProperty.setVoPackage(voPackage);
        commonProperty.setPageVo(pageVo);
        commonProperty.setPageVoPackage(pageVoPackage);
        commonProperty.setServiceFatherName(serviceFatherName);
        commonProperty.setServiceFatherNamePackage(serviceFatherNamePackage);
        // impl文件
        commonProperty.setImplFatherName(implFatherName);
        commonProperty.setImplFatherNamePackage(implFatherNamePackage);
        commonProperty.setWrapperName(wrapperName);
        commonProperty.setWrapperNamePackage(wrapperNamePackage);

        var main = new MyPlusGenMain();
        main.setCommonProperty(commonProperty);
        main.run();

    }

    /**
     * 根据url获取库名
     * @param url
     * @return
     */
    public  String getDbName(String url) {
        Pattern p = Pattern.compile("jdbc:(?<db>\\w+):.*((//)|@)(?<host>.+):(?<port>\\d+)(/|(;DatabaseName=)|:)(?<dbName>\\w+)\\??.*");
        Matcher m = p.matcher(url);
        if(m.find()) {
            return m.group("dbName");
        }
        return null;
    }

    private void setProperty() {
        // 数据库表名
        commonProperty.setDbName(this.getDbName(commonProperty.getUrl()));

        // 全包名
        if (commonProperty.getModuleName() == null || commonProperty.getModuleName().isBlank()) {
            commonProperty.setFullPackagePath(commonProperty.getPackagePath());
        } else {
            commonProperty.setFullPackagePath(commonProperty.getPackagePath() + "." + commonProperty.getModuleName());
        }
        // 表名字转换
//        commonProperty.setHumpTableName(StringUtil.underlineToHumpAndFirstToUpper(commonProperty.getTableName()));
        commonProperty.setHumpTableName(StringUtil.underlineToHump(commonProperty.getTableName()));
        commonProperty.setUpperTableName(StringUtil.toUpperCaseFirstOne(commonProperty.getHumpTableName()));
    }

    public void run() throws Exception {
        this.setProperty();
        if (commonProperty == null) {
            System.out.println("配置为空");
            return;
        }

        /** 生成 entity文件 start **/
        this.genEntity();
        System.out.println("***** 生成Entity成功 ***** ");
        /** 生成 entity文件 end **/

        /** 生成 xml文件 start **/
        this.genXml();
        System.out.println("***** 生成Xml成功 ***** ");
        /** 生成 xml文件 end **/

        /** 生成 mapper文件 start **/
        this.genMapper();
        System.out.println("***** 生成Mapper成功 ***** ");
        /** 生成 mapper文件 end **/

        /** 生成 entity out 文件 start **/
        this.genEntityOut();
        System.out.println("***** 生成Entity Out 成功 ***** ");
        /** 生成 entity文件 end **/

        /** 生成 Entity Page In 文件 start **/
        this.genEntityPageIn();
        System.out.println("***** 生成Entity Page In 成功 ***** ");
        /** 生成 Entity Page In 文件 end **/

        /** 生成 Entity Page In 文件 start **/
        this.genEntityIn();
        System.out.println("***** 生成Entity In 成功 ***** ");
        /** 生成 Entity Page In 文件 end **/

        /** 生成 Entity Page In 文件 start **/
        this.genService();
        System.out.println("***** 生成Service 成功 ***** ");
        /** 生成 Entity Page In 文件 end **/

        /** 生成 Entity Page In 文件 start **/
        this.genImpl();
        System.out.println("***** 生成Impl 成功 ***** ");
        /** 生成 Entity Page In 文件 end **/

        /** 生成 Entity Page In 文件 start **/
        this.genController();
        System.out.println("***** 生成Controller 成功 ***** ");
        /** 生成 Entity Page In 文件 end **/

    }

    private void genController() throws Exception{
        List<String> importJavaPackageList = new ArrayList<>();


        // 生成java entity 文件
        var tpl = new JavaControllerTpl();
        // 包名
        tpl.setPackageName(commonProperty.getFullPackagePath() + ".controller");
        // 类名
        tpl.setClassName(commonProperty.getUpperTableName() + "Controller");

        // 父类
//        tpl.setFatherName(commonProperty.getServiceFatherName());
//        importJavaPackageList.add(commonProperty.getServiceFatherNamePackage());
        // 泛形
//        tpl.setEntityName(commonProperty.getJavaTpl().getClassName());
//        importJavaPackageList.add(commonProperty.getJavaTpl().getPackageName()+ "." + commonProperty.getJavaTpl().getClassName());
        // vo实体
        tpl.setVo(commonProperty.getVo());
        importJavaPackageList.add(commonProperty.getVoPackage());
        // 分页vo实体
        tpl.setPageVo(commonProperty.getPageVo());
        importJavaPackageList.add(commonProperty.getPageVoPackage());
        // 分页出参实体
        tpl.setPageOutName(commonProperty.getJavaOutTpl().getClassName());
        importJavaPackageList.add(commonProperty.getJavaOutTpl().getPackageName()+ "." + commonProperty.getJavaOutTpl().getClassName());
        // 分页入参实体
        tpl.setPageInName(commonProperty.getJavaPageInTpl().getClassName());
        importJavaPackageList.add(commonProperty.getJavaPageInTpl().getPackageName()+ "." + commonProperty.getJavaPageInTpl().getClassName());
        // 入参实体
        tpl.setInName(commonProperty.getJavaInTpl().getClassName());
        importJavaPackageList.add(commonProperty.getJavaInTpl().getPackageName()+ "." + commonProperty.getJavaInTpl().getClassName());
        // service
        tpl.setServiceName(commonProperty.getJavaServiceTpl().getClassName());
        importJavaPackageList.add(commonProperty.getJavaServiceTpl().getPackageName()+ "." + commonProperty.getJavaServiceTpl().getClassName());
        tpl.setServiceNameLower(StringUtil.toLowerCaseFirstOne(commonProperty.getJavaServiceTpl().getClassName()));

        // 表注释
        if (commonProperty.getTableComment() == null || commonProperty.getTableComment().equals("")) {
            tpl.setTableComment("");
        } else {
            tpl.setTableComment(commonProperty.getTableComment());
        }


        // 保存类信息
        commonProperty.setJavaControllerTpl(tpl);
        // 公共配置
        tpl.setCommonProperty(commonProperty);

        tpl.setImportJavaPackage(importJavaPackageList);


        // 要生成java文件所在的全相对地址
        String fileFullName = commonProperty.getJavaPath() + "/"
                + tpl.getPackageName().replace(".", "/") + "/" + tpl.getClassName() + ".java";

        Template xmlTemplate = this.getGenConfig().getTemplate(FTL_CONTROLLER);
        File xmlFile = new File(fileFullName);
        File xmlParentFile = xmlFile.getParentFile();
        // 创建文件目录
        if (!xmlParentFile.exists()) {
            xmlParentFile.mkdirs();
        }
        // 生成base mapper xml文件
        xmlTemplate.process(tpl, new FileWriter(xmlFile));
    }

    private void genImpl() throws Exception{
        List<String> importJavaPackageList = new ArrayList<>();


        // 生成java entity 文件
        var tpl = new JavaImplTpl();
        // 包名
        tpl.setPackageName(commonProperty.getFullPackagePath() + ".service.impl");
        // 类名
        tpl.setClassName(commonProperty.getUpperTableName() + "ServiceImpl");

        // 父类
        tpl.setFatherName(commonProperty.getImplFatherName());
        importJavaPackageList.add(commonProperty.getImplFatherNamePackage());
        // 泛形
        tpl.setEntityName(commonProperty.getJavaTpl().getClassName());
        importJavaPackageList.add(commonProperty.getJavaTpl().getPackageName()+ "." + commonProperty.getJavaTpl().getClassName());
        // vo实体
        tpl.setVo(commonProperty.getVo());
        importJavaPackageList.add(commonProperty.getVoPackage());
        // 分页vo实体
        tpl.setPageVo(commonProperty.getPageVo());
        importJavaPackageList.add(commonProperty.getPageVoPackage());
        // 分页出参实体
        tpl.setPageOutName(commonProperty.getJavaOutTpl().getClassName());
        importJavaPackageList.add(commonProperty.getJavaOutTpl().getPackageName()+ "." + commonProperty.getJavaOutTpl().getClassName());
        // 分页入参实体
        tpl.setPageInName(commonProperty.getJavaPageInTpl().getClassName());
        importJavaPackageList.add(commonProperty.getJavaPageInTpl().getPackageName()+ "." + commonProperty.getJavaPageInTpl().getClassName());
        // 入参实体
        tpl.setInName(commonProperty.getJavaInTpl().getClassName());
        importJavaPackageList.add(commonProperty.getJavaInTpl().getPackageName()+ "." + commonProperty.getJavaInTpl().getClassName());
        // mapper
        tpl.setMapperName(commonProperty.getMapperTpl().getClassName());
        importJavaPackageList.add(commonProperty.getMapperTpl().getPackageName()+ "." + commonProperty.getMapperTpl().getClassName());
        // service
        tpl.setServiceName(commonProperty.getJavaServiceTpl().getClassName());
        importJavaPackageList.add(commonProperty.getJavaServiceTpl().getPackageName()+ "." + commonProperty.getJavaServiceTpl().getClassName());
        // wrapper 类
        tpl.setWrapperName(commonProperty.getWrapperName());
        importJavaPackageList.add(commonProperty.getWrapperNamePackage());

        // 分页包
        importJavaPackageList.add("com.baomidou.mybatisplus.extension.plugins.pagination.Page");
        // 表关键字函数
        tpl.setKeyFunc("get" + StringUtil.toUpperCaseFirstOne(commonProperty.getColumnJavaKey()) + "()");

        // 保存类信息
        commonProperty.setJavaImplTpl(tpl);
        // 公共配置
        tpl.setCommonProperty(commonProperty);

        tpl.setImportJavaPackage(importJavaPackageList);

        tpl.setColumnBos(commonProperty.getColumnBos());


        // 要生成java文件所在的全相对地址
        String fileFullName = commonProperty.getJavaPath() + "/"
                + tpl.getPackageName().replace(".", "/") + "/" + tpl.getClassName() + ".java";

        Template xmlTemplate = this.getGenConfig().getTemplate(FTL_IMPL);
        File xmlFile = new File(fileFullName);
        File xmlParentFile = xmlFile.getParentFile();
        // 创建文件目录
        if (!xmlParentFile.exists()) {
            xmlParentFile.mkdirs();
        }
        // 生成base mapper xml文件
        xmlTemplate.process(tpl, new FileWriter(xmlFile));
    }

    private void genService() throws Exception{
        List<String> importJavaPackageList = new ArrayList<>();


        // 生成java entity 文件
        var tpl = new JavaServiceTpl();
        // 包名
        tpl.setPackageName(commonProperty.getFullPackagePath() + ".service");
        // 类名
        tpl.setClassName( "I" + commonProperty.getUpperTableName() + "Service");

        // 父类
        tpl.setFatherName(commonProperty.getServiceFatherName());
        importJavaPackageList.add(commonProperty.getServiceFatherNamePackage());
        // 泛形
        tpl.setEntityName(commonProperty.getJavaTpl().getClassName());
        importJavaPackageList.add(commonProperty.getJavaTpl().getPackageName()+ "." + commonProperty.getJavaTpl().getClassName());
        // vo实体
        tpl.setVo(commonProperty.getVo());
        importJavaPackageList.add(commonProperty.getVoPackage());
        // 分页vo实体
        tpl.setPageVo(commonProperty.getPageVo());
        importJavaPackageList.add(commonProperty.getPageVoPackage());
        // 分页出参实体
        tpl.setPageOutName(commonProperty.getJavaOutTpl().getClassName());
        importJavaPackageList.add(commonProperty.getJavaOutTpl().getPackageName()+ "." + commonProperty.getJavaOutTpl().getClassName());
        // 分页入参实体
        tpl.setPageInName(commonProperty.getJavaPageInTpl().getClassName());
        importJavaPackageList.add(commonProperty.getJavaPageInTpl().getPackageName()+ "." + commonProperty.getJavaPageInTpl().getClassName());
        // 入参实体
        tpl.setInName(commonProperty.getJavaInTpl().getClassName());
        importJavaPackageList.add(commonProperty.getJavaInTpl().getPackageName()+ "." + commonProperty.getJavaInTpl().getClassName());
        // 引包

        // 保存类信息
        commonProperty.setJavaServiceTpl(tpl);
        // 公共配置
        tpl.setCommonProperty(commonProperty);

        tpl.setImportJavaPackage(importJavaPackageList);


        // 要生成java文件所在的全相对地址
        String fileFullName = commonProperty.getJavaPath() + "/"
                + tpl.getPackageName().replace(".", "/") + "/" + tpl.getClassName() + ".java";

        Template xmlTemplate = this.getGenConfig().getTemplate(FTL_SERVICE);
        File xmlFile = new File(fileFullName);
        File xmlParentFile = xmlFile.getParentFile();
        // 创建文件目录
        if (!xmlParentFile.exists()) {
            xmlParentFile.mkdirs();
        }
        // 生成base mapper xml文件
        xmlTemplate.process(tpl, new FileWriter(xmlFile));
    }

    /**
     * 分页入参
     */
    private void genEntityIn() throws IOException, TemplateException {
        List<TableSchema> list = commonProperty.getColumnList();
        List<Column> columnBoList = new ArrayList<>();
        Set<String> importJavaPackage = new HashSet<>();
        for (TableSchema po : list) {
            var bo = new Column();
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
            // 分析是否属于java.lang的包
            if (bo.getJavaPackage().indexOf("java.lang") < 0){
                importJavaPackage.add(bo.getJavaPackage());
            }

            Set<String> rsList = new HashSet<>();
            // 分析是否要注解
            if (CREATE_TIME.equals(bo.getJdbcName())) {
                // 创建时间 注解
//                rsList.add("@TableField(value = \"create_time\", fill = FieldFill.INSERT)");
//                rsList.add("@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)");
//                rsList.add("@JsonFormat(pattern = \"yyyy-MM-dd HH:mm:ss\",timezone=\"GMT+8\")");

//                importJavaPackage.add("com.baomidou.mybatisplus.annotation.*");
//                importJavaPackage.add("org.springframework.format.annotation.DateTimeFormat");
//                importJavaPackage.add("com.fasterxml.jackson.annotation.JsonFormat");
                continue;

            } else if (UPDATE_TIME.equals(bo.getJdbcName())) {
                // 修改时间
//                rsList.add("@TableField(value = \"create_time\", fill = FieldFill.UPDATE)");
//                rsList.add("@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)");
//                rsList.add("@JsonFormat(pattern = \"yyyy-MM-dd HH:mm:ss\",timezone=\"GMT+8\")");

//                importJavaPackage.add("com.baomidou.mybatisplus.annotation.*");
//                importJavaPackage.add("org.springframework.format.annotation.DateTimeFormat");
//                importJavaPackage.add("com.fasterxml.jackson.annotation.JsonFormat");
                continue;
            } else if (IS_DELETE.equals(bo.getJdbcName())) {
                continue;
            }

            // 只要LocalDateTime字段，加上时间注解
            if ("LocalDateTime".equals(bo.getJavaType())) {
                rsList.add("@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)");
                importJavaPackage.add("org.springframework.format.annotation.DateTimeFormat");
            }

            List<String> rs = new ArrayList<>();
            rs.addAll(rsList);
            bo.setRs(rs);
            columnBoList.add(bo);
        }

        List<String> importJavaPackageList = new ArrayList<>();
        importJavaPackageList.addAll(importJavaPackage);

        // 生成java entity 文件
        var tpl = new JavaPageInTpl();
        // 类名
        tpl.setClassName(commonProperty.getUpperTableName() + "In");
        // 包名
        tpl.setPackageName(commonProperty.getFullPackagePath() + ".entity.in");
        // 父类
        tpl.setFatherName(commonProperty.getInFatherClass());
        // 父类包名
        importJavaPackageList.add(commonProperty.getInFatherClassPackage());
        // 保存类信息
        commonProperty.setJavaInTpl(tpl);
        // 公共配置
        tpl.setCommonProperty(commonProperty);

        tpl.setImportJavaPackage(importJavaPackageList);
        tpl.setColumnBos(columnBoList);

        // 要生成java文件所在的全相对地址
        String fileFullName = commonProperty.getJavaPath() + "/"
                + tpl.getPackageName().replace(".", "/") + "/" + tpl.getClassName() + ".java";

        Template xmlTemplate = this.getGenConfig().getTemplate(FTL_JAVA_PAGE_IN);
        File xmlFile = new File(fileFullName);
        File xmlParentFile = xmlFile.getParentFile();
        // 创建文件目录
        if (!xmlParentFile.exists()) {
            xmlParentFile.mkdirs();
        }
        // 生成base mapper xml文件
        xmlTemplate.process(tpl, new FileWriter(xmlFile));
    }

    /**
     * 分页入参
     */
    private void genEntityPageIn() throws IOException, TemplateException {
        List<TableSchema> list = commonProperty.getColumnList();
        List<Column> columnBoList = new ArrayList<>();
        Set<String> importJavaPackage = new HashSet<>();
        for (TableSchema po : list) {
            var bo = new Column();
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
            // 分析是否属于java.lang的包
            if (bo.getJavaPackage().indexOf("java.lang") < 0){
                importJavaPackage.add(bo.getJavaPackage());
            }
            // get 函数
            bo.setGetLambda("get" + StringUtil.toUpperCaseFirstOne(bo.getJavaName()));

            Set<String> rsList = new HashSet<>();
            // 分析是否要注解
            if (CREATE_TIME.equals(bo.getJdbcName())) {
                // 创建时间 注解
//                rsList.add("@TableField(value = \"create_time\", fill = FieldFill.INSERT)");
                rsList.add("@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)");
//                rsList.add("@JsonFormat(pattern = \"yyyy-MM-dd HH:mm:ss\",timezone=\"GMT+8\")");

//                importJavaPackage.add("com.baomidou.mybatisplus.annotation.*");
                importJavaPackage.add("org.springframework.format.annotation.DateTimeFormat");
//                importJavaPackage.add("com.fasterxml.jackson.annotation.JsonFormat");


            } else if (UPDATE_TIME.equals(bo.getJdbcName())) {
                // 修改时间
//                rsList.add("@TableField(value = \"create_time\", fill = FieldFill.UPDATE)");
                rsList.add("@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)");
//                rsList.add("@JsonFormat(pattern = \"yyyy-MM-dd HH:mm:ss\",timezone=\"GMT+8\")");

//                importJavaPackage.add("com.baomidou.mybatisplus.annotation.*");
                importJavaPackage.add("org.springframework.format.annotation.DateTimeFormat");
//                importJavaPackage.add("com.fasterxml.jackson.annotation.JsonFormat");

            } else if (IS_DELETE.equals(bo.getJdbcName())) {
                continue;
            }

            // 只要LocalDateTime字段，加上时间注解
            if ("LocalDateTime".equals(bo.getJavaType())) {
                rsList.add("@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)");
                importJavaPackage.add("org.springframework.format.annotation.DateTimeFormat");
            }

            List<String> rs = new ArrayList<>();
            rs.addAll(rsList);
            bo.setRs(rs);
            columnBoList.add(bo);
        }
        //
        commonProperty.setColumnBos(columnBoList);

        List<String> importJavaPackageList = new ArrayList<>();
        importJavaPackageList.addAll(importJavaPackage);

        // 生成java entity 文件
        var tpl = new JavaPageInTpl();
        // 类名
        tpl.setClassName(commonProperty.getUpperTableName() + "PageIn");
        // 包名
        tpl.setPackageName(commonProperty.getFullPackagePath() + ".entity.in");
        // 父类
        tpl.setFatherName(commonProperty.getInFatherPageClass());
        // 父类包名
        importJavaPackageList.add(commonProperty.getInFatherPageClassPackage());
        // 保存类信息
        commonProperty.setJavaPageInTpl(tpl);

        // 公共配置
        tpl.setCommonProperty(commonProperty);

        tpl.setImportJavaPackage(importJavaPackageList);
        tpl.setColumnBos(columnBoList);

        // 要生成java文件所在的全相对地址
        String fileFullName = commonProperty.getJavaPath() + "/"
                + tpl.getPackageName().replace(".", "/") + "/" + tpl.getClassName() + ".java";

        Template xmlTemplate = this.getGenConfig().getTemplate(FTL_JAVA_PAGE_IN);
        File xmlFile = new File(fileFullName);
        File xmlParentFile = xmlFile.getParentFile();
        // 创建文件目录
        if (!xmlParentFile.exists()) {
            xmlParentFile.mkdirs();
        }
        // 生成base mapper xml文件
        xmlTemplate.process(tpl, new FileWriter(xmlFile));
    }

    /**
     * 生成实体
     */
    private void genEntityOut() throws IOException, TemplateException {
        List<TableSchema> list = commonProperty.getColumnList();
        List<Column> columnBoList = new ArrayList<>();
        Set<String> importJavaPackage = new HashSet<>();
        for (TableSchema po : list) {
            var bo = new Column();
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
            // 分析是否属于java.lang的包
            if (bo.getJavaPackage().indexOf("java.lang") < 0){
                importJavaPackage.add(bo.getJavaPackage());
            }

            List<String> rsList = new ArrayList<>();
            // 分析是否要注解
            if (CREATE_TIME.equals(bo.getJdbcName())) {
                // 创建时间 注解
//                rsList.add("@TableField(value = \"create_time\", fill = FieldFill.INSERT)");
//                rsList.add("@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)");
                rsList.add("@JsonFormat(pattern = \"yyyy-MM-dd HH:mm:ss\",timezone=\"GMT+8\")");

//                importJavaPackage.add("com.baomidou.mybatisplus.annotation.*");
//                importJavaPackage.add("org.springframework.format.annotation.DateTimeFormat");
                importJavaPackage.add("com.fasterxml.jackson.annotation.JsonFormat");


            } else if (UPDATE_TIME.equals(bo.getJdbcName())) {
                // 修改时间
//                rsList.add("@TableField(value = \"create_time\", fill = FieldFill.UPDATE)");
//                rsList.add("@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)");
                rsList.add("@JsonFormat(pattern = \"yyyy-MM-dd HH:mm:ss\",timezone=\"GMT+8\")");

//                importJavaPackage.add("com.baomidou.mybatisplus.annotation.*");
//                importJavaPackage.add("org.springframework.format.annotation.DateTimeFormat");
                importJavaPackage.add("com.fasterxml.jackson.annotation.JsonFormat");

            } else if (IS_DELETE.equals(bo.getJdbcName())) {
                continue;
            }


            bo.setRs(rsList);
            columnBoList.add(bo);
        }

        List<String> importJavaPackageList = new ArrayList<>();
        importJavaPackageList.addAll(importJavaPackage);

        // 生成java entity 文件
        var tpl = new JavaOutTpl();
        // 类名
        tpl.setClassName(commonProperty.getUpperTableName() + "Out");
        // 包名
        tpl.setPackageName(commonProperty.getFullPackagePath() + ".entity.out");
        //
        commonProperty.setJavaOutTpl(tpl);
        // 公共配置
        tpl.setCommonProperty(commonProperty);

        tpl.setImportJavaPackage(importJavaPackageList);
        tpl.setColumnBos(columnBoList);

        // 要生成java文件所在的全相对地址
        String fileFullName = commonProperty.getJavaPath() + "/"
                + tpl.getPackageName().replace(".", "/") + "/" + tpl.getClassName() + ".java";

        Template xmlTemplate = this.getGenConfig().getTemplate(FTL_JAVA_OUT);
        File xmlFile = new File(fileFullName);
        File xmlParentFile = xmlFile.getParentFile();
        // 创建文件目录
        if (!xmlParentFile.exists()) {
            xmlParentFile.mkdirs();
        }
        // 生成base mapper xml文件
        xmlTemplate.process(tpl, new FileWriter(xmlFile));
    }

    /**
     * 生成 entity文件
     */
    private void genEntity() throws Exception {
        // 获取字段属性
        List<TableSchema> list = this.getDbData(commonProperty.getUrl(), commonProperty.getDrive(), commonProperty.getUser(), commonProperty.getPassword(), commonProperty.getTableName(), commonProperty.getDbName());
        commonProperty.setColumnList(list);
        // 设置主键字段
        commonProperty.setTableComment(this.getTableComment(commonProperty.getTableName(), commonProperty.getDbName()));

        for (TableSchema e : list) {
            if (e.isColumnKey()) {
                commonProperty.setColumnJavaKey(StringUtil.underlineToHump(e.getColumnName()));
            }
        }

        if (commonProperty.getColumnJavaKey() == null || commonProperty.getColumnJavaKey().equals("")) {
            throw new RuntimeException("表没有关键字");

        }

        // 解析字段
        List<Column> columnBoList = new ArrayList<>();
        Set<String> importJavaPackage = new HashSet<>();
        for (TableSchema po : list) {
            var bo = new Column();
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
            // 分析是否属于java.lang的包
            if (bo.getJavaPackage().indexOf("java.lang") < 0){
                importJavaPackage.add(bo.getJavaPackage());
            }


            List<String> rsList = new ArrayList<>();
            // 分析是否要注解
            if (CREATE_TIME.equals(bo.getJdbcName())) {
                // 创建时间 注解
                rsList.add("@TableField(value = \"create_time\", fill = FieldFill.INSERT)");
//                rsList.add("@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)");
//                rsList.add("@JsonFormat(pattern = \"yyyy-MM-dd HH:mm:ss\",timezone=\"GMT+8\")");

                importJavaPackage.add("com.baomidou.mybatisplus.annotation.*");
//                importJavaPackage.add("org.springframework.format.annotation.DateTimeFormat");
//                importJavaPackage.add("com.fasterxml.jackson.annotation.JsonFormat");

            } else if (UPDATE_TIME.equals(bo.getJdbcName())) {
                // 修改时间
                rsList.add("@TableField(value = \"create_time\", fill = FieldFill.UPDATE)");
//                rsList.add("@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)");
//                rsList.add("@JsonFormat(pattern = \"yyyy-MM-dd HH:mm:ss\",timezone=\"GMT+8\")");

                importJavaPackage.add("com.baomidou.mybatisplus.annotation.*");
//                importJavaPackage.add("org.springframework.format.annotation.DateTimeFormat");
//                importJavaPackage.add("com.fasterxml.jackson.annotation.JsonFormat");
            } else if (IS_DELETE.equals(bo.getJdbcName())) {
                // 删除标记
                rsList.add("@TableLogic(value = \"" + UN_DELETE +"\", delval = \"" + DELETE_VAL + "\")");

                importJavaPackage.add("com.baomidou.mybatisplus.annotation.*");
            }

            if (po.isColumnKey()) {
                // 主键
                rsList.add("@TableId(value = \"" + po.getColumnName() + "\", type = IdType.AUTO)");
                importJavaPackage.add("com.baomidou.mybatisplus.annotation.*");
            }
            bo.setRs(rsList);
            columnBoList.add(bo);
        }
        //
//        commonProperty.setColumnBos(columnBoList);

        List<String> importJavaPackageList = new ArrayList<>();
        importJavaPackageList.addAll(importJavaPackage);

        // 生成java entity 文件
        var tpl = new JavaTpl();
        // 类名
        tpl.setClassName(commonProperty.getUpperTableName());
        // 包名
        tpl.setPackageName(commonProperty.getFullPackagePath() + ".entity");
        // 保存类信息
        commonProperty.setJavaTpl(tpl);
        // 公共配置
        tpl.setCommonProperty(commonProperty);

        tpl.setImportJavaPackage(importJavaPackageList);
        tpl.setColumnBos(columnBoList);

        // 要生成java文件所在的全相对地址
        String fileFullName = commonProperty.getJavaPath() + "/"
                + tpl.getPackageName().replace(".", "/") + "/" + tpl.getClassName() + ".java";

        Template xmlTemplate = this.getGenConfig().getTemplate(FTL_JAVA);
        File xmlFile = new File(fileFullName);
        File xmlParentFile = xmlFile.getParentFile();
        // 创建文件目录
        if (!xmlParentFile.exists()) {
            xmlParentFile.mkdirs();
        }
        // 生成base mapper xml文件
        xmlTemplate.process(tpl, new FileWriter(xmlFile));

    }

    /**
     * 生成 mapper.java
     */
    private void genMapper() throws IOException, TemplateException {
        MapperTpl tpl = new MapperTpl();
        // 包名
        tpl.setPackageName(commonProperty.getFullPackagePath() + ".mapper");
        // 引包
        List<String> importJavaPackage = new ArrayList<>();
        // plus包
        importJavaPackage.add("com.baomidou.mybatisplus.core.mapper.BaseMapper");
        // 实体包
        importJavaPackage.add(commonProperty.getFullPackagePath() + ".entity" + "." + commonProperty.getUpperTableName() );
        tpl.setImportJavaPackage(importJavaPackage);

        //
        commonProperty.setMapperTpl(tpl);
        // 公共属性
        tpl.setCommonProperty(commonProperty);
        // 类名
        tpl.setClassName(commonProperty.getUpperTableName() + "Mapper");
        // 继承的父类 mybatis-plus
        tpl.setFatherName("BaseMapper");

        // 实体类泛形
        tpl.setEntityName(commonProperty.getUpperTableName());

        // 要生成java文件所在的全相对地址
        String fileFullName = commonProperty.getJavaPath() + "/"
                + tpl.getPackageName().replace(".", "/") + "/" + tpl.getClassName() + ".java";

        Template xmlTemplate = this.getGenConfig().getTemplate(FTL_MAPPER);
        File xmlFile = new File(fileFullName);
        File xmlParentFile = xmlFile.getParentFile();
        // 创建文件目录
        if (!xmlParentFile.exists()) {
            xmlParentFile.mkdirs();
        }
        // 生成base mapper xml文件
        xmlTemplate.process(tpl, new FileWriter(xmlFile));
    }

    /**
     * 生成 xml
     *
     * @throws IOException
     * @throws TemplateException
     */
    private void genXml() throws IOException, TemplateException {
        XmlTpl xml = new XmlTpl();

        // 命名空间
        xml.setNamespace(commonProperty.getFullPackagePath() + ".mapper." + commonProperty.getUpperTableName() + "Mapper");
        // 路径
        String xmlFileFullName = commonProperty.getMapperPath()  + "/"  + commonProperty.getModuleName() + "/" + commonProperty.getUpperTableName() + "Mapper" + ".xml";

        // 模板文件
        Template xmlTemplate = this.getGenConfig().getTemplate(FTL_XML);
        File xmlFile = new File(xmlFileFullName);
        File xmlParentFile = xmlFile.getParentFile();
        // 创建文件目录
        if (!xmlParentFile.exists()) {
            xmlParentFile.mkdirs();
        }
        // 生成base mapper xml文件
        xmlTemplate.process(xml, new FileWriter(xmlFile));
        /**** 生成base mapper.xml文件 end   ****/
    }

    /**
     * 获取数据库映射数据
     *
     * @param url
     * @param drive
     * @param password
     * @return
     */
    private List<TableSchema> getDbData(String url, String drive, String user, String password, String tableName, String dbName) throws SQLException, ClassNotFoundException {
        // 查询sql
        String sql = "select `data_type`, `column_name`, `column_comment`, `column_key` FROM information_schema.columns where `table_name` = ? and `table_schema` = ? ORDER BY ORDINAL_POSITION asc";

        PreparedStatement preparedStatement = DBUtil.getConnection(drive, url, user, password).prepareStatement(sql);
        preparedStatement.setString(1, tableName);
        preparedStatement.setString(2, dbName);
        ResultSet resultSet = preparedStatement.executeQuery();
        // 不能把student在循环外面创建，要不list里面六个对象都是一样的，都是最后一个的值，
        // 因为list add进去的都是引用
        var list = new ArrayList<TableSchema>();
        while (resultSet.next()) {
            TableSchema po = new TableSchema();
            po.setDataType(resultSet.getString(1));
            po.setColumnName(resultSet.getString(2));

            po.setColumnComment(resultSet.getString(3));

            if (resultSet.getString(4) != null && !resultSet.getString(4).equals("") ) {
                po.setColumnKey(true);
            }

            list.add(po);
        }
        return list;
    }

    private String getTableComment(String tableName, String dbName) throws Exception {
        // 查询sql
        String sql = "select TABLE_NAME,TABLE_COMMENT from INFORMATION_SCHEMA.Tables WHERE  `table_name` = ? and `table_schema` = ?";

        PreparedStatement preparedStatement = DBUtil.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, tableName);
        preparedStatement.setString(2, dbName);
        ResultSet resultSet = preparedStatement.executeQuery();
        // 不能把student在循环外面创建，要不list里面六个对象都是一样的，都是最后一个的值，
        // 因为list add进去的都是引用

        while (resultSet.next()) {
            return resultSet.getString(2);
        }
        return null;
    }

    private Configuration getGenConfig() {
        Version version = new Version("2.3.0");
        Configuration config = new Configuration(version);
        config.setTemplateLoader(new ClassTemplateLoader(this.getClass(), FTL_PATH));

        return config;
    }

}
