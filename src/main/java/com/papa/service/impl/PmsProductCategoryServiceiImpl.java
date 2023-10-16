package com.papa.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.github.pagehelper.PageHelper;
import com.papa.dao.PmsProductCategoryDAO;
import com.papa.dto.PmsProductCategoryParam;
import com.papa.dto.PmsProductCategoryWithChildrenItem;
import com.papa.mbg.mapper.PmsProductCategoryAttributeRelationMapper;
import com.papa.mbg.mapper.PmsProductCategoryMapper;
import com.papa.mbg.mapper.PmsProductMapper;
import com.papa.mbg.model.*;
import com.papa.service.PmsProductCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

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

    private PmsProductCategoryWithChildrenItem convert(PmsProductCategory category,List<PmsProductCategory> categories){
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
        //创建分类与筛选属性的关联
        insertRelationList(categoryId,param);
        return count;
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
}

