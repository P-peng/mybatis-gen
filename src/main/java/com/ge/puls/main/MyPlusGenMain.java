package com.ge.puls.main;

import com.ge.generate.db.DBUtil;
import com.ge.generate.entity.ColumnBo;
import com.ge.generate.entity.TableSchemaPo;
import com.ge.generate.utils.MysqlUtil;
import com.ge.generate.utils.StringUtil;
import com.ge.puls.entity.Column;
import com.ge.puls.entity.PlusProperty;
import com.ge.puls.entity.TableSchema;
import com.ge.puls.entity.XmlTpl;
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
import java.text.SimpleDateFormat;
import java.util.*;

public class MyPlusGenMain {

    // 模板路径
    final static String FTL_PATH = "/template/plus/";
    // XML 模板
    final static String FTL_XML = "PlusMapperXml.xml.ftl";

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
        // 单表
        var tableName = "ge";
        var moduleName = "ge";

        // 数据库信息
        var drive = "com.mysql.jdbc.Driver";
        var url = "jdbc:mysql://120.55.162.42:3306/ge?useSSL=false";
        var user = "root";
        var password = "Lv123456+";

        // 公共配置
        var author = "dengzhipeng";
        var version = "1.0";

        // 基础目录
        var modulePath = System.getProperty("user.dir") + "/src/main";
        // 包名
        var packagePath = "com.ge";
        // java目录
        var javaPath = modulePath + "/java";
        // xml目录
        var mapperPath = modulePath + "/resources/mappers";

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

        var main = new MyPlusGenMain();
        main.commonProperty = commonProperty;
        main.run();

    }

    private void setProperty() {
        // 全包名
        if (commonProperty.getModuleName() == null || commonProperty.getModuleName().isBlank()) {
            commonProperty.setFullPackagePath(commonProperty.getPackagePath());
        } else {
            commonProperty.setFullPackagePath(commonProperty.getPackagePath() + "." + commonProperty.getModuleName());
        }
        // 表名字转换
        commonProperty.setHumpTableName(StringUtil.underlineToHumpAndFirstToUpper(commonProperty.getTableName()));
        commonProperty.setUpperTableName(StringUtil.toUpperCaseFirstOne(commonProperty.getHumpTableName()));
    }

    public void run() throws IOException, TemplateException, SQLException, ClassNotFoundException {
        this.setProperty();
        if (commonProperty == null) {
            System.out.println("配置为空");
            return;
        }


        /** 生成 entity文件 start **/
        this.genEntity();
        /** 生成 entity文件 end **/

        /** 生成 xml文件 start **/
        this.genXml();
        /** 生成 xml文件 end **/

        /** 生成 mapper文件 start **/
//        this.genMapper();
        /** 生成 mapper文件 end **/

    }

    /**
     * 生成 entity文件
     */
    private void genEntity() throws SQLException, ClassNotFoundException {
        // 获取字段属性
        List<TableSchema> list = this.getDbData(commonProperty.getUrl(), commonProperty.getDrive(), commonProperty.getUser(), commonProperty.getPassword(), commonProperty.getTableName());

        // 解析字段
        List<Column> columnBoList = new ArrayList();
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
                rsList.add("@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)");
                rsList.add("@JsonFormat(pattern = \"yyyy-MM-dd HH:mm:ss\",timezone=\"GMT+8\")");

                importJavaPackage.add("import com.baomidou.mybatisplus.annotation.*;");
                importJavaPackage.add("import com.fasterxml.jackson.annotation.JsonFormat;");
                importJavaPackage.add("import org.springframework.format.annotation.DateTimeFormat;");

            } else if (UPDATE_TIME.equals(bo.getJdbcName())) {
                // 修改时间
                rsList.add("@TableField(value = \"create_time\", fill = FieldFill.UPDATE)");
                rsList.add("@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)");
                rsList.add("@JsonFormat(pattern = \"yyyy-MM-dd HH:mm:ss\",timezone=\"GMT+8\")");

                importJavaPackage.add("import com.baomidou.mybatisplus.annotation.*;");
                importJavaPackage.add("import com.fasterxml.jackson.annotation.JsonFormat;");
                importJavaPackage.add("import org.springframework.format.annotation.DateTimeFormat;");
            } else if (IS_DELETE.equals(bo.getJdbcName())) {
                // 删除标记
                rsList.add("@TableLogic(value = \"" + UN_DELETE +"\", delval = \"" + DELETE_VAL + "\")");

                importJavaPackage.add("import com.baomidou.mybatisplus.annotation.*;");
            }

            if (po.isColumnKey()) {
                // 主键
                rsList.add("@TableId(value = \"" + po.getColumnName() + "\", type = IdType.AUTO)");
                importJavaPackage.add("import com.baomidou.mybatisplus.annotation.*;");
            }
            bo.setRs(rsList);
            columnBoList.add(bo);
        }
        System.out.println();
    }

    /**
     * 生成 mapper.java
     */
    private void genMapper() {
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
    private List<TableSchema> getDbData(String url, String drive, String user, String password, String tableName) throws SQLException, ClassNotFoundException {
        // 查询sql
        String sql = "select `data_type`, `column_name`, `column_comment`, `column_key` FROM information_schema.columns where table_name = ?";

        PreparedStatement preparedStatement = DBUtil.getConnection(drive, url, user, password).prepareStatement(sql);
        preparedStatement.setString(1, tableName);
        ResultSet resultSet = preparedStatement.executeQuery();
        // 不能把student在循环外面创建，要不list里面六个对象都是一样的，都是最后一个的值，
        // 因为list add进去的都是引用
        var list = new ArrayList<TableSchema>();
        while (resultSet.next()) {
            TableSchema po = new TableSchema();
            po.setDataType(resultSet.getString(1));
            po.setColumnName(resultSet.getString(2));

            po.setColumnComment(resultSet.getString(3));

            if (resultSet.getString(4) != null && !resultSet.getString(4).equals("") && !resultSet.getString(3).isBlank()) {
                po.setColumnKey(true);
            }

            list.add(po);
        }
        return list;
    }

    private Configuration getGenConfig() {
        Version version = new Version("2.3.0");
        Configuration config = new Configuration(version);
        config.setTemplateLoader(new ClassTemplateLoader(this.getClass(), FTL_PATH));

        return config;
    }

}
