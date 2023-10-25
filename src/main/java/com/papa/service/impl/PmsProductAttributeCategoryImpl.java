package com.papa.service.impl;

import com.github.pagehelper.PageHelper;
import com.papa.mbg.mapper.PmsProductAttributeCategoryMapper;
import com.papa.mbg.model.PmsProductAttributeCategory;
import com.papa.mbg.model.PmsProductAttributeCategoryExample;
import com.papa.service.PmsProductAttributeCategoryService;

import javax.annotation.Resource;
import java.util.List;

public class PmsProductAttributeCategoryImpl implements PmsProductAttributeCategoryService {

    @Resource
    private PmsProductAttributeCategoryMapper attributeCategoryMapper;

    @Override
    public int create(String name) {
        PmsProductAttributeCategory category = new PmsProductAttributeCategory();
        category.setName(name);
        int count = attributeCategoryMapper.insertSelective(category);
        return count;
    }

    @Override
    public List<PmsProductAttributeCategory> list(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return attributeCategoryMapper.selectByExample(new PmsProductAttributeCategoryExample());
    }

    @Override
    public int delete(Long id) {
        return attributeCategoryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int update(Long id, String name) {
        PmsProductAttributeCategory attributeCategory = new PmsProductAttributeCategory();
        attributeCategory.setId(id);
        attributeCategory.setName(name);
        return attributeCategoryMapper.updateByPrimaryKeySelective(attributeCategory);

    }

    @Override
    public List<PmsProductAttributeCategory> list() {
        return attributeCategoryMapper.selectByExample(new PmsProductAttributeCategoryExample());
    }

    @Override
    public PmsProductAttributeCategory getItem(Long id) {
        return attributeCategoryMapper.selectByPrimaryKey(id);
    }
}
