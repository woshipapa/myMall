package com.papa.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.github.pagehelper.PageHelper;
import com.papa.dao.*;
import com.papa.dto.PmsProductParam;
import com.papa.dto.PmsProductQueryParam;
import com.papa.dto.PmsProductResult;
import com.papa.mbg.mapper.*;
import com.papa.mbg.model.*;
import com.papa.service.PmsProductService;
import com.papa.vo.PmsSkuStockVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class PmsProductServiceImpl implements PmsProductService {
    private static Logger logger = LoggerFactory.getLogger(PmsProductService.class);

    @Resource
    private PmsProductMapper productMapper;

    @Resource
    private PmsMemberPriceMapper memberPriceMapper;

    @Resource
    private PmsProductFullReductionMapper fullReductionMapper;

    @Resource
    private PmsProductLadderMapper ladderMapper;

    @Resource
    private PmsProductAttributeValueMapper productAttributeValueMapper;

    @Resource
    private PmsSkuStockMapper skuStockMapper;

    @Resource
    private PmsMemberPriceDAO memberPriceDAO;

    @Resource
    private PmsProductFullReductionDAO fullReductionDAO;

    @Resource
    private PmsProductLadderDAO ladderDAO;

    @Resource
    private PmsSkuStockDAO skuStockDAO;

    @Resource
    private PmsProductAttributeValueDAO productAttributeValueDAO;


    @Resource
    private CmsSubjectProductRelationMapper subjectProductRelationMapper;
    @Resource
    private CmsPrefrenceAreaProductRelationMapper prefrenceAreaProductRelationMapper;
    @Resource
    private CmsPrefrenceAreaProductDAO prefrenceAreaProductDAO;

    @Resource CmsSubjectProductRelationDAO subjectProductRelationDAO;

    @Override
    public int create(PmsProductParam productParam) {
        PmsProduct product = productParam;
        product.setId(null);
        productMapper.insertSelective(product);
        Long productId=product.getId();
        //处理与商品相关的促销以及属性规格等等信息，也就是增加其他表的记录
        handleRelatedField(productParam,productId);
        int count = 1;
        return count;
    }

    private  void  handleRelatedField(PmsProductParam param,Long productId){
        relateAndInsertList(memberPriceDAO,param.getMemberPriceList(),productId);
        relateAndInsertList(ladderDAO,param.getProductLadderList(),productId);
        relateAndInsertList(fullReductionDAO,param.getProductFullReductionList(),productId);
        handSkucode(param.getSkuStockList(),productId);
        relateAndInsertList(skuStockDAO,param.getSkuStockList(),productId);
        addSkuImgs(param.getSkuStockList());
        relateAttrValue(param.getSkuStockList());
        relateAndInsertList(productAttributeValueDAO,param.getProductAttributeValues(),productId);
        relateAndInsertList(prefrenceAreaProductDAO,param.getPrefrenceAreaProductRelationList(),productId);
        relateAndInsertList(subjectProductRelationDAO,param.getSubjectProductRelations(),productId);
    }
    @Resource
    private PmsSkuImagesMapper imagesMapper;
    private void addSkuImgs(List<PmsSkuStockVO> skuStockList){
        List<PmsSkuImages> images = new ArrayList<>();
        for(PmsSkuStock sku : skuStockList) {
            String pic = sku.getPic();
            if (StringUtils.hasText(pic)) {
                List<String> one = Arrays.asList(pic.split(","));
                for (int i = 0; i < one.size(); i++) {
                    PmsSkuImages skuImage = new PmsSkuImages();
                    skuImage.setSkuId(sku.getId());
                    skuImage.setImgSort(0);
                    skuImage.setDefaultImg(0);//这里可以根据前端传来的进行设置
                    skuImage.setImgUrl(one.get(i));
                    images.add(skuImage);
                }
            }
        }
        if(CollUtil.isNotEmpty(images)){
            imagesMapper.insertBatch(images);
        }
    }
    @Resource
    private PmsSkuStockAttributeValueMapper skuStockAttributeValueMapper;
    private void relateAttrValue(List<PmsSkuStockVO> skuStockVOS){
        if(CollUtil.isNotEmpty(skuStockVOS)){
            for(PmsSkuStockVO skuStockVO:skuStockVOS){
                Long skuId = skuStockVO.getId();
                List<PmsSkuStockAttributeValue> values = skuStockVO.getAttrs();
                for(PmsSkuStockAttributeValue value : values){
                    value.setSkuId(skuId);
                    skuStockAttributeValueMapper.insert(value);
                }
            }
        }

    }
    private <T>void relateAndInsertList(Object dao, List<T> list, Long productId)  {
        try {
            if (CollUtil.isEmpty(list)) return;
            //设置productId
            for (Object it : list) {
                Method setId = it.getClass().getDeclaredMethod("setId", Long.class);
                setId.invoke(it,(Long)null);
                Method setProductId = it.getClass().getDeclaredMethod("setProductId",Long.class);
                setProductId.invoke(it,productId);
            }
            Method insertList = dao.getClass().getDeclaredMethod("insertList", List.class);
            insertList.invoke(dao,list);

        }catch(Exception e){
            e.printStackTrace();
            logger.info("创建商品时出错:{}",e.getMessage());
        }
    }


    private void handSkucode(List<PmsSkuStockVO> list, Long productId){
        if(CollUtil.isEmpty(list)) return ;
        for(int i=0;i<list.size();i++){
            PmsSkuStock skuStock = list.get(i);
            if(!StringUtils.hasText(skuStock.getSkuCode())){
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                StringBuffer sb=new StringBuffer();
                sb.append(format.format(new Date()));
                sb.append(String.format("%04d",productId));
                sb.append(String.format("%03d",i+1));
                skuStock.setSkuCode(sb.toString());
            }
        }
    }

    @Resource
    private PmsProductDAO productDAO;
    public PmsProductResult getUpdateInfos(Long productId){
        PmsProductResult result = productDAO.getUpdateInfo(productId);
        List<PmsSkuStockVO> skuStockVOS = result.getSkuStockList();
        List<PmsSkuStockVO> newSkuStockVOs = new ArrayList<>();
        for(PmsSkuStock s : skuStockVOS){
            Long skuId = s.getId();
            //根据skuId去sku_stock_attribute_value表中寻找相关属性
            PmsSkuStockAttributeValueExample attributeValueExample = new PmsSkuStockAttributeValueExample();
            attributeValueExample.createCriteria().andSkuIdEqualTo(skuId);
            List<PmsSkuStockAttributeValue> attributeValues = skuStockAttributeValueMapper.selectByExampleWithBLOBs(attributeValueExample);
            PmsSkuStockVO skuStockVO = new PmsSkuStockVO();
            BeanUtils.copyProperties(s,skuStockVO);
            skuStockVO.setAttrs(attributeValues);
            newSkuStockVOs.add(skuStockVO);
        }
        result.setSkuStockList(newSkuStockVOs);
        return result;
    }


    public int update(Long id,PmsProductParam param){
        PmsProduct product = param;
        product.setId(id);
        productMapper.updateByPrimaryKey(product);

        //与商品相关的会员价格列表修改
        PmsMemberPriceExample memberPriceExample = new PmsMemberPriceExample();
        memberPriceExample.createCriteria().andProductIdEqualTo(id);
        memberPriceMapper.deleteByExample(memberPriceExample);
        relateAndInsertList(memberPriceDAO,param.getMemberPriceList(),id);


        //与商品相关的阶梯价格修改
        PmsProductLadderExample ladderExample = new PmsProductLadderExample();
        ladderExample.createCriteria().andProductIdEqualTo(id);
        ladderMapper.deleteByExample(ladderExample);
        relateAndInsertList(ladderDAO,param.getProductLadderList(),id);

        //与商品相关的满减价格列表修改
        PmsProductFullReductionExample fullReductionExample = new PmsProductFullReductionExample();
        fullReductionExample.createCriteria().andProductIdEqualTo(id);
        fullReductionMapper.deleteByExample(fullReductionExample);
        relateAndInsertList(fullReductionDAO,param.getProductFullReductionList(),id);

        //修改与商品相关的sku
        handleUpdateSkuStockList(id,param);

        //修改与商品有关的属性规格参数
        PmsProductAttributeValueExample valueExample =new PmsProductAttributeValueExample();
        valueExample.createCriteria().andProductIdEqualTo(id);
        productAttributeValueMapper.deleteByExample(valueExample);
        relateAndInsertList(productAttributeValueDAO,param.getProductAttributeValues(),id);


        //修改与商品有关的关联专题
        CmsSubjectProductRelationExample subjectProductRelationExample = new CmsSubjectProductRelationExample();
        subjectProductRelationExample.createCriteria().andProductIdEqualTo(id);
        subjectProductRelationMapper.deleteByExample(subjectProductRelationExample);
        relateAndInsertList(subjectProductRelationDAO,param.getSubjectProductRelations(),id);

        //修改与商品有关的关联偏好专区
        CmsPrefrenceAreaProductRelationExample prefrenceAreaProductRelationExample =new CmsPrefrenceAreaProductRelationExample();
        prefrenceAreaProductRelationExample.createCriteria().andProductIdEqualTo(id);
        prefrenceAreaProductRelationMapper.deleteByExample(prefrenceAreaProductRelationExample);
        relateAndInsertList(prefrenceAreaProductDAO,param.getPrefrenceAreaProductRelationList(),id);

        int count = 1;
        return count;
    }

    @Override
    public List<PmsProduct> batchGet(List<Long> ids) {
        PmsProductExample productExample = new PmsProductExample();
        productExample.createCriteria().andIdIn(ids);
        List<PmsProduct> products = productMapper.selectByExample(productExample);
        return products;
    }


    private void handleUpdateSkuStockList(Long id,PmsProductParam param){
        List<PmsSkuStockVO> newSkus = param.getSkuStockList();
        if(CollUtil.isEmpty(newSkus)){
            PmsSkuStockExample skuStockExample = new PmsSkuStockExample();
            skuStockExample.createCriteria().andProductIdEqualTo(id);
            skuStockMapper.deleteByExample(skuStockExample);
        }
        PmsSkuStockExample example = new PmsSkuStockExample();
        example.createCriteria().andProductIdEqualTo(id);
        List<PmsSkuStock> oriSkus = skuStockMapper.selectByExample(example);

        List<PmsSkuStockVO> insertSkus = newSkus.stream().filter(it->it.getId()==null).collect(Collectors.toList());

        List<PmsSkuStockVO> updateSkus = newSkus.stream().filter(it->it.getId()!=null).collect(Collectors.toList());
        List<Long> updateIds = updateSkus.stream().map(it->it.getId()).collect(Collectors.toList());;
        List<PmsSkuStock> removeSkus = newSkus.stream().filter(it->!updateIds.contains(it.getId())).collect(Collectors.toList());

        if(CollUtil.isNotEmpty(removeSkus)){
            //删除旧的sku
            List<Long> removeIds = removeSkus.stream().map(it->it.getId()).collect(Collectors.toList());
            PmsSkuStockExample example1 = new PmsSkuStockExample();
            example1.createCriteria().andIdIn(removeIds);
            skuStockMapper.deleteByExample(example1);
            PmsSkuStockAttributeValueExample attributeValueExample = new PmsSkuStockAttributeValueExample();
            attributeValueExample.createCriteria().andSkuIdIn(removeIds);
            skuStockAttributeValueMapper.deleteByExample(attributeValueExample);
        }
        handSkucode(insertSkus,id);
        handSkucode(updateSkus,id);
        if(CollUtil.isNotEmpty(insertSkus)){
            //插入新的sku
            relateAndInsertList(skuStockDAO,insertSkus,id);
            relateAttrValue(insertSkus);
        }
        if(CollUtil.isNotEmpty(updateSkus)){
            //修改之前存在的sku
            for(PmsSkuStock skuStock:updateSkus){
                skuStockMapper.updateByPrimaryKeySelective(skuStock);
            }

        }
    }



    public List<PmsProduct> list(PmsProductQueryParam param,Integer pageNum,Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        PmsProductExample example = new PmsProductExample();
        PmsProductExample.Criteria criteria = example.createCriteria();
        if(StringUtils.hasText(param.getKeyword())){
            criteria.andNameLike(param.getKeyword());
        }
        if(StringUtils.hasText(param.getProductSn())){
            criteria.andProductSnEqualTo(param.getProductSn());
        }
        if(param.getBrandId()!=null){
            criteria.andBrandIdEqualTo(param.getBrandId());
        }
        if(param.getCategoryId()!=null){
            criteria.andProductCategoryIdEqualTo(param.getCategoryId());
        }
        if(param.getPublicStatus()!=null){
            criteria.andPublishStatusEqualTo(param.getPublicStatus());
        }
        if(param.getVerifyStatus()!=null){
            criteria.andVerifyStatusEqualTo(param.getVerifyStatus());
        }
        return productMapper.selectByExample(example);
    }

    @Resource
    private PmsProductVerifyDAO verifyDAO;


    public int updateVerifyStatus(List<Long> ids,Integer verifyStatus,String details){
        PmsProduct product = new PmsProduct();
        product.setVerifyStatus(verifyStatus);
        PmsProductExample example = new PmsProductExample();
        example.createCriteria().andIdIn(ids);
        int count = productMapper.updateByExampleSelective(product,example);
        List<PmsProductVertifyRecord> list = new ArrayList<>();
        //插入审核记录
        for(Long id:ids){
            PmsProductVertifyRecord record = new PmsProductVertifyRecord();
            record.setStatus(verifyStatus);
            record.setProductId(id);
            record.setDetail(details);
            record.setCreateTime(new Date());
            record.setVertifyMan(SecurityContextHolder.getContext().getAuthentication().getName());
            list.add(record);
        }
        verifyDAO.insertList(list);
        return count;
    }


    public int updatePublishStatus(List<Long> ids,Integer status){
        PmsProduct product = new PmsProduct();
        product.setPublishStatus(status);
        PmsProductExample example = new PmsProductExample();
        example.createCriteria().andIdIn(ids);
        return productMapper.updateByExampleSelective(product,example);
    }

    public int updateRecommendStatus(List<Long> ids,Integer status){
        PmsProduct product = new PmsProduct();
        product.setRecommandStatus(status);
        PmsProductExample example = new PmsProductExample();
        example.createCriteria().andIdIn(ids);
        return productMapper.updateByExampleSelective(product,example);
    }


    public int updateNewStatus(List<Long> ids,Integer status){
        PmsProduct product = new PmsProduct();
        product.setNewStatus(status);
        PmsProductExample example = new PmsProductExample();
        example.createCriteria().andIdIn(ids);
        return productMapper.updateByExampleSelective(product,example);
    }

    public int updateDeleteStatus(List<Long> ids,Integer status){
        PmsProduct product = new PmsProduct();
        product.setDeleteStatus(status);
        PmsProductExample example = new PmsProductExample();
        example.createCriteria().andIdIn(ids);
        return productMapper.updateByExampleSelective(product,example);
    }

}
