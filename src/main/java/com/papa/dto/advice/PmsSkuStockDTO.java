package com.papa.dto.advice;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import com.papa.mbg.model.PmsSkuStock;
@Data
@EqualsAndHashCode
public class PmsSkuStockDTO extends PmsSkuStock {
    private List<PmsSkuStockAttributeValueDTO> attrs;
}
