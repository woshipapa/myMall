package com.papa.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.papa.mbg.mapper.PmsBrandMapper;
import com.papa.mbg.mapper.PmsProductCategoryBrandRelationMapper;
import com.papa.mbg.model.PmsBrand;
import com.papa.mbg.model.PmsBrandExample;
import com.papa.mbg.model.PmsProductCategoryBrandRelation;
import com.papa.mbg.model.PmsProductCategoryBrandRelationExample;
import com.papa.service.PmsProductCategoryRelationBrandService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PmsProductCategoryRelationBrandServiceImpl implements PmsProductCategoryRelationBrandService {

    @Resource
    private PmsProductCategoryBrandRelationMapper relationMapper;

    @Resource
    private PmsBrandMapper brandMapper;
    @Override
    public List<PmsBrand> getBrandList(Long categoryId) {
        PmsProductCategoryBrandRelationExample relationExample = new PmsProductCategoryBrandRelationExample();
        relationExample.createCriteria().andCategoryIdEqualTo(categoryId);
        List<PmsProductCategoryBrandRelation> pmsProductCategoryBrandRelations = relationMapper.selectByExample(relationExample);
        if(CollUtil.isNotEmpty(pmsProductCategoryBrandRelations)){
            List<PmsBrand> brands = pmsProductCategoryBrandRelations.stream().map((item)->{
                PmsBrandExample brandExample = new PmsBrandExample();
                PmsBrand pmsBrand = brandMapper.selectByPrimaryKey(item.getBrandId());
                return pmsBrand;
            }).collect(Collectors.toList());
            return brands;
        }
        //这里逻辑是如果该商品分类下没有商品品牌
        return new ArrayList<>();
    }
}
