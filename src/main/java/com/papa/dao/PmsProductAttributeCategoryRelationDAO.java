package com.papa.dao;

import com.papa.mbg.model.PmsProductAttribute;
import com.papa.mbg.model.PmsProductAttributeCategoryRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PmsProductAttributeCategoryRelationDAO {
    public int insertBatch(@Param("list") List<PmsProductAttribute> attributeList);

    public int deleteBatch(@Param("list") List<PmsProductAttributeCategoryRelation> relationList);


}
