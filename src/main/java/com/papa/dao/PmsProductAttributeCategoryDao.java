package com.papa.dao;

import com.papa.dto.PmsProductAttributeCategoryItem;

import java.util.List;

public interface PmsProductAttributeCategoryDao {

    List<PmsProductAttributeCategoryItem> getListWithAttr();
}
