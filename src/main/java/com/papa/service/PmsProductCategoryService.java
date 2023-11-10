package com.papa.service;

import com.papa.dto.PmsProductCategoryParam;
import com.papa.dto.PmsProductCategoryWithChildrenItem;
import com.papa.mbg.model.PmsProductCategory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PmsProductCategoryService {

    public List<PmsProductCategoryWithChildrenItem>  listWithChildren();

    public List<PmsProductCategory> list(Long parentId,Integer pageNum,Integer pageSize);

    public PmsProductCategory getItem(Long id);

    @Transactional
    public int update(Long categoryId,PmsProductCategoryParam category);

    @Transactional
    public int delete(Long categoryId);

    @Transactional
    public int create(PmsProductCategoryParam param);

    public int updateNavStatus(List<Long> ids,Integer status);

    public int updateShowStatus(List<Long> ids,Integer status);

    public boolean isParent(Long id);
}
