package com.papa.nosql.es.repository;

import com.papa.nosql.es.document.EsProduct;
import io.swagger.models.auth.In;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EsProductRepository  extends ElasticsearchRepository<EsProduct, Integer> {


    Page<EsProduct> findEsProductByNameOrSubTitleOrKeywords(String name, String subTitle, String keywords, Pageable page);
}
