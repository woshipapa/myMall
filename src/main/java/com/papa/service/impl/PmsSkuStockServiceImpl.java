package com.papa.service.impl;

import com.papa.dao.PmsSkuStockDAO;
import com.papa.mbg.mapper.PmsSkuStockMapper;
import com.papa.mbg.model.PmsSkuStock;
import com.papa.mbg.model.PmsSkuStockExample;
import com.papa.service.PmsSkuStockService;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

public class PmsSkuStockServiceImpl implements PmsSkuStockService {
    @Resource
    private PmsSkuStockMapper stockMapper;

    @Resource
    private PmsSkuStockDAO skuStockDAO;
    @Override
    public List<PmsSkuStock> getList(Long productId, String keyword) {
        PmsSkuStockExample example = new PmsSkuStockExample();
        PmsSkuStockExample.Criteria criteria = example.createCriteria();
        criteria.andProductIdEqualTo(productId);
        if(StringUtils.hasText(keyword)){
            criteria.andSkuCodeLike("%"+keyword+"%");
        }
        return stockMapper.selectByExample(example);
    }

    public int update(Long productId,List<PmsSkuStock> list){
        List<PmsSkuStock> collect = list.stream().filter(it -> it.getProductId().equals(productId)).collect(Collectors.toList());
        return skuStockDAO.replaceList(collect);
    }
}
