package com.papa.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.github.pagehelper.PageHelper;
import com.papa.dao.PmsProductAttributeCategoryRelationDAO;
import com.papa.dao.PmsProductAttributeRelationDAO;
import com.papa.dao.PmsProductDAO;
import com.papa.mbg.mapper.PmsProductAttributeCategoryMapper;
import com.papa.mbg.mapper.PmsProductAttributeCategoryRelationMapper;
import com.papa.mbg.mapper.PmsProductAttributeMapper;
import com.papa.mbg.model.*;
import com.papa.service.PmsProductAttributeRelationService;
import com.papa.vo.AttGroupWithAttrsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PmsProductAttributeRelationServiceImpl implements PmsProductAttributeRelationService {

    @Resource
    private PmsProductAttributeCategoryRelationMapper relationMapper;

    @Resource
    private PmsProductAttributeMapper attributeMapper;
    @Resource
    private PmsProductAttributeCategoryMapper categoryMapper;

    @Resource
    private PmsProductAttributeCategoryRelationDAO relationDAO;

    @Override
    public List<PmsProductAttribute> list(Long cid, Integer pageNum, Integer pageSize,Integer type) {

        PmsProductAttributeCategoryRelationExample relationExample = new PmsProductAttributeCategoryRelationExample();
        relationExample.createCriteria().andAttributeCategoryIdEqualTo(cid);
        List<PmsProductAttributeCategoryRelation> relations = relationMapper.selectByExample(relationExample);
        List<Long> ids = relations.stream().map(item->item.getAttributeId()).collect(Collectors.toList());
        if(CollUtil.isEmpty(ids)){
            return new ArrayList<>();
        }
        PmsProductAttributeExample attributeExample = new PmsProductAttributeExample();
        //这里只显示规格这种基本属性 1
        attributeExample.createCriteria().andIdIn(ids).andTypeEqualTo(type);
        //分页的位置很重要，因为我只想在最后的查询中进行分页
        PageHelper.startPage(pageNum,pageSize);
        List<PmsProductAttribute> attributes =  attributeMapper.selectByExample(attributeExample);
        //在这里进行属性组拥有的基本属性的数量改变
        Integer attrCount = attributes.size();
        PmsProductAttributeCategory attributeCategory = new PmsProductAttributeCategory();
        attributeCategory.setAttributeCount(attrCount);
        attributeCategory.setId(cid);
        categoryMapper.updateByPrimaryKeySelective(attributeCategory);
        return attributes;
    }

    @Override
    public int deleteBatch(List<PmsProductAttributeCategoryRelation> relations) {
        return relationDAO.deleteBatch(relations);
    }

    @Override
    public List<PmsProductAttribute> allowRelation(Map<String, Object> map, Long id) {
        List<PmsProductAttribute> attributes = new ArrayList<>();
        //根据属性组id得到他所属的商品分类id
        PmsProductAttributeCategory pmsProductAttributeCategory = categoryMapper.selectByPrimaryKey(id);
        Long productCategoryId = pmsProductAttributeCategory.getProductCategoryId();
        if(productCategoryId!=null&&productCategoryId!=0){
            //选出与属性组属于同一商品分类下的商品基本属性
            PmsProductAttributeExample attributeExample = new PmsProductAttributeExample();
            PmsProductAttributeExample.Criteria criteria = attributeExample.createCriteria();
            criteria.andProductCategoryIdEqualTo(productCategoryId).andTypeEqualTo(1);
            //为了进行筛选，先获取到这个商品分类下所有商品属性组的id
            PmsProductAttributeCategoryExample categoryExample = new PmsProductAttributeCategoryExample();
            categoryExample.createCriteria().andProductCategoryIdEqualTo(productCategoryId);
            List<PmsProductAttributeCategory> categories = categoryMapper.selectByExample(categoryExample);
            List<Long> groupIds = categories.stream().map(item->item.getId()).collect(Collectors.toList());
            PmsProductAttributeCategoryRelationExample relationExample = new PmsProductAttributeCategoryRelationExample();
            relationExample.createCriteria().andAttributeCategoryIdIn(groupIds);
            List<PmsProductAttributeCategoryRelation> relations = relationMapper.selectByExample(relationExample);
            List<Long> collect = relations.stream().map(it -> it.getAttributeId()).collect(Collectors.toList());
            if(CollUtil.isNotEmpty(collect)){
                criteria.andIdNotIn(collect);
            }
            Object key = map.get("key");
            if(key!=null){
                criteria.andNameLike("%"+key+"%");
            }
            Integer pageNum = Integer.parseInt((String) map.get("pageNum"));
            Integer pageSize = Integer.parseInt((String)map.get("pageSize"));
            PageHelper.startPage(pageNum,pageSize);
            attributes = attributeMapper.selectByExample(attributeExample);
        }
        return attributes;
    }

    @Override
    public int addRelation(List<PmsProductAttributeCategoryRelation> relations) {
        int count = 0;
        for(PmsProductAttributeCategoryRelation relation:relations) {
            relationMapper.insertSelective(relation);
            count++;
        }
        return count;
    }


    @Override
    public List<AttGroupWithAttrsVO> getAttrListByCategoryId(Long categoryId) {
        //先找出与该商品种类相关的属性组
        PmsProductAttributeCategoryExample attributeCategoryExample = new PmsProductAttributeCategoryExample();
        List<AttGroupWithAttrsVO> list = new ArrayList<>();
        attributeCategoryExample.createCriteria().andProductCategoryIdEqualTo(categoryId);
        List<PmsProductAttributeCategory> attributeCategories = categoryMapper.selectByExample(attributeCategoryExample);
        if(CollUtil.isNotEmpty(attributeCategories)){
            for(PmsProductAttributeCategory category : attributeCategories){
                //遍历属性组，然后找到该属性组下的附属属性，封装成一个VO对象
                AttGroupWithAttrsVO item = new AttGroupWithAttrsVO();
                BeanUtils.copyProperties(category,item);
                PmsProductAttributeCategoryRelationExample relationExample = new PmsProductAttributeCategoryRelationExample();
                relationExample.createCriteria().andAttributeCategoryIdEqualTo(category.getId());
                List<PmsProductAttributeCategoryRelation> relations = relationMapper.selectByExample(relationExample);
                List<PmsProductAttribute> attrs = relations.stream().map((it) -> {
                    Long attrId = it.getAttributeId();
                    PmsProductAttribute attribute = attributeMapper.selectByPrimaryKey(attrId);
                    if(attribute.getType() == 0) return null;//在filter时将null排除在外，就是销售属性我不要这里
                    return attribute;
                }).filter(item2->item2!=null).collect(Collectors.toList());
                item.setAttributeList(attrs);
                list.add(item);
            }
        }
        return list;
    }

    @Resource
    private PmsProductAttributeRelationDAO attributeRelationDAO;
//    @Resource
//    private PmsProductDAO productDAO;
    /**
     * 获得该商品分类中的销售属性
     * @param categoryId
     * @return
     */
    @Override
    public List<PmsProductAttribute> getSaleAttrByCategoryId(Long categoryId) {
        PmsProductAttributeExample example = new PmsProductAttributeExample();
        PmsProductAttributeExample.Criteria criteria = example.createCriteria();
        attributeRelationDAO.setMaxRecursionDepth();
        List<Long> parentPath = attributeRelationDAO.getParentPath(categoryId);
//        List<Long> catogeryPath = productDAO.getCatogeryPath(categoryId);
        criteria.andProductCategoryIdIn(parentPath).andTypeEqualTo(0);

        return attributeMapper.selectByExample(example);
    }


}
