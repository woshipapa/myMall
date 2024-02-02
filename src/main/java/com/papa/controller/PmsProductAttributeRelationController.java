package com.papa.controller;

import com.papa.common.api.CommonPage;
import com.papa.common.api.CommonResult;
import com.papa.mbg.model.PmsProductAttribute;
import com.papa.mbg.model.PmsProductAttributeCategoryRelation;
import com.papa.service.PmsProductAttributeRelationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@Api(tags = "PmsProductAttributeRelationController",description = "商品属性组关联的属性管理")
@RequestMapping("/productAttributeRelation")
public class PmsProductAttributeRelationController {

    @Resource
    private PmsProductAttributeRelationService relationService;

    @RequestMapping(value = "/list/{gid}",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取某个属性组下的相关规格属性",tags = "在查询某个属性组下的属性列表和关联属性时会走这里")
    public CommonResult list(@PathVariable("gid") Long id,
                             @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                             @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize,
                             @RequestParam(value = "type",defaultValue = "1")Integer type){
        List<PmsProductAttribute> attributeList = relationService.list(id,pageNum,pageSize,type);
        return CommonResult.success(CommonPage.restPage(attributeList));
    }

    @RequestMapping(value = "/deleteBatch",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("批量删除关联的基本规格属性")
    public CommonResult deleteBatch(@RequestBody List<PmsProductAttributeCategoryRelation> relation){
        int count = relationService.deleteBatch(relation);
        if(count > 0){
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }
    }

    @RequestMapping(value = "/allowRelation/{gid}",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("列出与这个属性组属于同一个商品分类下的属性，不包含已经关联的基本属性")
    public CommonResult allowRelation(@RequestParam Map<String,Object> map,
                                      @PathVariable("gid")Long id){
        return CommonResult.success(CommonPage.restPage(relationService.allowRelation(map,id)));
    }


    @RequestMapping(value = "/AddRelations",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("新增属性与属性组的关联")
    public CommonResult addRelations(@RequestBody List<PmsProductAttributeCategoryRelation> relations){
        int count = relationService.addRelation(relations);
        if(count > 0){
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }
    }

    /**
     * 在通过了第一个步骤step之后，因为选了某个商品分类，此时会来获取与商品分类有关的基本属性和销售属性
     * @param id
     * @return
     */
    @RequestMapping(value = "/productCategory/{categoryId}",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("获取到与该商品分类有关的基本属性")
    public CommonResult getAttrListByCategoryId(@PathVariable("categoryId") Long id){
        return CommonResult.success(relationService.getAttrListByCategoryId(id));
    }

    @RequestMapping(value = "/getSaleAttributes/{categoryId}",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("获取到与该商品分类有关的销售属性")
    public CommonResult getSaleAttributeByCategoryId(@PathVariable("categoryId") Long cid){
        return CommonResult.success(relationService.getSaleAttrByCategoryId(cid));
    }
}
