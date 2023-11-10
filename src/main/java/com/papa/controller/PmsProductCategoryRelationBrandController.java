package com.papa.controller;

import com.papa.common.api.CommonResult;
import com.papa.mbg.model.PmsBrand;
import com.papa.service.PmsProductCategoryRelationBrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@Api(tags = "PmsProductCategoryRelationBrandController",description = "商品种类与品牌关联")
@RequestMapping(value = "/productCategoryRelationBrand")
public class PmsProductCategoryRelationBrandController {

    @Resource
    private PmsProductCategoryRelationBrandService relationBrandService;

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("获取与该商品种类有关的品牌列表")
    public CommonResult<List<PmsBrand>> getBrandsByCategoryId(@PathVariable("id")Long categoryId){
        return CommonResult.success(relationBrandService.getBrandList(categoryId));
    }
}
