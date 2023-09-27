package com.papa.service;

import com.papa.nosql.es.document.EsProduct;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EsProductService {

    public int importAll();

    public void delete(Integer id);

    public EsProduct create(Integer id);

    public void delete(List<Integer> ids);

    public Page<EsProduct> search(String keyword, Integer pageNum, Integer pageSize);
}
