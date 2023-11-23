package com.papa.dto.advice;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode
public class PmsSkuStockAttributeValueDTO implements Serializable {
    private Long id;

    private Long skuId;

    private Long attributeId;

    private String key;

    private String value;

    private static final long serialVersionUID = 1L;
}
