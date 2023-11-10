package com.papa.service;

import com.papa.dto.AdminParam;
import com.papa.dto.UmsAdminPasswordParam;
import com.papa.mbg.model.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;


public interface UmsAdminService {

    public UmsAdmin getAdminByName(String name);

    /**
     * 添加用户
     * @param admin
     * @return
     */
    public UmsAdmin register(AdminParam admin);

    public String login(String userName,String password);

    public List<UmsPermission> getPermissonsByAdminId(Long id);


    /**
     * 筛选搜索进行列出
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return
     */
    public List<UmsAdmin> list(String keyword,Integer pageNum,Integer pageSize);

    public UmsAdmin getItem(Long id);

    public int update(Long id,UmsAdmin admin);

    public int updatePassword(UmsAdminPasswordParam param);
    public int delete(Long id);

    public int allocRoles(Long adminID,List<Long> roleIds);

    /**
     * 回显用户的角色信息
     * @param adminId
     * @return
     */

    public List<UmsRole> getRoles(Long adminId);


    public List<UmsMenu> getMenusByAdmin(Long adminId);

    public List<UmsResource> getResourcesByAdmin(Long adminId);



    public UserDetails loadUserDetailsByUserName(String userName);
}
