package com.papa.controller;

import com.papa.common.api.CommonPage;
import com.papa.common.api.CommonResult;
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
    private PmsProductAttributeService attributeService;
    @ApiOperation("分页获取所有属性信息")
    @RequestMapping(value = "/listAll",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult listAll(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                                @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize){
        return CommonResult.success(CommonPage.restPage(attributeService.listAll(pageNum,pageSize)));
    }
    @RequestMapping(value = "/list/{cid}",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("根据分类查询属性列表或者参数列表")
    public CommonResult list(@PathVariable Long cid,
                             @RequestParam("type") Integer type,
                             @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                             @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize){
        List<PmsProductAttribute> attributeList = attributeService.list(cid,type,pageNum,pageSize);
        return CommonResult.success(CommonPage.restPage(attributeList));
    }

    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("创建属性或者参数")
    public CommonResult create(@RequestBody @Validated PmsProductAttributeParam param){
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
    public CommonResult getItem(@PathVariable Long id){
        return CommonResult.success(attributeService.getItem(id));
    }


    @RequestMapping(value = "/update/{id}",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("修改属性或者参数")
    public CommonResult update(@PathVariable Long id,
                               @RequestBody @Validated PmsProductAttributeParam param){
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
    public CommonResult delete(@RequestParam("ids") List<Long> ids){
        int count = attributeService.delete(ids);
        if(count > 0){
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }
    }
}
