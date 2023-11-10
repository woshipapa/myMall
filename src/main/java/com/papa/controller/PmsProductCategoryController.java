package com.papa.controller;

import com.papa.common.api.CommonPage;
import com.papa.common.api.CommonResult;
import com.papa.dto.PmsProductCategoryParam;
import com.papa.dto.PmsProductCategoryWithChildrenItem;
import com.papa.mbg.model.PmsProductCategory;
import com.papa.service.PmsProductCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@Api(tags = "PmsProductCategoryController",description = "商品分类管理")
@RequestMapping("/productCategory")
public class PmsProductCategoryController {

    @Resource
    private PmsProductCategoryService productCategoryService;

    @ApiOperation("添加商品分类")
    @ResponseBody
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public CommonResult create(@Validated @RequestBody PmsProductCategoryParam param){
        int count = productCategoryService.create(param);
        if(count > 0){
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }
    }

    @ApiOperation("分页获取商品分类")
    @RequestMapping(value = "/list/{parentId}",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult list(@PathVariable Long parentId,
            @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,
                             @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize)
    {
        List<PmsProductCategory> categories = productCategoryService.list(parentId,pageNum,pageSize);
        return CommonResult.success(CommonPage.restPage(categories));
    }

    @ApiOperation("根据商品分类id回显信息")
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getItem(@PathVariable Long id){
        return CommonResult.success(productCategoryService.getItem(id));
    }


    @ApiOperation("修改商品分类信息")
    @RequestMapping(value = "/update/{id}",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id,
                               @Validated@RequestBody PmsProductCategoryParam param){
        int count = productCategoryService.update(id,param);
        if(count > 0){
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }
    }

    @ApiOperation("删除商品分类")
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@PathVariable Long id){
        int count = productCategoryService.delete(id);
        if(count > 0){
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }
    }
    @ApiOperation("修改导航栏显示状态")
    @RequestMapping(value = "/update/navStatus", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateNavStatus(@RequestParam("ids") List<Long> ids, @RequestParam("navStatus") Integer navStatus) {
        int count = productCategoryService.updateNavStatus(ids, navStatus);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("修改显示状态")
    @RequestMapping(value = "/update/showStatus", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateShowStatus(@RequestParam("ids") List<Long> ids, @RequestParam("showStatus") Integer showStatus) {
        int count = productCategoryService.updateShowStatus(ids, showStatus);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("获得树形分类")
    @RequestMapping(value = "/list/withChildren",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult listWithChildren(){
        List<PmsProductCategoryWithChildrenItem> childrenItemList = productCategoryService.listWithChildren();
        return CommonResult.success(childrenItemList);
    }
    @RequestMapping(value = "/isParent/{pid}",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("判断该目录是否是其他分类的父分类")
    public CommonResult isParent(@PathVariable("pid")Long id){
        return CommonResult.success(productCategoryService.isParent(id));
    }
}
