package com.papa.dao;

import com.papa.mbg.model.UmsMenu;

import java.util.List;

public interface UmsMenuDAO {
    public List<UmsMenu> listChildren();

    public void updateChildren(Long menuId);
    public int deleteParent(Long menuId);

}
