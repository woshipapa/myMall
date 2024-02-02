package com.papa.dto;

import com.papa.mbg.model.OmsCompanyAddress;
import com.papa.mbg.model.OmsOrderReturnApply;
import lombok.Data;

@Data
public class OmsOrderReturnApplyResult extends OmsOrderReturnApply {

    private OmsCompanyAddress companyAddress;

}
