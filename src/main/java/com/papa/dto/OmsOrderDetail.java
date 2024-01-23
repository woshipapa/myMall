package com.papa.dto;

import com.papa.mbg.model.OmsOrder;
import com.papa.mbg.model.OmsOrderItem;
import com.papa.mbg.model.OmsOrderOperateHistory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
@Data
@EqualsAndHashCode
public class OmsOrderDetail  extends OmsOrder {

    @ApiModelProperty("订单商品列表")
    private List<OmsOrderItem> orderItemList;


    @ApiModelProperty("订单操作历史")
    private List<OmsOrderOperateHistory> operateHistories;
}
