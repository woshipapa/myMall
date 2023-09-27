package com.papa.service;

import com.papa.nosql.mongodb.document.MemberReadHistory;

import java.util.List;

public interface MemberReadHistoryService {

    /**
     * 创建用户的浏览历史信息记录
     * @param memberReadHistory
     * @return
     */
    public void create(MemberReadHistory memberReadHistory);

    /**
     * 根据浏览记录的id来批量删除，这个id对应的mongodb生成的id
     * @param ids
     * @return
     */
    public int delete(List<String> ids);

    /**
     *得到指定会员id的浏览历史记录
     * @param id
     * @return
     */
    public List<MemberReadHistory> list(Integer id);
}
