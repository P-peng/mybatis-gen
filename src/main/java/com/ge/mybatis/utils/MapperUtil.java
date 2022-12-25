package com.ge.mybatis.utils;

import com.ge.mybatis.mapper.TableSchemaMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * @author dengzhipeng
 * @date 2019/06/21
 */
public class MapperUtil {
    public static Object getMapper(Class cla){
        SqlSessionFactoryUtil.init();
        SqlSessionFactory factory = SqlSessionFactoryUtil.sqlSessionFactory;
        //true 不开启事务，自动提交
        SqlSession sqlSession = factory.openSession(true);
        return sqlSession.getMapper(cla);
    }
}
