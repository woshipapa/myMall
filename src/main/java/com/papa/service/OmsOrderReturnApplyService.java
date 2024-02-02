package com.papa.service;

import com.papa.dto.OmsOrderReturnApplyResult;
import com.papa.dto.OmsReturnApplyQueryParam;
import com.papa.dto.OmsUpdateStatusParam;
import com.papa.mbg.model.OmsOrderReturnApply;

import java.util.List;

public interface OmsOrderReturnApplyService {


    public List<OmsOrderReturnApply> list(OmsReturnApplyQueryParam param,Integer pageNum,Integer pageSize);


    public OmsOrderReturnApplyResult getItem(Long id);

    public int delete(List<Long> ids);

    public int updateStatus(Long id, OmsUpdateStatusParam param);
}
