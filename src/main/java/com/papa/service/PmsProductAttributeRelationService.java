package com.papa.service;

import com.papa.mbg.model.PmsProductAttribute;
import com.papa.mbg.model.PmsProductAttributeCategoryRelation;
import com.papa.vo.AttGroupWithAttrsVO;

import java.util.List;
import java.util.Map;

/**
 * 属性组与属性关联Service
 */
public interface PmsProductAttributeRelationService {

    public List<PmsProductAttribute> list(Long cid, Integer pageNum, Integer pageSize,Integer type);

    public int deleteBatch(List<PmsProductAttributeCategoryRelation> relations);

    public List<PmsProductAttribute> allowRelation(Map<String,Object> map, Long id);

    public int addRelation(List<PmsProductAttributeCategoryRelation> relations);

    public List<AttGroupWithAttrsVO> getAttrListByCategoryId(Long categoryId);

    public List<PmsProductAttribute> getSaleAttrByCategoryId(Long categoryId);
}
