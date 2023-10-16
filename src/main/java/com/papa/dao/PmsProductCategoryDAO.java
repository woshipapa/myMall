package com.papa.dao;

import org.apache.ibatis.annotations.Param;

public interface PmsProductCategoryDAO {

    public int updateParent(@Param("id") Long id);
}
