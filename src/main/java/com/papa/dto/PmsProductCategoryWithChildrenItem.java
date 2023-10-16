package com.papa.dto;

import com.papa.mbg.model.PmsProductCategory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class PmsProductCategoryWithChildrenItem extends PmsProductCategory {

    @Getter
    @Setter
    @ApiModelProperty("子级商品类型分类")
    private List<PmsProductCategoryWithChildrenItem> childrens;
}
