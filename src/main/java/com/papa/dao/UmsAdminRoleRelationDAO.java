package com.papa.dao;

import com.papa.mbg.model.UmsAdminRoleRelation;
import com.papa.mbg.model.UmsMenu;
import com.papa.mbg.model.UmsResource;
import com.papa.mbg.model.UmsRole;

import java.util.List;

public interface UmsAdminRoleRelationDAO {

    public int insertRoles(List<UmsAdminRoleRelation> relation);


    public void updateAdminCountInRole(List<Long> roleIds,String type);

    public List<UmsRole> getRoles(Long adminId);


    public List<UmsMenu> getMenusByAdmin(Long adminId);

    public List<UmsResource> getResourcesByAdmin(Long adminId);
}
