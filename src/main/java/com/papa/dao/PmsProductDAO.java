package com.papa.dao;

import com.papa.dto.PmsProductResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PmsProductDAO {

    public PmsProductResult getUpdateInfo(@Param("id") Long id);


    public List<Long> getCatogeryPath(@Param("id") Long productId);
}
