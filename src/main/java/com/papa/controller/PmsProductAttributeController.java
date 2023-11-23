package com.papa.controller;

import com.papa.common.api.CommonPage;
import com.papa.common.api.CommonResult;
import com.papa.design.AttributeServiceFactory;
import com.papa.dto.PmsProductAttributeParam;
import com.papa.mbg.model.PmsProductAttribute;
import com.papa.service.PmsProductAttributeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@Api(tags = "PmsProductAttributeController",description = "商品属性管理")
@RequestMapping("/productAttribute")
public class PmsProductAttributeController {

    @Resource
    private AttributeServiceFactory attributeServiceFactory;
    @ApiOperation("分页获取所有属性信息")
    @RequestMapping(value = "/listAll",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult listAll(@RequestParam(value = "type",defaultValue = "1") Integer type,
                                @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                                @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize){
        PmsProductAttributeService attributeService = attributeServiceFactory.getAttributeService(type);
        return CommonResult.success(CommonPage.restPage(attributeService.listAll(pageNum,pageSize)));
    }
    @RequestMapping(value = "/list/{attributeCategoryId}",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("根据属性组分类查询其下基本属性")
    public CommonResult list(@PathVariable Long attributeCategoryId,
                             @RequestParam(value = "type",defaultValue = "1") Integer type,
                             @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                             @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize){
        PmsProductAttributeService attributeService = attributeServiceFactory.getAttributeService(type);
        List<PmsProductAttribute> attributeList = attributeService.list(attributeCategoryId,type,pageNum,pageSize);
        return CommonResult.success(CommonPage.restPage(attributeList));
    }


    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("获得销售属性列表")
    public CommonResult<List<PmsProductAttribute>> list(@RequestParam(value = "type",defaultValue = "0") Integer type,
                                                        @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                                                        @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize){
        PmsProductAttributeService attributeService = attributeServiceFactory.getAttributeService(type);
        return CommonResult.success(attributeService.list(type,pageNum,pageSize));
    }
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("创建基本属性或者销售属性")
    public CommonResult create(@RequestBody @Validated PmsProductAttributeParam param){
        PmsProductAttributeService attributeService = attributeServiceFactory.getAttributeService(param.getType());
        int count = attributeService.create(param);
        if(count > 0){
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("根据id回显")
    public CommonResult getItem(@PathVariable Long id,@RequestParam(value = "type",defaultValue = "1")Integer type){
        PmsProductAttributeService attributeService = attributeServiceFactory.getAttributeService(type);
        return CommonResult.success(attributeService.getItem(id));
    }


    @RequestMapping(value = "/update/{id}",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("修改属性或者参数")
    public CommonResult update(@PathVariable Long id,
                               @RequestBody @Validated PmsProductAttributeParam param){
        PmsProductAttributeService attributeService = attributeServiceFactory.getAttributeService(param.getType());
        int count = attributeService.update(id,param);
        if(count > 0){
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }
    }

    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("删除属性或者参数")
    public CommonResult delete(@RequestParam("ids") List<Long> ids,@RequestParam(value = "type",defaultValue = "1") Integer type){
        PmsProductAttributeService attributeService = attributeServiceFactory.getAttributeService(type);
        int count = attributeService.delete(ids);
        if(count > 0){
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }
    }


    @RequestMapping(value = "/attrInfo/{productCategoryId}",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("获得与该商品分类有关的属性组以及其下属性")
    public CommonResult getProductAttrInfoByProductCategoryId(@PathVariable("productCategoryId") Long id,@RequestParam(required = false,defaultValue = "1") Integer type){
        PmsProductAttributeService attributeService = attributeServiceFactory.getAttributeService(type);
        return CommonResult.success(attributeService.getProductAttrInfo(id,type));
    }
}
