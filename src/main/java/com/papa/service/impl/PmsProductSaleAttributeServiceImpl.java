package com.papa.service.impl;

import com.github.pagehelper.PageHelper;
import com.papa.dto.PmsProductAttributeParam;
import com.papa.dto.ProductAttrInfo;
import com.papa.mbg.model.PmsProductAttribute;
import com.papa.mbg.model.PmsProductAttributeExample;
import com.papa.service.PmsProductAttributeAbs;
import com.papa.vo.PmsProductAttributeVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PmsProductSaleAttributeServiceImpl extends PmsProductAttributeAbs {


    public List<PmsProductAttribute> list(Integer type, Integer pageNum, Integer pageSize) {
            PmsProductAttributeExample attributeExample = new PmsProductAttributeExample();
            attributeExample.createCriteria().andTypeEqualTo(type);
            PageHelper.startPage(pageNum,pageSize);
            return productAttributeMapper.selectByExample(attributeExample);
    }

    @Override
    public int create(PmsProductAttributeParam productAttributeParam) {
        PmsProductAttribute attribute = new PmsProductAttribute();
        BeanUtils.copyProperties(productAttributeParam,attribute);//销售属性不需要考虑与属性组的关联关系
        return productAttributeMapper.insertSelective(attribute);
    }

    @Override
    public PmsProductAttributeVO getItem(Long id) {
        PmsProductAttribute attribute =  productAttributeMapper.selectByPrimaryKey(id);
        PmsProductAttributeVO attributeVO = new PmsProductAttributeVO();
        BeanUtils.copyProperties(attribute,attributeVO);
        return attributeVO;
    }

    @Override
    public int update(Long id, PmsProductAttributeParam productAttributeParam) {
        PmsProductAttribute attribute = new PmsProductAttribute();
        BeanUtils.copyProperties(productAttributeParam,attribute);
        attribute.setId(id);
        int count = productAttributeMapper.updateByPrimaryKeySelective(attribute);
        return count;
    }

    @Override
    public int delete(List<Long> ids) {
        PmsProductAttributeExample attributeExample = new PmsProductAttributeExample();
        attributeExample.createCriteria().andIdIn(ids);
        return productAttributeMapper.deleteByExample(attributeExample);
    }


}
