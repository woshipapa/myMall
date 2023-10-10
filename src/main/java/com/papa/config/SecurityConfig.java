package com.papa.config;

import com.papa.component.RestAuthenticationEntryPoint;
import com.papa.component.RestfulAccessDeniedHandler;

import com.papa.dao.UmsAdminRoleRelationDAO;
import com.papa.dto.AdminUserDetails;
import com.papa.mbg.model.UmsAdmin;
import com.papa.mbg.model.UmsPermission;
import com.papa.mbg.model.UmsResource;
import com.papa.service.UmsAdminService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;
import java.util.List;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private UmsAdminService umsAdminService;

    @Resource
    private RestfulAccessDeniedHandler accessDeniedHandler;
    @Resource
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

//    @Resource
//    private jwtAuthenticationTokenFilter JwtAuthenticationTokenFilter;
//
//    @Resource
//    private PasswordEncoder encoder;
    @Resource
    private UmsAdminRoleRelationDAO adminRoleRelationDAO;
    /**
     * 注入密码加密器
     * @return
     */
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }

    @Bean
    public UserDetailsService userDetailsService(){
        return username ->{
            UmsAdmin admin = umsAdminService.getAdminByName(username);
            if(admin!=null){
//                List<UmsPermission> permissions=umsAdminService.getPermissonsByAdminId(admin.getId());
//                return new AdminUserDetails(admin,permissions);
                List<UmsResource> resourceList=adminRoleRelationDAO.getResourcesByAdmin(admin.getId());
                UserDetails userDetails=new AdminUserDetails(admin,resourceList);
                return userDetails;
            }
            return null;
        };
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, // 允许对于网站静态资源的无授权访问
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/swagger-resources/**",
                        "/v2/api-docs/**",
                        "/**/swagger-ui"
                )
                .permitAll()
                .antMatchers("/admin/login", "/admin/register")// 对登录注册要允许匿名访问
                .permitAll()
                .antMatchers(HttpMethod.OPTIONS)//跨域请求会先进行一次options请求
                .permitAll()
                .antMatchers("/member/readHistory/**").
                permitAll()
                .antMatchers("/order/**").permitAll()
                .antMatchers("/menu/**").permitAll()
                .anyRequest()
                .authenticated().and()
                .exceptionHandling().
                accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(restAuthenticationEntryPoint);
        httpSecurity.headers().cacheControl();
        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);//插入到过滤器链中
    }
    //写入我们自定义的UserDetailsService和passwordEncoder
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());
    }

//    @Bean
//    public jwtAuthenticationTokenFilter jwtAuthenticationTokenFilter(){
//        return new jwtAuthenticationTokenFilter();
//    }

    //显式配置
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
