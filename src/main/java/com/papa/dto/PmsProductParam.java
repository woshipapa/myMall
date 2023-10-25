package com.papa.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode
public class PmsProductParam extends PmsProduct {
    @ApiModelProperty("商品会员价格列表")
    private List<PmsMemberPrice> memberPriceList;

    @ApiModelProperty("商品满减价格列表")
    private List<PmsProductFullReduction> productFullReductionList;

    @ApiModelProperty("商品阶梯价格列表")
    private List<PmsProductLadder> productLadderList;

    @ApiModelProperty("商品sku库存列表")
    private List<PmsSkuStock> skuStockList;

    @ApiModelProperty("商品参数和自定义规格列表")
    private List<PmsProductAttributeValue> productAttributeValues;
    @ApiModelProperty("与商品相关联的偏好专区")
    private List<CmsPrefrenceAreaProductRelation> prefrenceAreaProductRelationList;

    @ApiModelProperty("与商品相关联的主题")
    private List<CmsSubjectProductRelation> subjectProductRelations ;



}
