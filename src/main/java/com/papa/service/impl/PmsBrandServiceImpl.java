package com.papa.service.impl;

import com.github.pagehelper.PageHelper;
import com.papa.mbg.mapper.PmsBrandMapper;
import com.papa.mbg.model.PmsBrand;
import com.papa.mbg.model.PmsBrandExample;
import com.papa.service.PmsBrandService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class PmsBrandServiceImpl implements PmsBrandService {

    @Resource
    private PmsBrandMapper pmsBrandMapper;

    @Override
    public List<PmsBrand> listAllBrand() {
        //example对象属性为空
        return pmsBrandMapper.selectByExample(new PmsBrandExample());
    }

    @Override
    public int createBrand(PmsBrand pmsBrand) {
        return pmsBrandMapper.insertSelective(pmsBrand);
    }

    @Override
    public int deleteBrand(Long id) {
        return pmsBrandMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateBrand(Long id, PmsBrand pmsBrand) {
        pmsBrand.setId(id);
        return pmsBrandMapper.updateByPrimaryKeySelective(pmsBrand);
    }

    @Override
    public PmsBrand getBrand(Long id) {
        return pmsBrandMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<PmsBrand> listBrand(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return pmsBrandMapper.selectByExample(new PmsBrandExample());
    }


}
