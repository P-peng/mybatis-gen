package com.ge.generate.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dengzhipeng
 * @date 2019/07/01
 */
@Getter
@Setter
public class CommonPropertyBo {
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
     * dto实体类模块路径
     */
    private String dtoModulePath;
    /**
     * dto实体类包路径
     */
    private String dtoPackagePath;
    /**
     * 表名
     */
    private String tableName;
    /**
     * 数据库名
     */
    private String databaseName;
    /**
     * 包下模块名字(模块的包名)，已经存在的模块包(文件夹)不会删除
     */
    private String moduleName;
    /**
     * 日期
     */
    private String date;


}
