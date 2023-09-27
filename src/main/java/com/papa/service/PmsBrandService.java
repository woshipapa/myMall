package com.papa.service;

import com.papa.mbg.model.PmsBrand;
import io.swagger.models.auth.In;

import java.util.List;

public interface PmsBrandService {

    List<PmsBrand> listAllBrand();

    int createBrand(PmsBrand pmsBrand);

    int deleteBrand(Long id);

    int updateBrand(Long id,PmsBrand pmsBrand);


    PmsBrand getBrand(Long id);

    //分页查询
    List<PmsBrand> listBrand(int pageNum,int pageSize);

}
