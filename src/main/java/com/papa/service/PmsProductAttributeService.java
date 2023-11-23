package com.papa.service;

import com.papa.dto.PmsProductAttributeParam;
import com.papa.dto.ProductAttrInfo;
import com.papa.mbg.model.PmsProductAttribute;
import com.papa.vo.PmsProductAttributeVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PmsProductAttributeService {
    @Transactional
    public List<PmsProductAttribute> list(Integer type,Integer pageNum,Integer pageSize);
    @Transactional
    public List<PmsProductAttribute> list(Long attributeCategoryId,Integer type,Integer pageNum,Integer pageSize);
    @Transactional
    public int create(PmsProductAttributeParam productAttribute);


    public PmsProductAttributeVO getItem(Long id);

    @Transactional
    public int update(Long id,PmsProductAttributeParam productAttributeParam);

    @Transactional
    public int delete(List<Long> id);
    List<PmsProductAttribute> listAll(Integer pageNum,Integer pageSize);

    List<ProductAttrInfo> getProductAttrInfo(Long id,Integer type);
}
