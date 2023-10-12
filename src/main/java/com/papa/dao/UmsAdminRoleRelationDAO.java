package com.papa.dao;

import com.papa.mbg.model.*;

import java.util.List;

public interface UmsAdminRoleRelationDAO {

    public int insertRoles(List<UmsAdminRoleRelation> relation);


    public void updateAdminCountInRole(List<Long> roleIds,String type);

    public List<UmsRole> getRoles(Long adminId);


    public List<UmsMenu> getMenusByAdmin(Long adminId);

    public List<UmsResource> getResourcesByAdmin(Long adminId);


    public List<Long> getAdminIdByResource(Long resourceId);
}
