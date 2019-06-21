package com.ge.mybatis.mapper;

import com.ge.mybatis.entity.TableSchemaPo;

import java.util.List;

/**
 * @author dengzhipeng
 * @date 2019/06/21
 */
public interface TableSchemaMapper {
    List<TableSchemaPo> select();
}
