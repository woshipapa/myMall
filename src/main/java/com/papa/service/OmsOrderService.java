package com.papa.service;

import com.papa.dto.OmsOrderDeliveryParam;
import com.papa.dto.OmsOrderDetail;
import com.papa.dto.OmsOrderQueryParam;
import com.papa.dto.OmsOrderReceiverInfoParam;
import com.papa.mbg.model.OmsOrder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OmsOrderService {

    public List<OmsOrder> list(OmsOrderQueryParam param,Integer pageNum,Integer pageSize);

    @Transactional
    //批量发货
    public int delivery(List<OmsOrderDeliveryParam> list);

    @Transactional
    //批量关闭订单
    public int close(List<Long> ids,String notes);


    //批量删除
    public int delete(List<Long> ids);


    //获取订单详情，包括订单的基本信息以及其中包含的商品信息和订单的操作历史
    public OmsOrderDetail detail(Long id);


    //更新收货人的信息
    public int updateReceiverInfo(OmsOrderReceiverInfoParam param);


}
