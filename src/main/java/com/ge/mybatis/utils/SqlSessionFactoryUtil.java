package com.ge.mybatis.utils;

import com.ge.mybatis.mapper.TableSchemaMapper;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author dengzhipeng
 * @date 2019/06/21
 */
public class SqlSessionFactoryUtil {

    /**
     * 首先创建静态成员变量sqlSessionFactory，静态变量被所有的对象所共享。
     */
    public static SqlSessionFactory sqlSessionFactory = null;

    private SqlSessionFactoryUtil() {
    }

    // 使用静态代码块保证线程安全问题
    static {


    }


    public static SqlSessionFactory init() {
        // 数据库连接池信息
        String resource = "mybatisConfig.xml";
        try {
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

            return sqlSessionFactory;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

