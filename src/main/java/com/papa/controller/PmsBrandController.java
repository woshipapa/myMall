package com.papa.controller;

import com.papa.common.api.CommonPage;
import com.papa.common.api.CommonResult;
import com.papa.dto.PmsBrandParam;
import com.papa.mbg.CommentGenerator;
import com.papa.mbg.model.PmsBrand;
import com.papa.service.PmsBrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/brand")
@Api(description = "品牌管理",tags = "PmsBrandController")
public class PmsBrandController {

    @Resource
    private PmsBrandService pmsBrandService;

    @ApiOperation("获得所有品牌信息")
    @GetMapping("/listAll")
    @ResponseBody
    public CommonResult listAllBrand(){
        return CommonResult.success(pmsBrandService.listAllBrand());
    }


    @ApiOperation("获取指定id集合中的品牌信息")
    @PostMapping(value = "/list/ids")
    @ResponseBody
    public CommonResult listInIds(@RequestBody List<Long> ids){
        return CommonResult.success(pmsBrandService.listBrandListInIds(ids));
    }
    @ApiOperation("创建品牌")
    @PostMapping("/create")
    @ResponseBody
    @PreAuthorize("hasAuthority('pms:brand:create')")
    public CommonResult createBrand(@Validated @RequestBody PmsBrandParam brand){
        Integer affects=pmsBrandService.createBrand(brand);
        if(affects>0){
            return CommonResult.success(affects);
        }else{
            return CommonResult.failed("创建品牌失败!");
        }
    }

    @ApiOperation("删除品牌")
    @GetMapping("/delete/{brandId}")
    @ResponseBody
    public CommonResult deleteBrand(@PathVariable("brandId")Long id){
        Integer affects=pmsBrandService.deleteBrand(id);

        if(affects>0){
            return CommonResult.success(null);
        }else{
            return CommonResult.failed("删除失败");
        }
    }


    @ApiOperation("更新品牌")
    @PostMapping("/update/{brandId}")
    @ResponseBody
    public CommonResult updateBrand(@PathVariable("brandId")Long id,@Validated@RequestBody PmsBrandParam brand){
        Integer affects=pmsBrandService.updateBrand(id,brand);
        if(affects>0){
            return CommonResult.success(affects);
        }else{
            return CommonResult.failed("更新失败");
        }
    }
    @ApiOperation(value = "根据编号查询品牌信息")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<PmsBrand> getItem(@PathVariable("id") Long id) {
        return CommonResult.success(pmsBrandService.getBrand(id));
    }

    @ApiOperation("分页查询品牌")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult listBrand(@RequestParam(value = "keyword",required = false) String keyword,
            @RequestParam(value = "pageNum",defaultValue = "1")
                                  @ApiParam("页码") Integer pageNum,
                                  @RequestParam(value = "pageSize",defaultValue = "5")
                                  @ApiParam("页中记录数") Integer pageSize
                                  ){

        List<PmsBrand> pmsBrands=pmsBrandService.listBrand(keyword,pageNum,pageSize);
        return CommonResult.success(CommonPage.restPage(pmsBrands));
    }

    @ApiOperation(value = "批量更新显示状态")
    @RequestMapping(value = "/update/showStatus", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateShowStatus(@RequestParam("ids") List<Long> ids,
                                         @RequestParam("showStatus") Integer showStatus) {
        int count = pmsBrandService.updateShowStatus(ids, showStatus);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation(value = "批量更新厂家制造商状态")
    @RequestMapping(value = "/update/factoryStatus", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateFactoryStatus(@RequestParam("ids") List<Long> ids,
                                            @RequestParam("factoryStatus") Integer factoryStatus) {
        int count = pmsBrandService.updateFactoryStatus(ids, factoryStatus);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }


}

