package com.papa.service;

import com.papa.mbg.model.PmsBrand;

import java.util.List;

public interface PmsProductCategoryRelationBrandService {
    public List<PmsBrand> getBrandList(Long id);
}
