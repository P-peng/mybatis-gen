package com.ge.mybatis.main;

import com.ge.mybatis.entity.TableSchemaPo;
import com.ge.mybatis.mapper.TableSchemaMapper;
import com.ge.mybatis.utils.SqlSessionFactoryUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.Iterator;
import java.util.List;

/**
 * @author dengzhipeng
 * @date 2019/06/21
 */
public class MybatisMain {
    public static void main(String[] args){
        SqlSessionFactory factory = SqlSessionFactoryUtil.sqlSessionFactory;

        //true 不开启事务，自动提交
        SqlSession sqlSession = factory.openSession(true);
        TableSchemaMapper mapper = sqlSession.getMapper(TableSchemaMapper.class);
        List<TableSchemaPo> list = mapper.select("ge", "ge");
        Iterator i = list.iterator();
        while (i.hasNext()){
            System.out.println(i.next());
        }
    }
}
