package com.papa.service;

import com.papa.mbg.model.UmsResource;

import java.util.List;

public interface UmsResourceService {

    public List<UmsResource> list(String resourceName, String resourceUrl, Long categoryId, Integer pageNum, Integer pageSize);

    public List<UmsResource> listAll();


    public int create(UmsResource umsResource);


    public UmsResource getItem(Long id);

    public int update(Long id,UmsResource resource);

    public int delete(Long id);

}
