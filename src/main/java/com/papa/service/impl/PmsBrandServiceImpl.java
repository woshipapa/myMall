package com.papa.service.impl;

import com.github.pagehelper.PageHelper;
import com.papa.dto.PmsBrandParam;
import com.papa.mbg.mapper.PmsBrandMapper;
import com.papa.mbg.mapper.PmsProductMapper;
import com.papa.mbg.model.PmsBrand;
import com.papa.mbg.model.PmsBrandExample;
import com.papa.mbg.model.PmsProduct;
import com.papa.mbg.model.PmsProductExample;
import com.papa.service.PmsBrandService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sun.swing.StringUIClientPropertyKey;

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
    public int createBrand(PmsBrandParam param) {
        PmsBrand brand = new PmsBrand();
        BeanUtils.copyProperties(param,brand);
        if(!StringUtils.hasText(brand.getFirstLetter())){
            brand.setFirstLetter(brand.getName().substring(0,1).toUpperCase());
        }
        return pmsBrandMapper.insertSelective(brand);
    }

    @Override
    public int deleteBrand(Long id) {
        return pmsBrandMapper.deleteByPrimaryKey(id);
    }

    @Resource
    private PmsProductMapper productMapper;
    @Override
    public int updateBrand(Long id, PmsBrandParam param) {
        PmsBrand brand = new PmsBrand();
        BeanUtils.copyProperties(param,brand);
        brand.setId(id);
        if(!StringUtils.hasText(brand.getFirstLetter())){
            brand.setFirstLetter(brand.getName().substring(0,1).toUpperCase());
        }
        PmsProductExample productExample = new PmsProductExample();
        productExample.createCriteria().andBrandIdEqualTo(id);
        PmsProduct product = new PmsProduct();
        product.setBrandName(brand.getName());
        productMapper.updateByExampleSelective(product,productExample);
        return pmsBrandMapper.updateByPrimaryKeySelective(brand);
    }

    @Override
    public PmsBrand getBrand(Long id) {
        return pmsBrandMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<PmsBrand> listBrand(String keyword,int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        PmsBrandExample example =  new PmsBrandExample();
        PmsBrandExample.Criteria criteria = example.createCriteria();
        if(StringUtils.hasText(keyword)){
            criteria.andNameLike("%"+keyword+"%");
        }
        return pmsBrandMapper.selectByExample(example);
    }

    @Override
    public int updateShowStatus(List<Long> ids, Integer status) {
        PmsBrandExample example = new PmsBrandExample();
        example.createCriteria().andIdIn(ids);
        PmsBrand brand = new PmsBrand();
        brand.setShowStatus(status);
        return pmsBrandMapper.updateByExampleSelective(brand,example);
    }

    @Override
    public int updateFactoryStatus(List<Long> ids, Integer status) {
        PmsBrandExample example = new PmsBrandExample();
        example.createCriteria().andIdIn(ids);
        PmsBrand brand = new PmsBrand();
        brand.setFactoryStatus(status);
        return pmsBrandMapper.updateByExampleSelective(brand,example);



    }


}
