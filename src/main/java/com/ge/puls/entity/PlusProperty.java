package com.ge.puls.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PlusProperty {

    /**
     * 驱动
     */
    private String drive;

    /**
     * url
     */
    private String url;

    /**
     * 用户名
     */
    private String user;

    /**
     * 密码
     */
    private String password;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 包下模块名字(模块的包名)，已经存在的模块包(文件夹)不会删除
     */
    private String moduleName;

    /**
     * 作者
     */
    private String author;

    /**
     * 版本号
     */
    private String version;

    /**
     * 模块路径
     */
    private String modulePath;

    /**
     * 包路径
     */
    private String packagePath;

    /**
     * mapper映射目录
     */
    private String mapperPath;

    /**
     * java目录
     */
    private String javaPath;

    /**
     * 日期
     */
    private String date;

    /**
     * 全包路径（com.）
     */
    private String fullPackagePath;

    /**
     * 全表名（驼峰）
     */
    private String humpTableName;

    /**
     * 首字母大写
     */
    private String upperTableName;

    //字段信息
    /**
     * 字段集合
     */
    private List<TableSchema> columnList;
    /**
     * 字段主键
     */
    private String columnJavaKey;
    /**
     * 字段主键
     */
    private String tableComment;

    // 入参类配置
    /**
     * In java继承的类
     */
    private String inFatherPageClass;

    /**
     * In java继承的类
     */
    private String inFatherPageClassPackage;

    /**
     * In java继承的类
     */
    private String inFatherClass;

    /**
     * In java继承的类
     */
    private String inFatherClassPackage;

    // service配置
    /**
     * vo
     */
    private String vo;
    /**
     * 实体vo包
     */
    private String voPackage;
    /**
     * 实体分页vo
     */
    private String pageVo;
    /**
     * 实体分页vo包
     */
    private String pageVoPackage;
    /**
     * service 继承类
     */
    private String serviceFatherName;
    /**
     * service 继承包
     */
    private String serviceFatherNamePackage;
    /**
     * impl 继承类
     */
    private String implFatherName;
    /**
     * impl 继承包
     */
    private String implFatherNamePackage;

    // tel类
    // java生成信息
    private JavaTpl javaTpl;

    // mapper tel
    private MapperTpl mapperTpl;

    // java in 生成信息
    private JavaPageInTpl javaInTpl;

    // java in 生成信息
    private JavaOutTpl javaOutTpl;

    // java page in生成信息
    private JavaPageInTpl javaPageInTpl;

    // java page in生成信息
    private JavaServiceTpl javaServiceTpl;

    // java page in生成信息
    private JavaImplTpl javaImplTpl;

    // java page in生成信息
    private JavaControllerTpl javaControllerTpl;

}
