package com.papa.service;

import com.papa.mbg.model.PmsBrand;
import com.papa.mbg.model.PmsProductCategoryBrandRelation;

import java.util.List;

public interface PmsProductCategoryBrandRelationService {

    public List<PmsBrand> getBrandList(Long id, Integer pageNum, Integer pageSize);


    public List<PmsBrand> allowBrand(Long id,String key,Integer pageNum,Integer pageSize);

    public int addRelation(List<PmsProductCategoryBrandRelation> relations);

    public int deleteRelation(List<PmsProductCategoryBrandRelation> relations);
}
