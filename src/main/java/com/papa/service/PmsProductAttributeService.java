package com.papa.service;

import com.papa.dto.PmsProductAttributeParam;
import com.papa.mbg.model.PmsProductAttribute;

import java.util.List;

public interface PmsProductAttributeService {

    public List<PmsProductAttribute> list(Long attributeCategoryId,Integer type,Integer pageNum,Integer pageSize);

    public int create(PmsProductAttributeParam productAttribute);


    public PmsProductAttribute getItem(Long id);


    public int update(Long id,PmsProductAttributeParam productAttributeParam);

    public int delete(List<Long> id);
    List<PmsProductAttribute> listAll(Integer pageNum,Integer pageSize);

}
