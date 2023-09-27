package com.papa.service.impl;

import com.papa.nosql.mongodb.document.MemberReadHistory;
import com.papa.nosql.mongodb.repository.MemberReadHistoryRepository;
import com.papa.service.MemberReadHistoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class MemberReadHistoryServiceImpl implements MemberReadHistoryService {

    @Resource
    public MemberReadHistoryRepository repository;

    @Override
    public void create(MemberReadHistory memberReadHistory) {
        memberReadHistory.setId(null);
        memberReadHistory.setCreateTime(new Date());
        repository.save(memberReadHistory);
    }

    @Override
    public int delete(List<String> ids) {
        List<MemberReadHistory> list=new ArrayList<>();
        for(String id:ids){
            MemberReadHistory memberReadHistory=new MemberReadHistory();
            memberReadHistory.setId(id);
            list.add(memberReadHistory);
        }
        repository.deleteAll(list);
        return ids.size();
    }

    @Override
    public List<MemberReadHistory> list(Integer memberId) {
        return repository.findMemberReadHistoriesByMemberIdOrderByCreateTimeDesc(memberId);
    }
}
