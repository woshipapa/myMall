package com.papa.dto;

import com.papa.common.validator.FlagValidator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
@Data
@EqualsAndHashCode
public class PmsProductAttributeParam {
    @ApiModelProperty("与商品属性关联的商品类型")
    private Long productCategoryId;

    @NotEmpty
    @ApiModelProperty("关联的属性组id")
    private Long categoryId;

    @NotEmpty
    private String name;

    @FlagValidator(value = {"0","1"})
    private Integer filterType;

    @FlagValidator(value = {"0","1","2"})
    private Integer searchType;

    @FlagValidator({"0","1"})
    private Integer relatedStatus;

    @FlagValidator({"0","1","2"})
    private Integer selectType;

    @FlagValidator({"0","1"})
    private Integer inputType;

    private String inputList;


    @FlagValidator({"0","1"})
    private Integer handAddStatus;
    @FlagValidator({"0","1"})
    private Integer type;
}
