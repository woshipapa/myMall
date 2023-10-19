package com.papa.dto;

import com.papa.common.validator.FlagValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
@Data
@EqualsAndHashCode
public class PmsBrandParam {

    @NotEmpty
    @ApiModelProperty("品牌名称")
    private String name;

    @ApiModelProperty("品牌首字母")
    private String firstLetter;

    @NotEmpty
    @ApiModelProperty("品牌logo")
    private String logo;

    @ApiModelProperty("品牌大图")
    private String bigPic;


    @ApiModelProperty("品牌故事")
    private String brandStory;

    @Min(value = 0)
    @ApiModelProperty("排序")
    private Integer sort;

    @FlagValidator(value = {"0","1"})
    @ApiModelProperty("是否显示")
    private Integer showStatus;


    @FlagValidator(value = {"0","1"})
    @ApiModelProperty("是否为品牌制造商")
    private Integer factoryStatus;
}
