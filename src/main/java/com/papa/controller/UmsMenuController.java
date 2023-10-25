package com.papa.controller;

import com.papa.common.api.CommonPage;
import com.papa.common.api.CommonResult;
import com.papa.dto.UmsMenuNode;
import com.papa.mbg.model.UmsMenu;
import com.papa.service.UmsMenuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/menu")
public class UmsMenuController {
    @Resource
    private UmsMenuService umsMenuService;

    @ApiOperation("树形结构展示菜单")
    @RequestMapping(value = "/treelist",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult treeList(){
        List<UmsMenuNode> list=umsMenuService.treeNodeList();
        return CommonResult.success(list);
    }




    @ApiOperation("根据菜单id展示菜单详情")
    @RequestMapping(value = "/{menuId}",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getItem(@PathVariable(value = "menuId")Long id){
        return CommonResult.success(umsMenuService.getItem(id));
    }



    @ApiOperation("展示菜单列表")
    @RequestMapping(value = "/list/{parentId}",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult list(@PathVariable Long parentId, @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,@RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize){
        List<UmsMenu> list = umsMenuService.list(parentId, pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(list));
    }




    @ApiOperation("增加菜单")
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody UmsMenu menu){
        int count=umsMenuService.create(menu);
        if(count>0){
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }
    }

    @ApiOperation("修改菜单")
    @RequestMapping(value = "/update/{menuId}",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long menuId,@RequestBody UmsMenu menu){
        int count=umsMenuService.update(menuId,menu);
        if(count>0) {
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }
    }

    @ApiOperation("删除菜单")
    @RequestMapping(value = "/delete/{menuId}",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@PathVariable Long menuId){
        int count= umsMenuService.delete(menuId);
        if(count>0){
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }
    }

    @ApiOperation("修改菜单显示状态")
    @RequestMapping(value = "/updateStatus/{menuId}",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateStatus(@PathVariable Long menuId,@RequestParam(value = "status")Integer status){
        UmsMenu menu=new UmsMenu();
        menu.setHidden(status);
        int count= umsMenuService.update(menuId,menu);
        if(count>0) return CommonResult.success(count);
        else return CommonResult.failed();

    }


}
