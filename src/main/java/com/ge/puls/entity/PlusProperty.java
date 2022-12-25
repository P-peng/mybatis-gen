package com.ge.puls.entity;

import lombok.Getter;
import lombok.Setter;

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
}
