package com.papa.service.impl;

import com.github.pagehelper.PageHelper;
import com.papa.dao.OmsOrderDAO;
import com.papa.dao.OmsOrderOperateHistoryDAO;
import com.papa.dto.OmsOrderDeliveryParam;
import com.papa.dto.OmsOrderDetail;
import com.papa.dto.OmsOrderQueryParam;
import com.papa.dto.OmsOrderReceiverInfoParam;
import com.papa.mbg.mapper.OmsOrderMapper;
import com.papa.mbg.mapper.OmsOrderOperateHistoryMapper;
import com.papa.mbg.model.OmsOrder;
import com.papa.mbg.model.OmsOrderExample;
import com.papa.mbg.model.OmsOrderOperateHistory;
import com.papa.portal.design.OrderStatus;
import com.papa.service.OmsOrderService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class OmsOrderServiceImpl implements OmsOrderService {

    @Resource
    private OmsOrderMapper orderMapper;
    @Resource
    private OmsOrderDAO orderDAO;

    @Resource
    private OmsOrderOperateHistoryMapper operateHistoryMapper;


    @Resource
    private OmsOrderOperateHistoryDAO operateHistoryDAO;
    @Override
    public List<OmsOrder> list(OmsOrderQueryParam param, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<OmsOrder> orders = orderDAO.list(param);
        return orders;
    }

    @Override
    public int delivery(List<OmsOrderDeliveryParam> list) {
        //更新订单表中这个订单的状态和物流信息
        for(OmsOrderDeliveryParam it : list){
            OmsOrder order = new OmsOrder();
            order.setId(it.getOrderId());
            order.setStatus(OrderStatus.SHIPPED.getValue());
            order.setDeliveryCompany(it.getDeliveryCompany());
            order.setDeliverySn(it.getDeliverySn());
            orderMapper.updateByPrimaryKeySelective(order);
        }
        List<OmsOrderOperateHistory> orderOperateHistories = list.stream().map(
                it -> {
                    OmsOrderOperateHistory operateHistory = new OmsOrderOperateHistory();
                    operateHistory.setOrderId(it.getOrderId());
                    operateHistory.setOperateMan("后台管理员");
                    operateHistory.setNote("完成发货");
                    operateHistory.setCreateTime(new Date());
                    operateHistory.setOrderStatus(OrderStatus.SHIPPED.getValue());
                    return operateHistory;
                }
        ).collect(Collectors.toList());
        operateHistoryDAO.insertList(orderOperateHistories);
        return 0;
    }

    @Override
    public int close(List<Long> ids, String notes) {
        //更新订单状态
        OmsOrderExample example = new OmsOrderExample();
        example.createCriteria().andDeleteStatusEqualTo(0).andIdIn(ids);
        OmsOrder order = new OmsOrder();
        order.setStatus(OrderStatus.CLOSED.getValue());
        int count = orderMapper.updateByExampleSelective(order,example);
        List<OmsOrderOperateHistory> orderOperateHistories = ids.stream().map(
                id->{
                    OmsOrderOperateHistory operateHistory = new OmsOrderOperateHistory();
                    operateHistory.setOrderId(id);
                    operateHistory.setOrderStatus(OrderStatus.CLOSED.getValue());
                    operateHistory.setNote(notes);
                    operateHistory.setOperateMan("后台管理员");
                    operateHistory.setCreateTime(new Date());
                    return operateHistory;
                }
        ).collect(Collectors.toList());
        operateHistoryDAO.insertList(orderOperateHistories);
        return count;
    }

    @Override
    public int delete(List<Long> ids) {
        OmsOrderExample example = new OmsOrderExample();
        example.createCriteria().andDeleteStatusEqualTo(0).andIdIn(ids);
        OmsOrder order = new OmsOrder();
        order.setDeleteStatus(1);
        return orderMapper.updateByExampleSelective(order,example);
    }

    @Override
    public OmsOrderDetail detail(Long id) {
        return orderDAO.detail(id);
    }

    @Override
    public int updateReceiverInfo(OmsOrderReceiverInfoParam param) {
        //更新订单的收货信息
        OmsOrder order = new OmsOrder();
        order.setId(param.getOrderId());
        order.setReceiverName(param.getReceiverName());
        order.setReceiverPhone(param.getReceiverPhone());
        order.setReceiverPostCode(param.getReceiverPostCode());
        order.setReceiverProvince(param.getReceiverProvince());
        order.setReceiverCity(param.getReceiverCity());
        order.setReceiverRegion(param.getReceiverRegion());
        order.setReceiverDetailAddress(param.getReceiverDetailAddress());
        int count = orderMapper.updateByPrimaryKeySelective(order);
        OmsOrderOperateHistory history = new OmsOrderOperateHistory();
        history.setOrderId(param.getOrderId());
        history.setCreateTime(new Date());
        history.setOperateMan("后台管理员");
        history.setOrderStatus(param.getStatus());
        history.setNote("修改收货人信息");
        operateHistoryMapper.insertSelective(history);
        return count;
    }
}
