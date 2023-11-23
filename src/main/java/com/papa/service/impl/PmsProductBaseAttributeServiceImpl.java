package com.papa.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.github.pagehelper.PageHelper;
import com.papa.dao.PmsProductAttributeCategoryDao;
import com.papa.dao.PmsProductAttributeDAO;
import com.papa.dto.PmsProductAttributeCategoryItem;
import com.papa.dto.PmsProductAttributeParam;
import com.papa.dto.ProductAttrInfo;
import com.papa.mbg.mapper.PmsProductCategoryAttributeRelationMapper;
import com.papa.mbg.model.*;
import com.papa.service.PmsProductAttributeAbs;
import com.papa.service.PmsProductAttributeRelationService;
import com.papa.vo.PmsProductAttributeVO;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.web.ConditionalOnEnabledResourceChain;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class PmsProductBaseAttributeServiceImpl extends PmsProductAttributeAbs {

    @Resource
    private PmsProductAttributeDAO attributeDAO;
    @Resource
    private PmsProductAttributeRelationService attributeRelationService;

    @Override
    public List<PmsProductAttribute> list(Long attributeCategoryId, Integer type, Integer pageNum, Integer pageSize) {
        return attributeRelationService.list(attributeCategoryId,pageNum,pageSize,type);
    }
    @Override
    public int create(PmsProductAttributeParam productAttributeParam) {
        PmsProductAttribute productAttribute = new PmsProductAttribute();
        BeanUtils.copyProperties(productAttributeParam,productAttribute);
        int count = productAttributeMapper.insertSelective(productAttribute);
        if(productAttributeParam.getCategoryId()!=null){
            //如果选择了属性组的分类，那么加入到关系表中去
            PmsProductAttributeCategoryRelation relation = new PmsProductAttributeCategoryRelation();
            relation.setAttributeId(productAttribute.getId());
            relation.setAttributeCategoryId(productAttributeParam.getCategoryId());
            relationMapper.insertSelective(relation);
            //新增商品属性以后需要更新商品属性分类数量
            PmsProductAttributeCategory pmsProductAttributeCategory = attributeCategoryMapper.selectByPrimaryKey(productAttributeParam.getCategoryId());
            pmsProductAttributeCategory.setParamCount(pmsProductAttributeCategory.getParamCount()+count);
            attributeCategoryMapper.updateByPrimaryKey(pmsProductAttributeCategory);
        }
        return count;

    }

    @Override
    public PmsProductAttributeVO getItem(Long id) {
        PmsProductAttribute attribute = productAttributeMapper.selectByPrimaryKey(id);
        PmsProductAttributeVO attributeVO = new PmsProductAttributeVO();//带有其所属的属性组id
        if(attribute!=null) {
            BeanUtils.copyProperties(attribute, attributeVO);
            //获取与该属性关联的属性组
            PmsProductAttributeCategoryRelationExample relationExample = new PmsProductAttributeCategoryRelationExample();
            relationExample.createCriteria().andAttributeIdEqualTo(id);
            List<PmsProductAttributeCategoryRelation> relations = relationMapper.selectByExample(relationExample);
            Long attrCategoryId = null;
            if (CollUtil.isNotEmpty(relations)) {
                attrCategoryId = relations.get(0).getAttributeCategoryId();
            }
            attributeVO.setProductAttributeCategoryId(attrCategoryId);
        }
        return attributeVO;
    }

    @Override
    public int update(Long id, PmsProductAttributeParam productAttributeParam) {
        PmsProductAttribute productAttribute = new PmsProductAttribute();
        BeanUtils.copyProperties(productAttributeParam,productAttribute);
        productAttribute.setId(id);
        PmsProductAttributeCategoryRelationExample relationExample = new PmsProductAttributeCategoryRelationExample();
        relationExample.createCriteria().andAttributeIdEqualTo(id);
        relationMapper.deleteByExample(relationExample);
        if(productAttributeParam.getCategoryId()!=null){
            PmsProductAttributeCategoryRelation newRelation = new PmsProductAttributeCategoryRelation();
            newRelation.setAttributeId(id);
            newRelation.setAttributeCategoryId(productAttributeParam.getCategoryId());
            relationMapper.insertSelective(newRelation);
        }
        return productAttributeMapper.updateByPrimaryKeySelective(productAttribute);
    }

    @Override
    public int delete(List<Long> ids) {
                PmsProductAttributeExample attributeExample = new PmsProductAttributeExample();
                attributeExample.createCriteria().andIdIn(ids);
                PmsProductAttributeCategoryRelationExample relationExample = new PmsProductAttributeCategoryRelationExample();
                relationExample.createCriteria().andAttributeIdIn(ids);
                relationMapper.deleteByExample(relationExample);//删除跟该基本属性建立的关联关系
                return productAttributeMapper.deleteByExample(attributeExample);
    }

//    @Override
//    public List<PmsProductAttribute> listAll(Integer pageNum,Integer pageSize) {
//        PageHelper.startPage(pageNum,pageSize);
//        return productAttributeMapper.selectByExample(new PmsProductAttributeExample());
//    }


}
