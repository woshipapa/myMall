package com.papa.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class PmsProductQueryParam {

    @ApiModelProperty("商品名称关键字")
    private String keyword;

    @ApiModelProperty("商品货号")
    private String productSn;

    //叶子商品分类
    @ApiModelProperty("商品分类id")
    private Long categoryId;

    @ApiModelProperty("品牌id")
    private Long brandId;

    @ApiModelProperty("上架状态")
    private Integer publicStatus;

    @ApiModelProperty("审核状态")
    private Integer verifyStatus;

}
