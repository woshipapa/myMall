package com.papa.nosql.mongodb.repository;

import com.papa.nosql.mongodb.document.MemberReadHistory;
import io.swagger.models.auth.In;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MemberReadHistoryRepository extends MongoRepository<MemberReadHistory,String> {

    public List<MemberReadHistory> findMemberReadHistoriesByMemberIdOrderByCreateTimeDesc(Integer id);
}
