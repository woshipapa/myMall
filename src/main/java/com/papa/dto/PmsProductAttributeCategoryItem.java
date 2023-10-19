package com.papa.dto;

import com.papa.mbg.model.PmsProductAttribute;
import com.papa.mbg.model.PmsProductAttributeCategory;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
@Data
@EqualsAndHashCode
public class PmsProductAttributeCategoryItem extends PmsProductAttributeCategory {

    private List<PmsProductAttribute> attributeList;
}
