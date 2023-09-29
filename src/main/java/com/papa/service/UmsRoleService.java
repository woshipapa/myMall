package com.papa.service;

import com.papa.mbg.model.UmsMenu;
import com.papa.mbg.model.UmsResource;
import com.papa.mbg.model.UmsRole;

import java.util.List;

public interface UmsRoleService {
    public List<UmsRole> list(String keyword,Integer pageNum,Integer pageSize);

    public List<UmsRole> listAll();

    public int create(UmsRole role);

    public int update(Long id,UmsRole role);


    public int delete(Long id);

    public int allocMenus(Long id,List<Long> menuIds);

    public int allocResources(Long id,List<Long> resourceIds);

    /**
     * 回显菜单信息
     * @param roleId
     * @return
     */

    public List<UmsMenu> getMenus(Long roleId);

    public List<UmsResource> getResources(Long roleId);

}
