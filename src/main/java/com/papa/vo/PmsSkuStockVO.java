package com.papa.vo;

import com.papa.mbg.model.PmsSkuStock;
import com.papa.mbg.model.PmsSkuStockAttributeValue;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
@Data
@EqualsAndHashCode
public class PmsSkuStockVO extends PmsSkuStock {
    @ApiModelProperty("销售属性键值对列表")
    private List<PmsSkuStockAttributeValue> attrs;
}
