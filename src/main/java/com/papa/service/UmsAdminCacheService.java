package com.papa.service;


import java.util.List;

public interface UmsAdminCacheService {

    public void delResourceListByResource(Long resourceId);


    public void delResourceListByRole(Long roleId);

    public void delResourceListByRoles(List<Long> roleIds);


}
