package com.papa.service;

import com.papa.mbg.model.PmsSkuStock;

import java.util.List;

public interface PmsSkuStockService {

    public List<PmsSkuStock> getList(Long productId,String keyword);


}
