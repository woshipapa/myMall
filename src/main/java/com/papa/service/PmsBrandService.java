package com.papa.service;

import com.papa.dto.PmsBrandParam;
import com.papa.mbg.model.PmsBrand;

import java.util.List;

public interface PmsBrandService {

    List<PmsBrand> listAllBrand();

    List<PmsBrand> listBrandListInIds(List<Long> ids);

    int createBrand(PmsBrandParam pmsBrand);

    int deleteBrand(Long id);

    int updateBrand(Long id,PmsBrandParam pmsBrand);


    PmsBrand getBrand(Long id);

    //分页查询
    List<PmsBrand> listBrand(String keyword,int pageNum,int pageSize);

    int updateShowStatus(List<Long> id,Integer status);

    int updateFactoryStatus(List<Long> id,Integer status);



}
