package com.papa.dao;

import com.papa.dto.SmsCouponParam;
import org.apache.ibatis.annotations.Param;

public interface SmsCouponDAO {
    /**
     * 获取优惠券详情包括绑定关系
     */
    SmsCouponParam getItem(@Param("id") Long id);
}
