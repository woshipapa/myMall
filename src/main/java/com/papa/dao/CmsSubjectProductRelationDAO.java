package com.papa.dao;

import com.papa.mbg.model.CmsSubjectProductRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CmsSubjectProductRelationDAO {

    public int insertList(@Param("list") List<CmsSubjectProductRelation> relationList);

}
