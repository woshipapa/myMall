package com.papa.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.github.pagehelper.PageHelper;
import com.papa.common.api.CommonPage;
import com.papa.common.api.CommonResult;
import com.papa.dao.PmsBrandDAO;
import com.papa.mbg.mapper.PmsBrandMapper;
import com.papa.mbg.mapper.PmsProductCategoryBrandRelationMapper;
import com.papa.mbg.model.PmsBrand;
import com.papa.mbg.model.PmsBrandExample;
import com.papa.mbg.model.PmsProductCategoryBrandRelation;
import com.papa.mbg.model.PmsProductCategoryBrandRelationExample;
import com.papa.service.PmsProductCategoryBrandRelationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PmsProductCategoryBrandRelationServiceImpl implements PmsProductCategoryBrandRelationService {

    @Resource
    private PmsProductCategoryBrandRelationMapper categoryBrandRelationMapper;

    @Resource
    private PmsBrandMapper brandMapper;
    @Override
    public List<PmsBrand> getBrandList(Long id, Integer pageNum, Integer pageSize) {
        PmsProductCategoryBrandRelationExample relationExample = new PmsProductCategoryBrandRelationExample();
        relationExample.createCriteria().andCategoryIdEqualTo(id);
        List<PmsProductCategoryBrandRelation> pmsProductCategoryBrandRelations = categoryBrandRelationMapper.selectByExample(relationExample);
        List<PmsBrand> brands = new ArrayList<>();
        PageHelper.startPage(pageNum,pageSize);
        if(CollUtil.isNotEmpty(pmsProductCategoryBrandRelations)){
            List<Long> ids = pmsProductCategoryBrandRelations.stream().map(it->{
                Long brandId = it.getBrandId();
                return brandId;
            }).collect(Collectors.toList());
            PmsBrandExample brandExample = new PmsBrandExample();
            brandExample.createCriteria().andIdIn(ids);
            brands = brandMapper.selectByExample(brandExample);
        }
        return brands;
    }

    @Resource
    private PmsBrandDAO brandDAO;
    @Override
    public List<PmsBrand> allowBrand(Long id, String key, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return brandDAO.allowBrand(id,key);
    }


    @Override
    public int addRelation(List<PmsProductCategoryBrandRelation> relations) {
        int count = 0;
        if(CollUtil.isNotEmpty(relations)){
            for(PmsProductCategoryBrandRelation relation : relations){
                categoryBrandRelationMapper.insertSelective(relation);
                count++;
            }
        }
        return count;
    }

    public int deleteRelation(List<PmsProductCategoryBrandRelation> relations){
        int count = 0;
        if(CollUtil.isNotEmpty(relations)){
            for(PmsProductCategoryBrandRelation relation : relations){
                PmsProductCategoryBrandRelationExample relationExample = new PmsProductCategoryBrandRelationExample();
                relationExample.createCriteria().andCategoryIdEqualTo(relation.getCategoryId()).andBrandIdEqualTo(relation.getBrandId());
                categoryBrandRelationMapper.deleteByExample(relationExample);
                count++;
            }
        }
        return count;
    }
}
