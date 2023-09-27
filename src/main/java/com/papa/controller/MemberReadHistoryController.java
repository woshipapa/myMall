package com.papa.controller;

import com.papa.common.api.CommonResult;
import com.papa.nosql.mongodb.document.MemberReadHistory;
import com.papa.service.MemberReadHistoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/member/readHistory")
public class MemberReadHistoryController {

    @Resource
    public MemberReadHistoryService memberReadHistoryService;


    @ApiOperation("创建用户的历史访问记录")
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody MemberReadHistory memberReadHistory){
        memberReadHistoryService.create(memberReadHistory);
        return CommonResult.success(null);
    }

    @ApiOperation("批量删除访问历史记录")
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@RequestParam("ids") List<String> ids){
        int count = memberReadHistoryService.delete(ids);
        if(count > 0){
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }
    }

    @ApiOperation("获得指定会员id的历史记录")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult list(@RequestParam("id") Integer id){
        return CommonResult.success(memberReadHistoryService.list(id));
    }
}
