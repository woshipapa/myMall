package com.papa.service;

import com.papa.dto.UmsMenuNode;
import com.papa.mbg.model.UmsMenu;

import java.util.List;

public interface UmsMenuService {

    public List<UmsMenuNode> treeNodeList();


    public List<UmsMenu> list(Long parentId,Integer pageNum,Integer pageSize);

    public int update(Long id,UmsMenu menu);


    public int delete(Long id);

    public UmsMenu getItem(Long id);
}
