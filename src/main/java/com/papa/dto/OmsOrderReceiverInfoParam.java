package com.papa.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class OmsOrderReceiverInfoParam {
    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("收货人姓名")
    private String receiverName;

    @ApiModelProperty("收货人电话")
    private String receiverPhone;

    @ApiModelProperty(value = "详细地址")
    private String receiverDetailAddress;
    @ApiModelProperty("收货人邮政")
    private String receiverPostCode;

    @ApiModelProperty("收货人所在省份")
    private String receiverProvince;

    @ApiModelProperty("收货人所在城市")
    private String receiverCity;

    @ApiModelProperty("收货人所在区域")
    private String receiverRegion;


    private Integer status;





}
