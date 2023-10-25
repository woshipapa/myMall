package com.papa.controller;

import com.papa.common.api.CommonPage;
import com.papa.common.api.CommonResult;
import com.papa.mbg.model.UmsResource;
import com.papa.security.component.DynamicSecurityMetadataSource;
import com.papa.service.UmsResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Api(value = "UmsResourceController",description = "后台用户资源管理")
@RequestMapping("/resource")
public class UmsResourceController {

    @Resource
    private UmsResourceService resourceService;
    @Resource
    private DynamicSecurityMetadataSource metadataSource;

    @ApiOperation("创建资源")
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public CommonResult create(@RequestBody UmsResource resource){
        int count=resourceService.create(resource);
        metadataSource.clearDataSource();
        if(count>0){
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }
    }

    @ApiOperation("显示资源信息")
    @RequestMapping(value = "/{resourceId}",method = RequestMethod.GET)
    public CommonResult getItem(@PathVariable Long resourceId){
        return CommonResult.success(resourceService.getItem(resourceId));
    }

    @ApiOperation("分页查询资源")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public CommonResult list(@RequestParam(required = false)String nameKeyword,
                             @RequestParam(required = false)String urlKeyword,
                             @RequestParam(required = false)Long categoryId,
                             @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize,
                             @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum){

        return CommonResult.success(CommonPage.restPage(resourceService.list(nameKeyword, urlKeyword, categoryId, pageSize, pageNum)));
    }


    @ApiOperation("显示所有资源信息")
    @RequestMapping(value = "/listAll",method = RequestMethod.GET)
    public CommonResult listAll(){
        return CommonResult.success(resourceService.listAll());
    }

    @ApiOperation("编辑资源信息")
    @RequestMapping(value = "/update/{menuId}",method = RequestMethod.POST)
    public CommonResult update(@PathVariable Long menuId,@RequestBody UmsResource resource){
        int count= resourceService.update(menuId,resource);
        metadataSource.clearDataSource();
        if(count>0){
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }
    }


    @ApiOperation("根据id删除资源")
    @RequestMapping(value = "/delete/{menuId}",method = RequestMethod.POST)
    public CommonResult delete(@PathVariable Long menuId){
        int count= resourceService.delete(menuId);
        metadataSource.clearDataSource();
        if(count>0){
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }

    }




}
