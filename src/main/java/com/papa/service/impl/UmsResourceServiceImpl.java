package com.papa.service.impl;

import com.github.pagehelper.PageHelper;
import com.papa.mbg.mapper.UmsResourceMapper;
import com.papa.mbg.model.UmsResource;
import com.papa.mbg.model.UmsResourceExample;
import com.papa.security.component.DynamicSecurityMetadataSource;
import com.papa.service.UmsResourceService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
@Service
public class UmsResourceServiceImpl implements UmsResourceService {
    @Resource
    private UmsResourceMapper resourceMapper;

    @Override
    public List<UmsResource> list(String resourceName, String resourceUrl, Long categoryId, Integer pageNum, Integer pageSize) {
        UmsResourceExample example=new UmsResourceExample();
        UmsResourceExample.Criteria criteria = example.createCriteria();
        if(categoryId!=null){
            criteria.andCategoryIdEqualTo(categoryId);
        }
        if(StringUtils.hasText(resourceName)){
            criteria.andNameLike("%"+resourceName+"%");
        }
        if(StringUtils.hasText(resourceUrl)){
            criteria.andUrlLike("%"+resourceUrl+"%");
        }
        PageHelper.startPage(pageNum,pageSize);
        return resourceMapper.selectByExample(example);
    }

    @Override
    public List<UmsResource> listAll() {
        UmsResourceExample example=new UmsResourceExample();
        return resourceMapper.selectByExample(example);
    }

    @Override
    public int create(UmsResource umsResource) {
        umsResource.setCreateTime(new Date());
        return resourceMapper.insertSelective(umsResource);
    }

    @Override
    public UmsResource getItem(Long id) {
        return resourceMapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(Long id, UmsResource resource)
    {
        resource.setId(id);
        return resourceMapper.updateByPrimaryKeySelective(resource);
    }

    @Override
    public int delete(Long id) {
        return resourceMapper.deleteByPrimaryKey(id);
    }
}
