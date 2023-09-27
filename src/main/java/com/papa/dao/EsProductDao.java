package com.papa.dao;

import com.papa.nosql.es.document.EsProduct;

import java.util.List;

public interface EsProductDao {
    List<EsProduct>  getAllEsProductList(Integer id);
}
