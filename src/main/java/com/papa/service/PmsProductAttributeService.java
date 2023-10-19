package com.papa.service;

import com.papa.mbg.model.PmsProductAttribute;

import java.util.List;

public interface PmsProductAttributeService {

    public List<PmsProductAttribute> list(Long attributeCategoryId,Integer type,Integer pageNum,Integer pageSize);

    public int create(PmsProductAttribute productAttribute);


    public PmsProductAttribute getItem(Long id);


    public int update(Long id,PmsProductAttribute productAttribute);

    public int delete(Long id);

}
