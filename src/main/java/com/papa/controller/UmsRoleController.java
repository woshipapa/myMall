package com.papa.controller;

import com.papa.common.api.CommonPage;
import com.papa.common.api.CommonResult;
import com.papa.mbg.CommentGenerator;
import com.papa.mbg.model.UmsRole;
import com.papa.service.UmsRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping(value = "/role")
public class UmsRoleController {

    @Resource
    public UmsRoleService roleService;


    @ApiOperation("创建角色")
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody UmsRole role){
        int count = roleService.create(role);
        if(count>0){
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }
    }



    @ApiOperation("删除角色")
    @RequestMapping(value = "/delete/{roleId}",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@PathVariable("roleId") Long id){
        int count = roleService.delete(id);
        if(count>0){
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }
    }


    @ApiOperation("更新角色信息")
    @RequestMapping(value = "/update/{roleId}",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable("roleId") Long id,@RequestBody UmsRole role){
        int count = roleService.update(id,role);
        if(count>0) return CommonResult.success(count);
        else return CommonResult.failed();
    }


    @ApiOperation("更新角色启用状态")
    @RequestMapping(value = "/updateStatus/{roleId}",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateStatus(@PathVariable("roleId") Long id,@RequestParam("status") Integer status){
        UmsRole umsRole=new UmsRole();
        umsRole.setStatus(status);
        int count = roleService.update(id,umsRole);
        if(count>0) return CommonResult.success(count);
        else return CommonResult.failed();
    }


    @ApiOperation("获取所有角色")
    @RequestMapping(value = "/listAll",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult listAll(){
        return CommonResult.success(roleService.listAll());
    }

    @ApiOperation("根据角色名称分页获取角色信息")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult list(@RequestParam(required = false,value = "keyword") String keyword,
                             @RequestParam(value = "pageNum") Integer pageNum,
                             @RequestParam(value = "pageSize")Integer pageSize){
        return CommonResult.success(CommonPage.restPage(roleService.list(keyword,pageNum,pageSize)));
    }



    @ApiOperation("根据角色id获得到角色分配的菜单")
    @RequestMapping(value = "/listMenu/{roleId}",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getMenuList(@PathVariable("roleId") Long id){
        return CommonResult.success(roleService.getMenus(id));
    }


    @ApiOperation("根据角色id获得到角色分配的资源")
    @RequestMapping(value = "/listResources/{roleId}",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getResourceList(@PathVariable("roleId")Long id){
        return CommonResult.success(roleService.getResources(id));
    }



    @ApiOperation("给角色分配资源")
    @RequestMapping(value = "/allocResources/{roleId}",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult allocResources(@PathVariable(value = "roleId") Long id, @RequestParam("ids")List<Long> resourceIds){
        int count = roleService.allocResources(id,resourceIds);
        if(count>0){
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }
    }

    @ApiOperation("给角色分配菜单")
    @RequestMapping(value = "/allocMenus/{roleId}",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult allocMenus(@PathVariable("roleId") Long id,@RequestParam("ids")List<Long> ids){
        int count = roleService.allocMenus(id,ids);
        if(count>0) return CommonResult.success(count);
        else return CommonResult.failed();
    }





}
