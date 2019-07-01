package com.ge.generate.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 用于读取properties文件的属性
 * @author dengzhipeng
 * @date 2019/06/28
 */
public class ReadPropertiesUtil {
    private static Properties pro;
    /**
     * 读取指定properties，指定属性
     * @param name
     * @return
     */
    public static String readByName(String name) throws IOException {
        if (pro == null){
            new Exception("为加载位置文件");
        }
        return pro.getProperty(name);
    }

    /**
     * 初始化读取配置文件
     * @param path
     * @throws IOException
     */
    public static void intiReadProperties(String path) throws IOException {
        pro = new Properties();
        FileInputStream in = new FileInputStream(path);
        pro.load(in);
        in.close();
    }
}
