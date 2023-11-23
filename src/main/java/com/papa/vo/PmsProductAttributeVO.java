package com.papa.vo;

import com.papa.mbg.model.PmsProductAttribute;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class PmsProductAttributeVO extends PmsProductAttribute {
    private Long productAttributeCategoryId;
}
