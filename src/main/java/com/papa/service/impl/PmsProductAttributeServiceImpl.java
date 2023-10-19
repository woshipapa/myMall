package com.papa.service.impl;

import com.github.pagehelper.PageHelper;
import com.papa.dao.PmsProductAttributeDAO;
import com.papa.dto.PmsProductAttributeCategoryItem;
import com.papa.mbg.mapper.PmsProductAttributeCategoryMapper;
import com.papa.mbg.mapper.PmsProductAttributeMapper;
import com.papa.mbg.model.PmsProductAttribute;
import com.papa.mbg.model.PmsProductAttributeCategory;
import com.papa.mbg.model.PmsProductAttributeExample;
import com.papa.service.PmsProductAttributeCategoryService;
import com.papa.service.PmsProductAttributeService;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;
import java.util.List;

public class PmsProductAttributeServiceImpl implements PmsProductAttributeService {
    @Resource
    private PmsProductAttributeMapper productAttributeMapper;

    @Resource
    private PmsProductAttributeCategoryMapper attributeCategoryMapper;

    @Resource
    private PmsProductAttributeDAO attributeDAO;

    @Override
    public List<PmsProductAttribute> list(Long attributeCategoryId, Integer type, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        PmsProductAttributeExample example = new PmsProductAttributeExample();
        example.setOrderByClause("sort desc");
        PmsProductAttributeExample.Criteria criteria = example.createCriteria();
        criteria.andProductAttributeCategoryIdEqualTo(attributeCategoryId);
        criteria.andTypeEqualTo(type);
        return productAttributeMapper.selectByExample(example);
    }
    @Override
    public int create(PmsProductAttribute productAttribute) {
        int count = productAttributeMapper.insertSelective(productAttribute);
        //更新与商品属性相关联的商品属性类型表,attribute_count,param_count
        PmsProductAttributeCategory attributeCategory = attributeCategoryMapper.selectByPrimaryKey(productAttribute.getProductAttributeCategoryId());
        int type = productAttribute.getType();
        if(type == 0){
            attributeCategory.setAttributeCount(attributeCategory.getAttributeCount()+1);
        }else{
            attributeCategory.setParamCount(attributeCategory.getParamCount()+1);
        }
        attributeCategoryMapper.updateByPrimaryKeySelective(attributeCategory);
        return count;

    }

    @Override
    public PmsProductAttribute getItem(Long id) {
        return productAttributeMapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(Long id, PmsProductAttribute productAttribute) {
        productAttribute.setId(id);
        return productAttributeMapper.updateByPrimaryKeySelective(productAttribute);
    }

    @Override
    public int delete(Long id) {
        PmsProductAttributeCategoryItem item = attributeDAO.getItem(id);
        int count = productAttributeMapper.deleteByPrimaryKey(id);
        int type = item.getAttributeList().get(0).getType();
        PmsProductAttributeCategory attributeCategory = new PmsProductAttributeCategory();
        BeanUtils.copyProperties(item,attributeCategory);
        if(type == 0){
            attributeCategory.setAttributeCount(attributeCategory.getAttributeCount()-1);
        }else{
            attributeCategory.setParamCount(attributeCategory.getParamCount()-1);
        }
        attributeCategoryMapper.updateByPrimaryKeySelective(attributeCategory);
        return count;
    }
}
