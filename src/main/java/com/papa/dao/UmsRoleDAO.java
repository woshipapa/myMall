package com.papa.dao;

import com.papa.mbg.model.UmsMenu;
import com.papa.mbg.model.UmsResource;

import java.util.List;

public interface UmsRoleDAO {

    public List<UmsMenu> getMenus(Long roleId);

    public List<UmsResource> getResources(Long roleId);
}
