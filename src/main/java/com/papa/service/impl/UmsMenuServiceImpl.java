package com.papa.service.impl;

import com.github.pagehelper.PageHelper;
import com.papa.dao.UmsMenuDAO;
import com.papa.dto.UmsMenuNode;
import com.papa.mbg.mapper.UmsMenuMapper;
import com.papa.mbg.model.UmsMenu;
import com.papa.mbg.model.UmsMenuExample;
import com.papa.service.UmsMenuService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class UmsMenuServiceImpl implements UmsMenuService {
    @Resource
    private UmsMenuMapper menuMapper;
    @Resource
    private UmsMenuDAO menuDAO;
    @Override
    public List<UmsMenuNode> treeNodeList() {
        //查询出所有菜单，在进行筛选
        UmsMenuExample example=new UmsMenuExample();
        List<UmsMenu> menus=menuMapper.selectByExample(example);

        return menus.stream().filter(parentMenu->parentMenu.getParentId()==0L)
                .map(parentMenu->convertMenuNode(parentMenu,menus))
                .collect(Collectors.toList());
    }

    private int updateLevel(UmsMenu menu){
        int height=0;
        while(menu.getParentId()!=0){
            height++;
            UmsMenuExample example=new UmsMenuExample();
            example.createCriteria().andParentIdEqualTo(menu.getParentId());
            menu=menuMapper.selectByExample(example).get(0);
        }
        return height;
    }


    @Override
    public int create(UmsMenu menu) {
        menu.setCreateTime(new Date());
        menu.setLevel(updateLevel(menu));
        return menuMapper.insertSelective(menu);
    }

    /**
     * 数据列表和查看下级都是此方法
     * @param parentId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public List<UmsMenu> list(Long parentId,Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        UmsMenuExample example=new UmsMenuExample();
        example.createCriteria().andParentIdEqualTo(parentId);
        return menuMapper.selectByExample(example);
    }


    @Override
    public int update(Long id, UmsMenu menu) {
        menu.setId(id);
        return menuMapper.updateByPrimaryKeySelective(menu);
    }



    @Override
    public int delete(Long menuId) {
//        return menuMapper.deleteByPrimaryKey(id);
        //先将子菜单指向被删除菜单的父菜单，在进行删除
        menuDAO.updateChildren(menuId);
        return menuDAO.deleteParent(menuId);
    }


    @Override
    public UmsMenu getItem(Long id) {
        return menuMapper.selectByPrimaryKey(id);
    }

//    @Override
//    public int updateStatus(Long id, UmsMenu menu) {
//
//    }

    private UmsMenuNode convertMenuNode(UmsMenu parentMenu,List<UmsMenu> menus){
        UmsMenuNode menuNode=new UmsMenuNode();
        BeanUtils.copyProperties(parentMenu,menuNode);
        //遍历所有节点，其父节点就是当前的父节点
        List<UmsMenuNode> childList = menus.stream().filter(eachMenu -> eachMenu.getParentId() == parentMenu.getId())
                .map(childMenu -> convertMenuNode(childMenu, menus))
                .collect(Collectors.toList());
        menuNode.setChildMenus(childList);
        return menuNode;
    }




}
