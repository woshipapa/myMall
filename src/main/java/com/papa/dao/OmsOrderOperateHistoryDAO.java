package com.papa.dao;

import com.papa.mbg.model.OmsOrderOperateHistory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OmsOrderOperateHistoryDAO {


    public int insertList(@Param("list") List<OmsOrderOperateHistory> orderOperateHistories);


}
