package com.papa.service.impl;

import com.papa.common.api.CommonResult;
import com.papa.component.CancelOrderSender;
import com.papa.dto.OrderParam;
import com.papa.service.OmsPortalOrderService;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
@Service
public class OmsPortalOrderServiceImpl implements OmsPortalOrderService {

    @Resource
    private CancelOrderSender cancelOrderSender;

    @Override
    public CommonResult generateOrder(OrderParam orderParam) {
        //具体的下单逻辑，还要得到订单号
        sendDelayMessageCancelOrder(11);
        return CommonResult.success(null,"下单成功");
    }

    @Override
    public void cancelOrder(Integer orderId) {

    }

    private void sendDelayMessageCancelOrder(Integer orderId){
        long delayTimes=30*1000;
        cancelOrderSender.sendMessage(orderId,delayTimes);
    }
}
