package com.ge.generate.utils;

/**
 * 字符串工具类
 * @author dengzhipeng
 * @date 2019/06/29
 */
public class StringUtil {

    /**
     * 下划线转驼峰法
     * @param args
     * @return
     */
    public static String underlineToHump(String args){
        StringBuilder result = new StringBuilder();
        if (!args.contains("_")){
            return args;
        }
        String[] array = args.split("_");
        for(String s : array){
            if(result.length() == 0){
                result.append(s.toLowerCase());
            }else{
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    /**
     * 首字母转大写
     * @param s
     * @return
     */
    public static String toUpperCaseFirstOne(String s){
        if(Character.isUpperCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }
}
