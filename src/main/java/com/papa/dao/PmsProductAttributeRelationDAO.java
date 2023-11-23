package com.papa.dao;

import java.util.List;

public interface PmsProductAttributeRelationDAO {
    public List<Long> getParentPath(Long categoryId);

    public int setMaxRecursionDepth();
}
