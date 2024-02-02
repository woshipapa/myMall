package com.papa.service.impl;

import com.papa.mbg.mapper.OmsOrderSettingMapper;
import com.papa.mbg.model.OmsOrderSetting;
import com.papa.service.OmsOrderService;
import com.papa.service.OmsOrderSettingService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
@Service
public class OmsOrderSettingServiceImpl implements OmsOrderSettingService {

    @Resource
    private OmsOrderSettingMapper settingMapper;


    @Override
    public OmsOrderSetting getItem(Long id) {
        return settingMapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(Long id, OmsOrderSetting setting) {
        setting.setId(id);
        int res = settingMapper.updateByPrimaryKeySelective(setting);
        return res;
    }
}
