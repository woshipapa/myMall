package com.papa.controller;

import com.papa.common.api.CommonResult;
import com.papa.dto.UmsMenuNode;
import com.papa.service.UmsMenuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/menu")
public class UmsMenuController {
    @Resource
    private UmsMenuService umsMenuService;

    @RequestMapping(value = "/treelist",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult treeList(){
        List<UmsMenuNode> list=umsMenuService.treeNodeList();
        return CommonResult.success(list);
    }
}
