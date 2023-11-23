package com.papa.controller;

import com.papa.common.api.CommonPage;
import com.papa.common.api.CommonResult;
import com.papa.dto.PmsProductParam;
import com.papa.dto.PmsProductQueryParam;
import com.papa.dto.advice.PmsProductParamDTO;
import com.papa.dto.advice.PmsSkuStockAttributeValueDTO;
import com.papa.dto.advice.PmsSkuStockDTO;
import com.papa.mbg.model.PmsProduct;
import com.papa.mbg.model.PmsSkuStockAttributeValue;
import com.papa.service.PmsProductService;
import com.papa.vo.PmsSkuStockVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Api(tags = "PmsProductController",description = "商品管理")
@RequestMapping("/product")
public class PmsProductController {

    @Resource
    private PmsProductService productService;


    @ApiOperation("创建商品")
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody PmsProductParamDTO paramDTO){
        PmsProductParam param = convertDTOtoParam(paramDTO);
        int count = productService.create(param);
        if(count > 0){
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }
    }
    private PmsProductParam convertDTOtoParam(PmsProductParamDTO dto){
        PmsProductParam param = new PmsProductParam();
        BeanUtils.copyProperties(dto,param);
        List<PmsSkuStockDTO> skuStockDTOList = dto.getSkuStockList();
        List<PmsSkuStockVO> skuStockVOS = skuStockDTOList.stream().map(
                it -> {
                    PmsSkuStockVO skuStockVO = new PmsSkuStockVO();
                    BeanUtils.copyProperties(it, skuStockVO);
                    List<PmsSkuStockAttributeValueDTO> attrs = it.getAttrs();
                    List<PmsSkuStockAttributeValue> attributeValues = attrs.stream().map(attr -> {
                        PmsSkuStockAttributeValue skuStockAttributeValue = new PmsSkuStockAttributeValue();
                        skuStockAttributeValue.setAttributeName(attr.getKey());//前端传来的key对应属性名
                        skuStockAttributeValue.setValue(attr.getValue());
                        skuStockAttributeValue.setAttributeId(attr.getAttributeId());
                        return skuStockAttributeValue;
                    }).collect(Collectors.toList());
                    skuStockVO.setAttrs(attributeValues);
                    return skuStockVO;
                }
        ).collect(Collectors.toList());
        param.setSkuStockList(skuStockVOS);
        return param;
    }
    @ApiOperation("根据商品id回显商品信息")
    @RequestMapping(value = "/updateInfo/{id}",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getUpdateInfo(@PathVariable("id") Long id){
        return CommonResult.success(productService.getUpdateInfos(id));
    }


    @ApiOperation("更新商品信息")
    @RequestMapping(value = "/update/{id}",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable("id")Long id,
                               @RequestBody PmsProductParamDTO paramDTO){
        PmsProductParam pmsProductParam = convertDTOtoParam(paramDTO);
        int count = productService.update(id,pmsProductParam);
        if(count > 0){
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }
    }

    @ApiOperation("查询商品")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<PmsProduct>> list(PmsProductQueryParam queryParam,
                             @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,
                             @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize){
        List<PmsProduct> products = productService.list(queryParam,pageNum,pageSize);
        return CommonResult.success(CommonPage.restPage(products));
    }

    @ApiOperation("批量修改审核状态")
    @RequestMapping(value = "/update/verifyStatus",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateVerifyStatus(@RequestParam("ids")List<Long> ids,
                                           @RequestParam("verifyStatus") Integer verifyStatus,
                                           @RequestParam("detail")String detail){
        int count = productService.updateVerifyStatus(ids,verifyStatus,detail);
        if(count > 0){
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }
    }



    @ApiOperation("批量上下架商品")
    @RequestMapping(value = "/update/publishStatus",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updatePublishStatus(@RequestParam("ids") List<Long> ids,
                                            @RequestParam("publishStatus")Integer publishStatus){
        int count = productService.updatePublishStatus(ids, publishStatus);
        if(count > 0){
            return CommonResult.success(count);
        }
        else{
            return CommonResult.failed();
        }
    }


    @ApiOperation("批量推荐商品")
    @RequestMapping(value = "/update/recommendStatus", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateRecommendStatus(@RequestParam("ids") List<Long> ids,
                                              @RequestParam("recommendStatus") Integer recommendStatus) {
        int count = productService.updateRecommendStatus(ids, recommendStatus);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("批量设为新品")
    @RequestMapping(value = "/update/newStatus", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateNewStatus(@RequestParam("ids") List<Long> ids,
                                        @RequestParam("newStatus") Integer newStatus) {
        int count = productService.updateNewStatus(ids, newStatus);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("批量修改删除状态")
    @RequestMapping(value = "/update/deleteStatus", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateDeleteStatus(@RequestParam("ids") List<Long> ids,
                                           @RequestParam("deleteStatus") Integer deleteStatus) {
        int count = productService.updateDeleteStatus(ids, deleteStatus);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

}
