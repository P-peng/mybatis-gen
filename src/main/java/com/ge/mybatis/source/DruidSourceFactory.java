package com.ge.mybatis.source;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.datasource.DataSourceFactory;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author dengzhipeng
 * @date 2019/06/21
 */
public class DruidSourceFactory implements DataSourceFactory {
    private Properties props;

    public void setProperties(Properties props) {
        this.props = props;
    }

    public DataSource getDataSource() {
        DruidDataSource dds = new DruidDataSource();
        dds.setDriverClassName((String) this.props.get("driverClassName"));
        dds.setUrl((String) this.props.get("url"));
        dds.setUsername((String) this.props.get("username"));
        dds.setPassword((String) this.props.get("password"));
        // 其他配置可以根据MyBatis主配置文件进行配置
        try {
            dds.init();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dds;
    }
}
