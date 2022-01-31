package com.bsu.truthtables.datasource;

import com.bsu.truthtables.dao.Dao;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.beans.PropertyVetoException;

@Configuration
public class DataSource {

    private final Log logger = LogFactory.getLog(this.getClass());

    @Value("${database.driverClass}")
    private String driverClass;

    @Value("${database.url}")
    private String url;

    @Value("${database.username}")
    private String username;

    @Value("${database.password}")
    private String password;


    @Bean(name = "myDataSource")
    public ComboPooledDataSource cmdDataSource() {
        return comboPooledDataSource(driverClass, url, username, password);
    }

    @Bean(name = "mySqlSessionFactoryBean")
    public SqlSessionFactoryBean gkSqlSessionFactoryBean() {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setMapperLocations(new ClassPathResource("/sql/queries.xml"));
        factoryBean.setDataSource(cmdDataSource());

        return factoryBean;
    }

    @Bean
    public Dao dao() throws Exception {
        Dao dao = new Dao();
        dao.setSqlSessionFactory(gkSqlSessionFactoryBean().getObject());
        return dao;
    }

    private ComboPooledDataSource comboPooledDataSource(String driverClass, String url,
                                                        String username, String password) {

        ComboPooledDataSource dataSource = new ComboPooledDataSource();

        try {
            dataSource.setDriverClass(driverClass);
        } catch (PropertyVetoException pve) {
            logger.error("Cannot load datasource driver (" + driverClass + ") : " + pve.getMessage());
            return null;
        }
        dataSource.setJdbcUrl(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        dataSource.setMinPoolSize(3);
        dataSource.setMaxPoolSize(5);
        dataSource.setMaxIdleTime(600);
        return dataSource;
    }
}

