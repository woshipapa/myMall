package com.papa.config;



import com.papa.mbg.model.UmsResource;
import com.papa.security.component.DynamicSecurityService;
import com.papa.service.UmsAdminService;
import com.papa.service.UmsResourceService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//@EnableWebSecurity
public class MallSecurityConfig {

    @Resource
    private UmsAdminService umsAdminService;

//    @Resource
//    private RestfulAccessDeniedHandler accessDeniedHandler;
//    @Resource
//    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

//    @Resource
//    private jwtAuthenticationTokenFilter JwtAuthenticationTokenFilter;
//
//    @Resource
//    private PasswordEncoder encoder;
//    @Resource
//    private UmsAdminRoleRelationDAO adminRoleRelationDAO;
    /**
     * 注入密码加密器
     *
     */
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }

//    @Bean
//    public UserDetailsService userDetailsService(){
//        return username ->{
//            UmsAdmin admin = umsAdminService.getAdminByName(username);
//            if(admin!=null){
////                List<UmsPermission> permissions=umsAdminService.getPermissonsByAdminId(admin.getId());
////                return new AdminUserDetails(admin,permissions);
//                List<UmsResource> resourceList=adminRoleRelationDAO.getResourcesByAdmin(admin.getId());
//                UserDetails userDetails=new AdminUserDetails(admin,resourceList);
//                return userDetails;
//            }
//            return null;
//        };
//    }
    @Bean
    public UserDetailsService userDetailsService(){
        return userName -> umsAdminService.loadUserDetailsByUserName(userName);
    }

    //显式配置
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }

    @Resource
    private UmsResourceService resourceService;

    @Bean
    public DynamicSecurityService dynamicSecurityService(){
        return new DynamicSecurityService() {
            @Override
            public Map<String, ConfigAttribute> loadDataSource() {
                Map<String,ConfigAttribute> map = new ConcurrentHashMap<>();
                List<UmsResource> resourceList = resourceService.listAll();
                for(UmsResource resource:resourceList){
                    map.put(resource.getUrl(),new org.springframework.security.access.SecurityConfig(resource.getId()+":"+resource.getName()));
                }
                return map;

            }
        };

    }
}
//class AdminDetailsService implements UserDetailsService{
//
//    @Override
//    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
//
//    }
//}