package com.ge.generate.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 用于mysql的字段类型向java对应转换工具类。
 * 示例：调用 analyzeColumnName("int") return Integer   调用 analyzeColumnJavaPackage("int") return java.lang.Integer
 *
 * @author dengzhipeng
 * @date 2019/06/28
 */
public class MysqlUtil {
    /**
     *  mysql字段对应java包装类
     */
    private static final Map<String, JavaColumn> MYSQL_MAPPING_JAVA_MAP = new HashMap<>(32){{
        put("int", new JavaColumn("Integer", Integer.class.getName(), "INTEGER"));
        put("tinyint", new JavaColumn("Integer", Integer.class.getName(), "INTEGER"));
        put("char", new JavaColumn("String", String.class.getName(), "CHAR"));
        put("varchar", new JavaColumn("String", String.class.getName(), "VARCHAR"));
        put("longtext",  new JavaColumn("String", String.class.getName(), "LONGVARCHAR"));
        put("bit", new JavaColumn("Byte", Byte.class.getName(), "BIT"));
        put("bigint", new JavaColumn("BigInteger", BigInteger.class.getName(), "BIGINT"));
        put("float", new JavaColumn("Float", Float.class.getName(), "REAL"));
        put("double",  new JavaColumn("Double", Double.class.getName(), "DOUBLE"));
        put("decimal", new JavaColumn("BigDecimal", BigDecimal.class.getName(), "DECIMAL"));
        put("datetime", new JavaColumn("LocalDateTime", LocalDateTime.class.getName(), "TIMESTAMP"));
        put("date",  new JavaColumn("Date", Date.class.getName(), "TIMESTAMP"));
        put("timestamp",  new JavaColumn("Date", Date.class.getName(), "TIMESTAMP"));
        put("json", new JavaColumn("String", String.class.getName(), "json"));
    }};

    /**
     * Mysql关键字
     */
    private static final Set<String> MYSQL_KEY = new HashSet<String>(16){
        {
            add("to");
            add("from");
            add("value");
            add("timestamp");
        }
    };

    /**
     * 传入sql字段类型，返回java字段名字
     * @param sqlType
     * @return
     */
    public static String analyzeColumnName(String sqlType){
        return MYSQL_MAPPING_JAVA_MAP.get(sqlType).getName();
    }

    /**
     * 传入sql字段类型，返回java字段所在的包
     * @param sqlType
     * @return
     */
    public static String analyzeColumnJavaPackage(String sqlType){
        return MYSQL_MAPPING_JAVA_MAP.get(sqlType).getJavaPackage();
    }

    /**
     * 传入sql字段类型，返回对应mybatis字段类型
     * @param sqlType
     * @return
     */
    public static String analyzeColumnJdbcType(String sqlType){
        return MYSQL_MAPPING_JAVA_MAP.get(sqlType).getJdbcType();
    }

    /**
     * Mysql关键字转换
     */

}


class JavaColumn{
    private String name;
    private String javaPackage;
    private String jdbcType;

    public JavaColumn(){}

    public JavaColumn(String name, String javaPackage) {
        this.name = name;
        this.javaPackage = javaPackage;
    }

    public JavaColumn(String name, String javaPackage, String jdbcType) {
        this.name = name;
        this.javaPackage = javaPackage;
        this.jdbcType = jdbcType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJavaPackage() {
        return javaPackage;
    }

    public void setJavaPackage(String javaPackage) {
        this.javaPackage = javaPackage;
    }

    public String getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }
}