package com.papa.service;

import com.papa.mbg.model.PmsProductAttributeCategory;

import java.util.List;

public interface PmsProductAttributeCategoryService {
    public int create(String name);

    public List<PmsProductAttributeCategory>  list(Integer pageNum,Integer pageSize);


    public int delete(Long id);

    public int update(Long id,String name);


    public List<PmsProductAttributeCategory> list();

    public PmsProductAttributeCategory getItem(Long id);

}
