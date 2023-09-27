package com.papa;

import com.papa.common.utils.JwtTokenUtil;
import com.papa.dao.UserAdminPermissionDAO;
import com.papa.service.RedisService;
import com.papa.service.UmsAdminService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

import javax.annotation.Resource;
import java.util.Date;

@SpringBootTest
public class testDAO {
    @Resource
    private ConfigurableApplicationContext ioc;

    @Test
    public void test1(){
        UserAdminPermissionDAO bean = ioc.getBean(UserAdminPermissionDAO.class);
        System.out.println(bean);

    }

    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Test
    public void test2(){
        String token="eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ4ZHBhcGEiLCJjcmVhdGVkIjoxNjk0NTE1NjAzNTU4LCJleHAiOjE2OTUxMjA0MDN9.Pvtrij-EPH_lq1aPIGDIG39fot_Ca-xY9UZQQWvydeWNVfzcq9SyCgRc8JmyxN6VS7VcxDZt3OxpCaAX5KZJsg";
         Date date=jwtTokenUtil.getExpireByToken(token);
        System.out.println(date);
    }

    @Resource
    private RedisService redisService;
    @Test
    public void t3(){
        redisService.set("username","2023914```");
        Object username = redisService.get("username");
        System.out.println(username);
    }

    @Resource
    private UmsAdminService adminService;
    @Test
    public void t4(){
        adminService.getMenusByAdmin(1L).forEach(item-> System.out.println(item));
    }
}
