package com.papa.service.impl;

import com.github.pagehelper.PageHelper;
import com.papa.dao.OmsOrderReturnApplyDAO;
import com.papa.design.OmsOrderReturnApplyStatus;
import com.papa.dto.OmsOrderReturnApplyResult;
import com.papa.dto.OmsReturnApplyQueryParam;
import com.papa.dto.OmsUpdateStatusParam;
import com.papa.mbg.mapper.OmsOrderReturnApplyMapper;
import com.papa.mbg.model.OmsOrderReturnApply;
import com.papa.mbg.model.OmsOrderReturnApplyExample;
import com.papa.service.OmsOrderReturnApplyService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

public class OmsOrderReturnApplyServiceImpl implements OmsOrderReturnApplyService {

    @Resource
    private OmsOrderReturnApplyMapper returnApplyMapper;

    @Resource
    private OmsOrderReturnApplyDAO returnApplyDAO;

    @Override
    public List<OmsOrderReturnApply> list(OmsReturnApplyQueryParam param, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return returnApplyDAO.getList(param);
    }

    @Override
    public OmsOrderReturnApplyResult getItem(Long id) {
        return returnApplyDAO.getDetail(id);
    }

    @Override
    public int delete(List<Long> ids) {
        OmsOrderReturnApplyExample returnApplyExample = new OmsOrderReturnApplyExample();
        returnApplyExample.createCriteria().andIdIn(ids).andStatusEqualTo(OmsOrderReturnApplyStatus.REJECTED.getValue());
        return returnApplyMapper.deleteByExample(returnApplyExample);
    }

    @Override
    public int updateStatus(Long id, OmsUpdateStatusParam statusParam) {
        Integer status = statusParam.getStatus();
        OmsOrderReturnApply returnApply = new OmsOrderReturnApply();
        if(status.equals(1)){
            //确认退货
            returnApply.setId(id);
            returnApply.setStatus(1);
            returnApply.setReturnAmount(statusParam.getReturnAmount());
            returnApply.setCompanyAddressId(statusParam.getCompanyAddressId());
            returnApply.setHandleTime(new Date());
            returnApply.setHandleMan(statusParam.getHandleMan());
            returnApply.setHandleNote(statusParam.getHandleNote());
        }else if(status.equals(2)){
            //完成退货
            returnApply.setId(id);
            returnApply.setStatus(2);
            returnApply.setReceiveTime(new Date());
            returnApply.setReceiveMan(statusParam.getReceiveMan());
            returnApply.setReceiveNote(statusParam.getReceiveNote());
        }else if(status.equals(3)){
            //拒绝退货
            returnApply.setId(id);
            returnApply.setStatus(3);
            returnApply.setHandleTime(new Date());
            returnApply.setHandleMan(statusParam.getHandleMan());
            returnApply.setHandleNote(statusParam.getHandleNote());
        }else{
            return 0;
        }
        return returnApplyMapper.updateByPrimaryKeySelective(returnApply);
    }

}
