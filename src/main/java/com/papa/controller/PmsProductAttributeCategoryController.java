package com.papa.controller;

import com.papa.common.api.CommonPage;
import com.papa.common.api.CommonResult;
import com.papa.service.PmsProductAttributeCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@Api(tags = "PmsProductAttributeCategoryController",description = "商品属性类型管理")
@RequestMapping("/productAttributeCategory")
public class PmsProductAttributeCategoryController {
    @Resource
    private PmsProductAttributeCategoryService attributeCategoryService;

    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @ApiOperation("添加商品属性类型")
    @ResponseBody
    public CommonResult create(@RequestParam String name){
        int count = attributeCategoryService.create(name);
        if(count > 0){
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }
    }

    @RequestMapping(value = "/update/{id}",method = RequestMethod.POST)
    @ApiOperation("修改商品属性类型")
    @ResponseBody
    public CommonResult update(@PathVariable Long id,
                               @RequestParam String name){
        int count = attributeCategoryService.update(id,name);
        if(count > 0){
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }
    }


    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ApiOperation("分页获取商品属性类型")
    @ResponseBody
    public CommonResult list(@RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,
                             @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize){
        return CommonResult.success(CommonPage.restPage(attributeCategoryService.list(pageNum, pageSize)));
    }


    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    @ApiOperation("根据id回显信息")
    @ResponseBody
    public CommonResult getItem(@PathVariable Long id){
        return CommonResult.success(attributeCategoryService.getItem(id));
    }

    @RequestMapping(value = "/delete/{id}",method = RequestMethod.POST)
    @ApiOperation("删除指定商品属性类型")
    @ResponseBody
    public CommonResult delete(@PathVariable Long id){
        int count = attributeCategoryService.delete(id);
        if(count > 0){
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }
    }
}
