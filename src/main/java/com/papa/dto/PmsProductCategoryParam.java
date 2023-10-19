package com.papa.dto;

import com.papa.common.validator.FlagValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class PmsProductCategoryParam {

    @ApiModelProperty("上级分类id")
    private Long parentId;

    @NotEmpty
    @ApiModelProperty("分类名称")
    private String name;
    @ApiModelProperty("分类单位")
    private String productUnit;

    @FlagValidator(value = {"0","1"},message = "状态只能为0或者1")
    @ApiModelProperty("是否在导航栏显示")
    private Integer navStatus;

    @FlagValidator(value = {"0","1"},message = "状态只能为0或者1")
    @ApiModelProperty("是否进行显示")
    private Integer showStatus;
    @Min(0)
    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("关键词")
    private String keywords;

    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("与该分类相关的筛选属性")
    private List<Long> productAttributeIdList;
}
