package com.papa.controller;

import com.papa.common.api.CommonPage;
import com.papa.common.api.CommonResult;
import com.papa.mbg.model.PmsProductCategoryBrandRelation;
import com.papa.service.PmsProductCategoryBrandRelationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/categoryBrandRelation")
public class PmsProductCategoryBrandRelationController {
    @Resource
    private PmsProductCategoryBrandRelationService categoryBrandRelationService;


    @RequestMapping(value = "/getRelationBrand/{id}",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("根据商品分类id获取他所关联的品牌")
    public CommonResult getBrandList(@PathVariable Long id,
                                     @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,
                                     @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize){
        return CommonResult.success(CommonPage.restPage(categoryBrandRelationService.getBrandList(id,pageNum,pageSize)));
    }

    @RequestMapping("/allowBrandRelation/{id}")
    @ResponseBody
    @ApiOperation("获取可以关联的品牌列表，去除关联的品牌")
    public CommonResult allowRelation(@PathVariable Long id,
                                      @RequestParam(value = "key",required = false)String key,
                                      @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,
                                      @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize){
        return CommonResult.success(CommonPage.restPage(categoryBrandRelationService.allowBrand(id, key, pageNum, pageSize)));
    }

    @RequestMapping(value = "/addRelation",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("新增商品分类与品牌的关联")
    public CommonResult addRelation(@RequestBody List<PmsProductCategoryBrandRelation> relations){
        int count = categoryBrandRelationService.addRelation(relations);
        if(count > 0){
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }
    }

    @RequestMapping(value = "/deleteRelation",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("删除与该商品分类关联的品牌的关联关系")
    public CommonResult deleteRelation(@RequestBody List<PmsProductCategoryBrandRelation> relations){
        int count = categoryBrandRelationService.deleteRelation(relations);
        if(count > 0){
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }
    }
}