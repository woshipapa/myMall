package com.papa.service;

import cn.hutool.core.collection.CollUtil;
import com.github.pagehelper.PageHelper;
import com.papa.dto.ProductAttrInfo;
import com.papa.mbg.mapper.PmsProductAttributeCategoryMapper;
import com.papa.mbg.mapper.PmsProductAttributeCategoryRelationMapper;
import com.papa.mbg.mapper.PmsProductAttributeMapper;
import com.papa.mbg.mapper.PmsProductCategoryAttributeRelationMapper;
import com.papa.mbg.model.*;
import org.springframework.security.core.parameters.P;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

public abstract class PmsProductAttributeAbs implements PmsProductAttributeService{

    @Resource
    protected PmsProductAttributeCategoryRelationMapper relationMapper;

    @Resource
    protected PmsProductAttributeMapper productAttributeMapper;


    @Resource
    protected PmsProductAttributeCategoryMapper attributeCategoryMapper;
    @Override
    public List<PmsProductAttribute> listAll(Integer pageNum,Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return productAttributeMapper.selectByExample(new PmsProductAttributeExample());
    }

    @Override
    public List<PmsProductAttribute> list(Integer type, Integer pageNum, Integer pageSize) {
        return null;
    }

    @Override
    public List<PmsProductAttribute> list(Long attributeCategoryId, Integer type, Integer pageNum, Integer pageSize) {
        return null;
    }

    @Override
    public List<ProductAttrInfo> getProductAttrInfo(Long id,Integer type) {
        //根据商品分类id获得它的筛选属性关联
        PmsProductAttributeExample attributeExample = new PmsProductAttributeExample();
        attributeExample.createCriteria().andProductCategoryIdEqualTo(id).andTypeEqualTo(type);
        List<PmsProductAttribute> attributes = productAttributeMapper.selectByExample(attributeExample);
        List<ProductAttrInfo> attrInfos = attributes.stream().map(
                (item) ->{
                    PmsProductAttributeCategoryRelationExample relationExample = new PmsProductAttributeCategoryRelationExample();
                    relationExample.createCriteria().andAttributeIdEqualTo(item.getId());
                    List<PmsProductAttributeCategoryRelation> relations = relationMapper.selectByExample(relationExample);
                    ProductAttrInfo attrInfo = new ProductAttrInfo();
                    attrInfo.setAttributeId(item.getId());
                    //与该商品分类相关的基本属性可能还没有关联属性组，所以这里要判断
                    if(CollUtil.isNotEmpty(relations))
                        attrInfo.setAttributeCategoryId(relations.get(0).getAttributeCategoryId());
                    else attrInfo.setAttributeCategoryId(0L);
                    return attrInfo;
                }
        ).collect(Collectors.toList());
        return attrInfos;
    }
}
