package com.papa.dao;

import com.papa.mbg.model.PmsBrand;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PmsBrandDAO {
    public List<PmsBrand> allowBrand(@Param("id") Long categoryId, @Param("key") String key);
}
