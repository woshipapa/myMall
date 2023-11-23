package com.papa.controller;

import com.papa.common.api.CommonResult;
import com.papa.mbg.model.PmsSkuStock;
import com.papa.service.PmsSkuStockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/sku")
@Api(tags = "PmsSkuStockController",description = "sku库存管理")
public class PmsSkuStockController {
    @Resource
    private PmsSkuStockService skuStockService;


    @RequestMapping(value = "/{pid}",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "根据商品id和sku编码模糊查询",tags = "在商品index界面点击一条具体的商品记录的sku库存信息时回显")
    public CommonResult getList(@PathVariable Long pid, @RequestParam(value = "keyword",required = false) String keyword){
        return CommonResult.success(skuStockService.getList(pid,keyword));
    }


    @RequestMapping(value = "/update/{pid}",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "批量更新sku库存信息",tags = "在sku的编辑页面进行修改后提交更新")
    public CommonResult update(@PathVariable Long pid, @RequestBody List<PmsSkuStock> skuStockList){
        int count = skuStockService.update(pid, skuStockList);
        if(count > 0){
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }
    }


}
