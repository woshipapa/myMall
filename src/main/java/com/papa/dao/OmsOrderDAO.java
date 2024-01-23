package com.papa.dao;

import com.papa.dto.OmsOrderDetail;
import com.papa.dto.OmsOrderQueryParam;
import com.papa.mbg.model.OmsOrder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface OmsOrderDAO {

    public List<OmsOrder> list(OmsOrderQueryParam param);

    public OmsOrderDetail detail(Long id);


}
