package com.papa.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class OmsOrderQueryParam {
    @ApiModelProperty("订单编号")
    private String orderSn;

    @ApiModelProperty("收货人姓名/手机号")
    private String receiverKeyword;

    @ApiModelProperty("订单提交时间")
    private String createTime;

    @ApiModelProperty("订单状态")
    private Integer status;

    @ApiModelProperty("订单来源")
    private Integer sourceType;

    @ApiModelProperty("订单类型")
    private Integer orderType;

}
