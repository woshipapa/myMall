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
import io.swagger.models.auth.In;
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
    @PreAuthorize(value="hasAuthority('pms:brand:read')")
    public CommonResult listAllBrand(){
        return CommonResult.success(pmsBrandService.listAllBrand());
    }

    @ApiOperation("创建品牌")
    @PostMapping("/create")
    @ResponseBody
    @PreAuthorize("hasAuthority('pms:brand:create')")
    public CommonResult createBrand(@Validated @RequestBody PmsBrandParam brand){
        Integer affects=pmsBrandService.createBrand(brand);
        if(affects>0){
            return CommonResult.success(brand);
        }else{
            return CommonResult.failed("创建品牌失败!");
        }
    }

    @ApiOperation("删除品牌")
    @GetMapping("/delete/{brandId}")
    @ResponseBody
    @PreAuthorize("hasAuthority('pms:brand:delete')")
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
    @PreAuthorize("hasAuthority('pms:brand:update')")
    public CommonResult updateBrand(@PathVariable("brandId")Long id,@Validated@RequestBody PmsBrandParam brand){
        Integer affects=pmsBrandService.updateBrand(id,brand);
        if(affects>0){
            return CommonResult.success(null);
        }else{
            return CommonResult.failed("更新失败");
        }
    }


    @ApiOperation("分页查询品牌")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority('pms:brand:read')")
    public CommonResult listBrand(@RequestParam(value = "keyword",required = false) String keyword,
            @RequestParam(value = "pageNum",defaultValue = "1")
                                  @ApiParam("页码") Integer pageNum,
                                  @RequestParam(value = "pageSize",defaultValue = "5")
                                  @ApiParam("页中记录数") Integer pageSize
                                  ){

        List<PmsBrand> pmsBrands=pmsBrandService.listBrand(keyword,pageNum,pageSize);
        return CommonResult.success(CommonPage.restPage(pmsBrands));
    }
}
