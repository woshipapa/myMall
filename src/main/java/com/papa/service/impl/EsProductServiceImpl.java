package com.papa.service.impl;

import com.papa.dao.EsProductDao;
import com.papa.nosql.es.document.EsProduct;
import com.papa.nosql.es.repository.EsProductRepository;
import com.papa.service.EsProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
@Service
public class EsProductServiceImpl implements EsProductService {
    //操纵mysql数据库
    @Resource
    public EsProductDao esProductDao;

    //操纵es
    @Resource
    public EsProductRepository repository;

    /**
     * 导入数据库中的商品信息到ES中存储
     * @return
     */
    @Override
    public int importAll() {
        List<EsProduct> productList=esProductDao.getAllEsProductList(null);
        Iterable<EsProduct> esProducts = repository.saveAll(productList);
        Iterator<EsProduct> iterator=esProducts.iterator();
        int result=0;
        while(iterator.hasNext()){
            result++;
            iterator.next();
        }
        return result;
    }

    @Override
    public void delete(Integer id) {
         repository.deleteById(id);
    }

    @Override
    public EsProduct create(Integer id) {
        List<EsProduct> esProducts=esProductDao.getAllEsProductList(id);
        EsProduct result=null;
        if(esProducts.size()>0){
            EsProduct product=esProducts.get(0);
            result = repository.save(product);
        }
        return result;
    }

    public void delete(List<Integer> ids){
        if(!CollectionUtils.isEmpty(ids)){
            List<EsProduct> esProducts=new ArrayList<>();
            for(Integer id:ids){
                EsProduct product=new EsProduct();
                product.setId(id);
                esProducts.add(product);
            }
            repository.deleteAll(esProducts);
        }
    }

    public Page<EsProduct> search(String keyword,Integer pageNum,Integer pageSize){
        Pageable pageable= PageRequest.of(pageNum,pageSize);
        return repository.findEsProductByNameOrSubTitleOrKeywords(keyword,keyword,keyword,pageable);
    }
}
