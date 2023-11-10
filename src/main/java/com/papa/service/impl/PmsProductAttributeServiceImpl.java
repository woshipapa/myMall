package com.papa.service.impl;

import com.github.pagehelper.PageHelper;
import com.papa.dao.PmsProductAttributeDAO;
import com.papa.dto.PmsProductAttributeCategoryItem;
import com.papa.dto.PmsProductAttributeParam;
import com.papa.mbg.mapper.PmsProductAttributeCategoryMapper;
import com.papa.mbg.mapper.PmsProductAttributeCategoryRelationMapper;
import com.papa.mbg.mapper.PmsProductAttributeMapper;
import com.papa.mbg.model.*;
import com.papa.service.PmsProductAttributeService;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

public class PmsProductAttributeServiceImpl implements PmsProductAttributeService {
    @Resource
    private PmsProductAttributeMapper productAttributeMapper;

    @Resource
    private PmsProductAttributeCategoryMapper attributeCategoryMapper;

    @Resource
    private PmsProductAttributeCategoryRelationMapper relationMapper;

    @Resource
    private PmsProductAttributeDAO attributeDAO;

    @Override
    public List<PmsProductAttribute> list(Long attributeCategoryId, Integer type, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        PmsProductAttributeCategoryRelationExample relationExample = new PmsProductAttributeCategoryRelationExample();
        relationExample.createCriteria().andAttributeCategoryIdEqualTo(attributeCategoryId);
        List<PmsProductAttributeCategoryRelation> relations = relationMapper.selectByExample(relationExample);
        List<Long> attributes = relations.stream().map(it -> it.getAttributeId()).collect(Collectors.toList());
        PmsProductAttributeExample attributeExample = new PmsProductAttributeExample();
        attributeExample.createCriteria().andIdIn(attributes).andTypeEqualTo(type);
        return productAttributeMapper.selectByExample(attributeExample);
    }
    @Override
    public int create(PmsProductAttributeParam productAttributeParam) {
        PmsProductAttribute productAttribute = new PmsProductAttribute();
        BeanUtils.copyProperties(productAttributeParam,productAttribute);
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
    public int update(Long id, PmsProductAttributeParam productAttributeParam) {
        PmsProductAttribute productAttribute = new PmsProductAttribute();
        BeanUtils.copyProperties(productAttributeParam,productAttribute);
        productAttribute.setId(id);
        return productAttributeMapper.updateByPrimaryKeySelective(productAttribute);
    }

    @Override
    public int delete(List<Long> ids) {
        PmsProductAttributeCategoryItem item = attributeDAO.getItem(ids.get(0));
        int count = 0;
        for(Long id:ids){
            productAttributeMapper.deleteByPrimaryKey(id);
            count++;
        }
        int type = item.getAttributeList().get(0).getType();
        PmsProductAttributeCategory attributeCategory = new PmsProductAttributeCategory();
        BeanUtils.copyProperties(item,attributeCategory);
        if(type == 0){
            attributeCategory.setAttributeCount(attributeCategory.getAttributeCount()-ids.size());
        }else{
            attributeCategory.setParamCount(attributeCategory.getParamCount()- ids.size());
        }
        attributeCategoryMapper.updateByPrimaryKeySelective(attributeCategory);
        return count;
    }

    @Override
    public List<PmsProductAttribute> listAll(Integer pageNum,Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return productAttributeMapper.selectByExample(new PmsProductAttributeExample());
    }
}
