package com.ge.generate.mian;

import com.ge.generate.entity.ColumnBo;
import com.ge.generate.utils.MysqlUtil;
import com.ge.mybatis.entity.TableSchemaPo;
import com.ge.mybatis.mapper.TableSchemaMapper;
import com.ge.mybatis.utils.MapperUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dengzhipeng
 * @date 2019/06/21
 */
public class GenerateMain {
    private static String templateBaseJava = "src/template/TemplateBaseJava.java";

    private static String generatePath = "com.ge.generate.test";

    public static void main(String[] args){
        GenerateMain main = new GenerateMain();
        try {
            main.go();
        } catch (Exception e) {
            System.out.println("生成java文件失败");
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public void go(){

    }

    /**
     * 生成java文件
     * @throws IOException
     */
    public void generateJava() throws IOException {
        Version version = new Version("2.3.0");
        Configuration config = new Configuration(version);
        Template template = config.getTemplate(templateBaseJava);

    }

    /**
     * 解析表字段
     */
    public void analyzeTableColumn(){
        TableSchemaMapper mapper = (TableSchemaMapper) MapperUtil.getMapper(TableSchemaMapper.class);
        List<TableSchemaPo> list =  mapper.select("ge");
        List<ColumnBo> columnBoList = new ArrayList();
        for (TableSchemaPo po : list) {
            ColumnBo bo = new ColumnBo();
            bo.setJavaName(po.getColumnName());
            // 分析对应的java类型
            bo.setJavaType(MysqlUtil.analyzeColumnName(po.getDataType()));
            // 分析对应所在的java包
            bo.setJavaPackage(MysqlUtil.analyzeColumnJavaPackage(po.getDataType()));
            // sql字段加前缀a_
            bo.setJdbcName("a_" + po.getColumnName());
            bo.setComment(po.getColumnComment());
            // 分析是否属于
            columnBoList.add(bo);
        }

        System.out.println("123");
    }



}
