package com.papa.controller;

import com.papa.common.api.CommonPage;
import com.papa.common.api.CommonResult;
import com.papa.mbg.CommentGenerator;
import com.papa.nosql.es.document.EsProduct;
import com.papa.nosql.es.document.EsProductAttributeValue;
import com.papa.service.EsProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/esProduct")
@Api(tags = "EsProductController",description = "搜索商品管理")
public class EsProductController {
    @Resource
    public EsProductService esProductService;


    @RequestMapping(value = "/importAll",method = RequestMethod.POST)
    @ApiOperation("导入数据库所有商品信息到ES中去")
    @ResponseBody
    public CommonResult importAllList(){
        int count = esProductService.importAll();
        return CommonResult.success(count);
    }

    @RequestMapping(value="/delete/{id}",method = RequestMethod.GET)
    @ApiOperation("删除指定id的商品")
    @ResponseBody
    public CommonResult delete(@PathVariable Integer id){
        esProductService.delete(id);
        return CommonResult.success(null);
    }

    @RequestMapping(value="/delete/batch",method = RequestMethod.POST)
    @ApiOperation("批量删除商品")
    @ResponseBody
    public CommonResult delete(@RequestParam("ids")List<Integer> ids){
        esProductService.delete(ids);
        return CommonResult.success(null);
    }



    @ApiOperation("根据id创建商品")
    @RequestMapping(value = "/create/{id}",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult create(@PathVariable("id") Integer id){
        EsProduct esProduct=esProductService.create(id);
        if(esProduct!=null){
            return CommonResult.success(esProduct);
        }
        else{
            return CommonResult.failed("创建商品失败");
        }
    }

    @ApiOperation("简单搜索")
    @RequestMapping(value = "/search/simple",method=RequestMethod.GET)
    @ResponseBody
    public CommonResult search(@RequestParam(required = false) String keyword,
                               @RequestParam(required = false,defaultValue = "0") Integer pageNum,
                               @RequestParam(required = false,defaultValue ="5" )Integer pageSize){
        Page<EsProduct> esProductPage=esProductService.search(keyword, pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(esProductPage));
    }


}
