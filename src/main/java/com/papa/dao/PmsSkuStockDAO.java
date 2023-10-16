package com.papa.dao;

import com.papa.mbg.model.PmsSkuStock;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PmsSkuStockDAO {

    public int insertList(@Param("list") List<PmsSkuStock> skuStockList);

    public int replaceList(@Param("list") List<PmsSkuStock> skuStockList);
}
