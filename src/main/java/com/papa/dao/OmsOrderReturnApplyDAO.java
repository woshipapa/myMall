package com.papa.dao;

import com.papa.dto.OmsOrderReturnApplyResult;
import com.papa.dto.OmsReturnApplyQueryParam;
import com.papa.mbg.model.OmsOrderReturnApply;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OmsOrderReturnApplyDAO {

    public List<OmsOrderReturnApply> getList(@Param("param")OmsReturnApplyQueryParam param);

    public OmsOrderReturnApplyResult getDetail(@Param("id") Long id);
}
