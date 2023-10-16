package com.papa.dao;

import com.papa.mbg.model.PmsProductVertifyRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PmsProductVerifyDAO {
    public int insertList(@Param("list") List<PmsProductVertifyRecord> recordList);
}
