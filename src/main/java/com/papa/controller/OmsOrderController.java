package com.papa.controller;

import com.papa.common.api.CommonPage;
import com.papa.common.api.CommonResult;
import com.papa.dto.OmsOrderDeliveryParam;
import com.papa.dto.OmsOrderDetail;
import com.papa.dto.OmsOrderQueryParam;
import com.papa.dto.OmsOrderReceiverInfoParam;
import com.papa.mbg.model.OmsOrder;
import com.papa.service.OmsOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.mockito.stubbing.ValidableAnswer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@Api(value = "OmsOrderController")
@ResponseBody
@RequestMapping("/order")
public class OmsOrderController {

    @Resource
    private OmsOrderService orderService;


    @ApiOperation("查询订单")
    @RequestMapping(method = RequestMethod.GET,value ="/list")
    public CommonResult<CommonPage<OmsOrder>> list(OmsOrderQueryParam param,
                                                   @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                                                   @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize){
        List<OmsOrder> list = orderService.list(param, pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(list));
    }

    @ApiOperation("批量发货")
    @RequestMapping(method = RequestMethod.POST,value = "/update/delivery")
    public CommonResult delivery(@RequestBody List<OmsOrderDeliveryParam> params){
        int result = orderService.delivery(params);
        if(result > 0){
            return CommonResult.success(result);
        }else{
            return CommonResult.failed();
        }
    }

    @ApiOperation("批量关闭订单")
    @RequestMapping(method = RequestMethod.POST,value = "/update/close")
    public CommonResult close(@RequestParam("ids") List<Long> ids,@RequestParam("content") String note){
        int result = orderService.close(ids, note);
        if(result > 0){
            return CommonResult.success(result);
        }else{
            return CommonResult.failed();
        }
    }

    @ApiOperation("批量删除订单")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = orderService.delete(ids);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }


    @ApiOperation("获取订单详情：订单信息、商品信息、操作记录")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public CommonResult detail(@PathVariable("id") Long id){
        OmsOrderDetail detail = orderService.detail(id);
        return CommonResult.success(detail);
    }


    @ApiOperation("修改收货人信息")
    @RequestMapping(value = "/update/receiverInfo", method = RequestMethod.POST)
    public CommonResult updateReceiverInfo(@RequestBody OmsOrderReceiverInfoParam receiverInfoParam) {
        int count = orderService.updateReceiverInfo(receiverInfoParam);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }


    @ApiOperation("备注订单")
    @RequestMapping(value = "/update/note", method = RequestMethod.POST)
    public CommonResult updateNote(@RequestParam("id") Long id,
                                   @RequestParam("note") String note,
                                   @RequestParam("status") Integer status) {
        int count = orderService.updateOrderNote(id, note, status);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}
