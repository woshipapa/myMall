package com.papa.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
//@ConfigurationProperties("druiddatasource.statviewservlet")
public class DruidConfig {
    @Value("druiddatasource.statviewservlet.loginUserName")
    private String loginUserName;
    @Value("druiddatasource.statviewservlet.loginUserPassword")
    private String loginUserPassword;

    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSource druidDataSource() throws SQLException {
        DruidDataSource dataSource=new DruidDataSource();
        dataSource.setFilters("stat");
//        dataSource.setFilters("wall");
        return dataSource;
    }

    @Bean
    public ServletRegistrationBean<StatViewServlet> StatViewServlet(){
        ServletRegistrationBean<StatViewServlet> registrationBean=new ServletRegistrationBean<>(new StatViewServlet(),"/druid/*");
        registrationBean.addInitParameter("loginUserName",loginUserName);
        registrationBean.addInitParameter("loginUserPassword",loginUserPassword);
        return registrationBean;
    }
}
