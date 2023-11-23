package com.papa.service.impl;

import com.github.pagehelper.PageHelper;
import com.papa.dao.PmsProductAttributeCategoryDao;
import com.papa.dto.PmsProductAttributeCategoryItem;
import com.papa.mbg.mapper.PmsProductAttributeCategoryMapper;
import com.papa.mbg.mapper.PmsProductAttributeCategoryRelationMapper;
import com.papa.mbg.model.PmsProductAttributeCategory;
import com.papa.mbg.model.PmsProductAttributeCategoryExample;
import com.papa.mbg.model.PmsProductAttributeCategoryRelation;
import com.papa.mbg.model.PmsProductAttributeCategoryRelationExample;
import com.papa.service.PmsProductAttributeCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
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

    @Resource
    private PmsProductAttributeCategoryRelationMapper relationMapper;

    @Override
    public int delete(Long id) {
        //除了删除属性组还要删除与其相关关联的属性那个表中的关联关系
        PmsProductAttributeCategoryRelationExample relationExample = new PmsProductAttributeCategoryRelationExample();
        relationExample.createCriteria().andAttributeCategoryIdEqualTo(id);
        relationMapper.deleteByExample(relationExample);
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


    @Resource
    private PmsProductAttributeCategoryDao attributeCategoryDao;
    @Override
    public List<PmsProductAttributeCategoryItem> getGroupsWithAttr() {
        return attributeCategoryDao.getListWithAttr();
    }
}
