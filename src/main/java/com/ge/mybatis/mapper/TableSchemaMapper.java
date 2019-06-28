package com.ge.mybatis.mapper;

import com.ge.mybatis.entity.TableSchemaPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author dengzhipeng
 * @date 2019/06/21
 */
public interface TableSchemaMapper {
    /**
     * 获取表字段名、属性、注释
     * @param tableName
     * @return
     */
    List<TableSchemaPo> select(@Param("tableName")String tableName);
}
