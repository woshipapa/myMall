package com.papa.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode
public class PmsProductResult extends PmsProductParam{
    private List<Long> parentIds;
}
