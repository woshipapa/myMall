package com.papa.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.github.pagehelper.PageHelper;
import com.papa.dao.PmsProductCategoryDAO;
import com.papa.dto.PmsProductCategoryParam;
import com.papa.dto.PmsProductCategoryWithChildrenItem;
import com.papa.mbg.mapper.PmsProductAttributeMapper;
import com.papa.mbg.mapper.PmsProductCategoryAttributeRelationMapper;
import com.papa.mbg.mapper.PmsProductCategoryMapper;
import com.papa.mbg.mapper.PmsProductMapper;
import com.papa.mbg.model.*;
import com.papa.service.PmsProductCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class PmsProductCategoryServiceiImpl implements PmsProductCategoryService {

    @Resource
    private PmsProductCategoryMapper categoryMapper;

    @Override
    public List<PmsProductCategoryWithChildrenItem> listWithChildren() {
        PmsProductCategoryExample example = new PmsProductCategoryExample();
        List<PmsProductCategory> categories = categoryMapper.selectByExample(example);
        List<PmsProductCategoryWithChildrenItem> list = categories.stream().filter(it -> it.getParentId() == 0)
                .map(it -> convert(it, categories)).collect(Collectors.toList());
        return list;

    }

    private PmsProductCategoryWithChildrenItem convert(PmsProductCategory category, List<PmsProductCategory> categories){
        PmsProductCategoryWithChildrenItem pmsProductCategoryWithChildrenItem = new PmsProductCategoryWithChildrenItem();
        BeanUtils.copyProperties(category,pmsProductCategoryWithChildrenItem);
        List<PmsProductCategoryWithChildrenItem> childrenItemList = categories.stream().filter(it -> it.getParentId().equals(category.getId()))
                .map(it -> convert(it, categories)).collect(Collectors.toList());
        pmsProductCategoryWithChildrenItem.setChildrens(childrenItemList);
        return pmsProductCategoryWithChildrenItem;
    }
    @Override
    public List<PmsProductCategory> list(Long parentId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        PmsProductCategoryExample example = new PmsProductCategoryExample();
        example.createCriteria().andParentIdEqualTo(parentId);
        return categoryMapper.selectByExample(example);
    }

    @Override
    public PmsProductCategory getItem(Long id) {
        return categoryMapper.selectByPrimaryKey(id);
    }

    @Resource
    private PmsProductMapper productMapper;
    @Override
    public int update(Long categoryId, PmsProductCategoryParam categoryParam) {
            PmsProductCategory category = new PmsProductCategory();
            BeanUtils.copyProperties(categoryParam,category);
            setCategoryLevel(category);
            category.setId(categoryId);
            //修改与商品类型相关的商品中商品类型的名字
            PmsProductExample productExample = new PmsProductExample();
            productExample.createCriteria().andProductCategoryIdEqualTo(categoryId);
            PmsProduct product = new PmsProduct();
            product.setProductCategoryName(category.getName());
            productMapper.updateByExampleSelective(product,productExample);

            //修改与商品类型相关的筛选属性
            PmsProductCategoryAttributeRelationExample relationExample = new PmsProductCategoryAttributeRelationExample();
            relationExample.createCriteria().andProductCategoryIdEqualTo(categoryId);
            relationMapper.deleteByExample(relationExample);
            if(categoryParam.getProductAttributeIdList()!=null){
                insertRelationList(categoryId,categoryParam);
            }

        //获取到与该商品种类已经关联的基本属性attributes
        PmsProductAttributeExample attributeExample = new PmsProductAttributeExample();
        attributeExample.createCriteria().andTypeEqualTo(1).andProductCategoryIdEqualTo(categoryId);
        List<PmsProductAttribute> attributes = attributeMapper.selectByExample(attributeExample);
        List<Long> exists = attributes.stream().map(PmsProductAttribute::getId).collect(Collectors.toList());
        //获取到要更新的关联属性id列表
        List<Long> updates = categoryParam.getProductAttributeIdList();

        exists.retainAll(updates);//公共元素
        List<PmsProductAttribute> deleteAttrRelations = attributes.stream().filter(item -> !exists.contains(item)).collect(Collectors.toList());//此时exists中剩下剔除公共元素的，这里面的属性关联被删除了
        if(CollUtil.isNotEmpty(deleteAttrRelations)) {
            PmsProductAttributeExample deleteExample = new PmsProductAttributeExample();
            deleteExample.createCriteria().andIdIn(deleteAttrRelations.stream().map(PmsProductAttribute::getId).collect(Collectors.toList()));
            PmsProductAttribute nullAttribute = new PmsProductAttribute();
            nullAttribute.setProductCategoryId(0L);
            attributeMapper.updateByExampleSelective(nullAttribute, deleteExample);
        }

        //这里是要新增的关联
        updates.removeAll(exists);
        if(CollUtil.isNotEmpty(updates)) {
            //有新增的
            PmsProductAttributeExample addExample = new PmsProductAttributeExample();
            addExample.createCriteria().andIdIn(updates);
            PmsProductAttribute addAttribute = new PmsProductAttribute();
            addAttribute.setProductCategoryId(categoryId);
            attributeMapper.updateByExampleSelective(addAttribute, addExample);
        }
            return categoryMapper.updateByPrimaryKeySelective(category);
    }

    @Resource
    private PmsProductCategoryDAO categoryDAO;
    @Override
    public int delete(Long categoryId) {
        categoryDAO.updateParent(categoryId);
        //与该商品类型有关的属性也要删除
        PmsProductCategoryAttributeRelationExample example = new PmsProductCategoryAttributeRelationExample();
        example.createCriteria().andProductCategoryIdEqualTo(categoryId);
        relationMapper.deleteByExample(example);

        return categoryMapper.deleteByPrimaryKey(categoryId);
    }

    @Resource
    private PmsProductCategoryAttributeRelationMapper relationMapper;
    @Override
    public int create(PmsProductCategoryParam param) {
        PmsProductCategory category = new PmsProductCategory();
        BeanUtils.copyProperties(param,category);
        category.setId(null);
        category.setProductCount(0);
        setCategoryLevel(category);
        int count = categoryMapper.insertSelective(category);
        Long categoryId = category.getId();
        //创建分类与筛选属性的关联(category与attribute的关联表)
        insertRelationList(categoryId,param);
        //修改属性表中的基本属性type==1的他们所属的商品种类
        updateAttribute(categoryId,param);
        return count;
    }

    @Resource
    private PmsProductAttributeMapper attributeMapper;
    private void updateAttribute(Long categoryId,PmsProductCategoryParam param){
        List<Long> productAttributeIdList = param.getProductAttributeIdList();
        if(CollUtil.isNotEmpty(productAttributeIdList)){
            Iterator<Long> iterator = productAttributeIdList.iterator();
            while(iterator.hasNext()){
                Long attributeId = iterator.next();
                PmsProductAttribute attribute = new PmsProductAttribute();
                attribute.setId(attributeId);
                attribute.setProductCategoryId(categoryId);
                attributeMapper.updateByPrimaryKeySelective(attribute);
            }
        }
    }
    @Override
    public int updateNavStatus(List<Long> ids, Integer status) {
        PmsProductCategoryExample example = new PmsProductCategoryExample();
        example.createCriteria().andIdIn(ids);
        PmsProductCategory category = new PmsProductCategory();
        category.setNavStatus(status);
        return categoryMapper.updateByExampleSelective(category,example);
    }

    @Override
    public int updateShowStatus(List<Long> ids, Integer status) {
        PmsProductCategoryExample example = new PmsProductCategoryExample();
        example.createCriteria().andIdIn(ids);
        PmsProductCategory category =  new PmsProductCategory();
        category.setShowStatus(status);
        return categoryMapper.updateByExampleSelective(category,example);
    }

    private void insertRelationList(Long categoryId,PmsProductCategoryParam param){
        List<Long> attributeIds = param.getProductAttributeIdList();
        for(Long attributeId:attributeIds){
            PmsProductCategoryAttributeRelation relation = new PmsProductCategoryAttributeRelation();
            relation.setProductCategoryId(categoryId);
            relation.setProductAttributeId(attributeId);
            relationMapper.insertSelective(relation);
        }
    }
    private void setCategoryLevel(PmsProductCategory category){
            Integer level = 1;
            if(category.getParentId()!=0) {
                while (category.getParentId() != 0) {
                    level++;
                    PmsProductCategoryExample example = new PmsProductCategoryExample();
                    example.createCriteria().andParentIdEqualTo(category.getParentId());
                    category = categoryMapper.selectByExample(example).get(0);
                }
            }
            category.setLevel(level);
        }
    @Override
    public boolean isParent(Long id) {
        PmsProductCategoryExample example = new PmsProductCategoryExample();
        example.createCriteria().andParentIdEqualTo(id);
        List<PmsProductCategory> childrens =categoryMapper.selectByExample(example);
        if(CollUtil.isNotEmpty(childrens)) return true;
        return false;
    }
}

