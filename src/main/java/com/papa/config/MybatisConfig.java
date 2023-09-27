package com.papa.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = {"com.papa.mbg.mapper","com.papa.dao"})
public class MybatisConfig {
}
