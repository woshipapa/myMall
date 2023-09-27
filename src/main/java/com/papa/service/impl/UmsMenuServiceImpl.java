package com.papa.service.impl;

import com.github.pagehelper.PageHelper;
import com.papa.dto.UmsMenuNode;
import com.papa.mbg.mapper.UmsMenuMapper;
import com.papa.mbg.model.UmsMenu;
import com.papa.mbg.model.UmsMenuExample;
import com.papa.service.UmsMenuService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class UmsMenuServiceImpl implements UmsMenuService {
    @Resource
    private UmsMenuMapper menuMapper;
    @Override
    public List<UmsMenuNode> treeNodeList() {
        //查询出所有菜单，在进行筛选
        UmsMenuExample example=new UmsMenuExample();
        List<UmsMenu> menus=menuMapper.selectByExample(example);

        return menus.stream().filter(parentMenu->parentMenu.getParentId()==0L)
                .map(parentMenu->convertMenuNode(parentMenu,menus))
                .collect(Collectors.toList());
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
    public int delete(Long id) {
        return menuMapper.deleteByPrimaryKey(id);
    }

    @Override
    public UmsMenu getItem(Long id) {
        return menuMapper.selectByPrimaryKey(id);
    }

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
