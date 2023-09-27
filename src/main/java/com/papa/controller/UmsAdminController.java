package com.papa.controller;

import cn.hutool.core.collection.CollUtil;
import com.papa.common.api.CommonPage;
import com.papa.common.api.CommonResult;
import com.papa.dao.UmsAdminRoleRelationDAO;
import com.papa.dto.AdminParam;
import com.papa.dto.UmsAdminLoginParam;
import com.papa.dto.UmsAdminPasswordParam;
import com.papa.mbg.model.UmsAdmin;
import com.papa.mbg.model.UmsResource;
import com.papa.mbg.model.UmsRole;
import com.papa.service.UmsAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Tag;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@Api(value="UmsAdminController",description = "用户信息控制器")
public class UmsAdminController {

    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Resource
    private UmsAdminService umsAdminService;
    @PostMapping("/register")
    @ApiOperation("注册用户")
    public CommonResult register(@Validated @RequestBody AdminParam adminParam, BindingResult result){
        UmsAdmin admin=umsAdminService.register(adminParam);
        if(admin==null){
            return CommonResult.failed();
        }
        return CommonResult.success(admin);
    }

    @PostMapping("/login")
    @ApiOperation("登录用户")
    public CommonResult login(@RequestBody UmsAdminLoginParam param){
        String token = umsAdminService.login(param.getUsername(), param.getPassword());
        if(token == null){
            return CommonResult.failed("用户名或者密码错误");
        }
        //返回给前端
        Map<String,Object> map=new HashMap<>();
        map.put("tokenHead",tokenHead);
        map.put("token",token);
        return CommonResult.success(map);
    }

    @GetMapping("/permission/{id}")
    @ApiOperation("获取用户权限列表")
    public CommonResult getPermissionList(@PathVariable long id){
        return CommonResult.success(umsAdminService.getPermissonsByAdminId(id));
    }



    @ApiOperation("获取用户详情信息")
    @RequestMapping(value = "/info",method = RequestMethod.GET)
    public CommonResult getAdminInfo(Authentication authentication){
        String userName=authentication.getName();
        System.out.println("username = "+userName);
        UmsAdmin admin = umsAdminService.getAdminByName(userName);

        Map<String,Object> infos=new HashMap<>();
        infos.put("name",userName);
        infos.put("menus",umsAdminService.getMenusByAdmin(admin.getId()));
        infos.put("icon",admin.getIcon());
        List<UmsRole> roleList=umsAdminService.getRoles(admin.getId());
        if(CollUtil.isNotEmpty(roleList)){
            List<String> roleNameList=roleList.stream().map(role->role.getName()).collect(Collectors.toList());
            infos.put("roles",roleNameList);
        }
        return CommonResult.success(infos);
    }


    //查询时，关键字可以为空，那其实就是默认所有
    //因为是分页查询所以返回时要带有分页参数的信息
    @ApiOperation("根据用户名或者名称来分页查找用户")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public CommonResult list(@RequestParam(required = false,value = "keyword") String keyword,
                             @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                             @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize){
        List<UmsAdmin> admins=umsAdminService.list(keyword,pageNum,pageSize);
        return CommonResult.success(CommonPage.restPage(admins));
    }


    @ApiOperation("根据指定用户id回显用户信息")
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public CommonResult getItem(@PathVariable(value = "id")Long id){
        return CommonResult.success(umsAdminService.getItem(id));
    }

    @ApiOperation("修改指定用户信息")
    @RequestMapping(value = "/update/{id}",method = RequestMethod.POST)
    public CommonResult update(@PathVariable("id") Long id,
                               @RequestBody UmsAdmin umsAdmin){
        int count = umsAdminService.update(id, umsAdmin);
        if(count>0){
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }
    }


    @ApiOperation("修改用户账号的启用状态")
    @RequestMapping(value = "/updateStatus/{id}",method = RequestMethod.POST)
    public CommonResult updateStatus(@PathVariable("id")Long id,@RequestParam("status")Integer status){
        UmsAdmin umsAdmin=new UmsAdmin();
        umsAdmin.setStatus(status);
        int count = umsAdminService.update(id,umsAdmin);
        if(count>0){
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }
    }


    @ApiOperation("修改用户密码")
    @RequestMapping(value = "/updatePassword",method = RequestMethod.POST)
    public CommonResult updatePassword(@Validated @RequestBody UmsAdminPasswordParam param) {
        int res=umsAdminService.updatePassword(param);
        switch (res){
            case -1:return CommonResult.failed("不能填写为空");
            case -2:return CommonResult.failed("用户名不正确");
            case -3:return CommonResult.failed("原始密码不正确");
            default:return CommonResult.success(null);
        }

    }


    @ApiOperation("删除用户")
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.POST)
    public CommonResult delete(@PathVariable("id")Long id){
        int count = umsAdminService.delete(id);
        if(count>0){
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }
    }


    @ApiOperation("获取用户的角色信息")
    @RequestMapping(value = "/role/{adminId}",method = RequestMethod.GET)
    public CommonResult getRoles(@PathVariable("adminId")Long id){
        List<UmsRole> roleList=umsAdminService.getRoles(id);
        return CommonResult.success(roleList);
    }

    @ApiOperation("更新用户的角色信息")
    @RequestMapping(value = "/role/update",method = RequestMethod.POST)
    public CommonResult updateRoles(@RequestParam("adminId")Long adminId,
                                    @RequestParam("roleIds")List<Long> roleIds){
        int res = umsAdminService.allocRoles(adminId, roleIds);
        if(res>0){
            return CommonResult.success(res);
        }else{
            return CommonResult.failed();
        }

    }


}
