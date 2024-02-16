package com.papa.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.papa.dao.SmsCouponDAO;
import com.papa.dao.SmsCouponProductCategoryRelationDAO;
import com.papa.dao.SmsCouponProductRelationDAO;
import com.papa.dto.SmsCouponParam;
import com.papa.mbg.mapper.SmsCouponMapper;
import com.papa.mbg.mapper.SmsCouponProductCategoryRelationMapper;
import com.papa.mbg.mapper.SmsCouponProductRelationMapper;
import com.papa.mbg.model.*;
import com.papa.service.SmsCouponService;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class SmsCouponServiceImpl implements SmsCouponService {

    @Resource
    private SmsCouponMapper couponMapper;

    @Resource
    private SmsCouponProductRelationDAO productRelationDao;

    @Resource
    private SmsCouponProductCategoryRelationDAO productCategoryRelationDao;

    @Resource
    private SmsCouponProductRelationMapper productRelationMapper;

    @Resource
    private SmsCouponProductCategoryRelationMapper productCategoryRelationMapper;

    @Resource
    private SmsCouponDAO couponDao;
    @Override
    public int create(SmsCouponParam couponParam) {
        couponParam.setCount(couponParam.getPublishCount());
        couponParam.setUseCount(0);
        couponParam.setReceiveCount(0);
        //插入优惠券表
        int count = couponMapper.insert(couponParam);
        //插入优惠券和商品关系表
        if(couponParam.getUseType().equals(2)){
            if(CollUtil.isNotEmpty(couponParam.getProductRelationList())) {
                for (SmsCouponProductRelation productRelation : couponParam.getProductRelationList()) {
                    productRelation.setCouponId(couponParam.getId());
                }
                productRelationDao.insertList(couponParam.getProductRelationList());
            }
        }
        //插入优惠券和商品分类关系表
        if(couponParam.getUseType().equals(1)){
            if(CollUtil.isNotEmpty(couponParam.getProductCategoryRelationList())) {
                for (SmsCouponProductCategoryRelation couponProductCategoryRelation : couponParam.getProductCategoryRelationList()) {
                    couponProductCategoryRelation.setCouponId(couponParam.getId());
                }
                productCategoryRelationDao.insertList(couponParam.getProductCategoryRelationList());
            }
        }
        return count;
    }

    //删除优惠券时，也要删除关联表中的记录
    @Override
    public int delete(Long id) {
        //删除优惠券
        int count = couponMapper.deleteByPrimaryKey(id);
        //删除商品关联
        deleteProductRelation(id);
        //删除商品分类关联
        deleteProductCategoryRelation(id);
        return count;
    }

    private void deleteProductCategoryRelation(Long id) {
        SmsCouponProductCategoryRelationExample productCategoryRelationExample = new SmsCouponProductCategoryRelationExample();
        productCategoryRelationExample.createCriteria().andCouponIdEqualTo(id);
        productCategoryRelationMapper.deleteByExample(productCategoryRelationExample);
    }

    private void deleteProductRelation(Long id) {
        SmsCouponProductRelationExample productRelationExample = new SmsCouponProductRelationExample();
        productRelationExample.createCriteria().andCouponIdEqualTo(id);
        productRelationMapper.deleteByExample(productRelationExample);
    }

    @Override
    public int update(Long id, SmsCouponParam couponParam) {
        couponParam.setId(id);
        int count =couponMapper.updateByPrimaryKey(couponParam);
        //删除后插入优惠券和商品关系表
        if(couponParam.getUseType().equals(2)){
            for(SmsCouponProductRelation productRelation:couponParam.getProductRelationList()){
                productRelation.setCouponId(couponParam.getId());
            }
            deleteProductRelation(id);
            productRelationDao.insertList(couponParam.getProductRelationList());
        }
        //删除后插入优惠券和商品分类关系表
        if(couponParam.getUseType().equals(1)){
            for (SmsCouponProductCategoryRelation couponProductCategoryRelation : couponParam.getProductCategoryRelationList()) {
                couponProductCategoryRelation.setCouponId(couponParam.getId());
            }
            deleteProductCategoryRelation(id);
            productCategoryRelationDao.insertList(couponParam.getProductCategoryRelationList());
        }
        return count;
    }
    @Override
    public List<SmsCoupon> list(String name, Integer type, Integer pageSize, Integer pageNum) {
        SmsCouponExample example = new SmsCouponExample();
        SmsCouponExample.Criteria criteria = example.createCriteria();
        if(!StrUtil.isEmpty(name)){
            criteria.andNameLike("%"+name+"%");
        }
        if(type!=null){
            criteria.andTypeEqualTo(type);
        }
        PageHelper.startPage(pageNum,pageSize);
        return couponMapper.selectByExample(example);
    }

    @Override
    public SmsCouponParam getItem(Long id) {
        return couponDao.getItem(id);
    }
}
