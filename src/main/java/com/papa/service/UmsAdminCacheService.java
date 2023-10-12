package com.papa.service;


import com.papa.mbg.model.UmsAdmin;
import com.papa.mbg.model.UmsResource;

import java.util.List;

public interface UmsAdminCacheService {

    public void delResourceListByResource(Long resourceId);


    public void delResourceListByRole(Long roleId);

    public void delResourceListByRoles(List<Long> roleIds);


    public void delResourceListByAdmin(Long adminId);

    public void setResourceList(Long adminId,List<UmsResource> resources);

    public List<UmsResource> getResourceList(Long adminId);

    public void setAdmin(String username,UmsAdmin umsAdmin);

    public UmsAdmin getAdmin(String username);
}
