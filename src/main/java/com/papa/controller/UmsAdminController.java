package com.papa.controller;

import com.papa.common.api.CommonResult;
import com.papa.dto.AdminParam;
import com.papa.dto.UmsAdminLoginParam;
import com.papa.mbg.model.UmsAdmin;
import com.papa.service.UmsAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Tag;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

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
}
