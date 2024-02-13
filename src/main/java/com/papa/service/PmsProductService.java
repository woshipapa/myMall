package com.papa.service;

import com.papa.dto.PmsProductParam;
import com.papa.dto.PmsProductQueryParam;
import com.papa.dto.PmsProductResult;
import com.papa.mbg.model.PmsProduct;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PmsProductService {

    @Transactional
    public int create(PmsProductParam productParam);

    @Transactional
    public int update(Long id,PmsProductParam param);

    public List<PmsProduct> batchGet(List<Long> ids);
    public List<PmsProduct> list(PmsProductQueryParam param,Integer pageNum,Integer pageSize);

    public PmsProductResult getUpdateInfos(Long id);

    @Transactional
    public int updateVerifyStatus(List<Long> ids,Integer status,String details);

    public int updatePublishStatus(List<Long> ids,Integer status);


    public int updateRecommendStatus(List<Long> ids,Integer status);

    public int updateDeleteStatus(List<Long> ids,Integer status);

    public int updateNewStatus(List<Long> ids,Integer status);
}
