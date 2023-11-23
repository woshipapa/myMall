package com.papa.design;

import com.papa.service.PmsProductAttributeService;
import com.papa.service.impl.PmsProductBaseAttributeServiceImpl;
import com.papa.service.impl.PmsProductSaleAttributeServiceImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class AttributeServiceFactory {
    @Resource
    private PmsProductBaseAttributeServiceImpl baseAttributeService;

    @Resource
    private PmsProductSaleAttributeServiceImpl saleAttributeService;

    public PmsProductAttributeService getAttributeService(Integer type){
        if(type == null || type == 1) return baseAttributeService;
        return saleAttributeService;
    }

}
