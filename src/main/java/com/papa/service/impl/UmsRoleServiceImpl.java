package com.papa.service.impl;

import com.github.pagehelper.PageHelper;
import com.papa.dao.UmsRoleDAO;
import com.papa.mbg.mapper.UmsRoleMapper;
import com.papa.mbg.mapper.UmsRoleMenuRelationMapper;
import com.papa.mbg.mapper.UmsRoleResourceRelationMapper;
import com.papa.mbg.model.*;
import com.papa.service.UmsRoleService;
import org.apache.ibatis.annotations.Result;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
@Service
public class UmsRoleServiceImpl implements UmsRoleService {
    @Resource
    private UmsRoleMapper roleMapper;
    @Override
    public List<UmsRole> list(String keyword, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        UmsRoleExample example=new UmsRoleExample();
        UmsRoleExample.Criteria criteria=example.createCriteria();
        if(StringUtils.hasText(keyword)){
            criteria.andNameLike(keyword);
        }
        return roleMapper.selectByExample(example);
    }

    @Override
    public List<UmsRole> listAll() {
        return roleMapper.selectByExample(new UmsRoleExample());
    }

    @Override
    public int create(UmsRole role) {
        role.setCreateTime(new Date());
        role.setSort(0);
        role.setAdminCount(0);
        return roleMapper.insertSelective(role);
    }

    @Override
    public int update(Long id, UmsRole role) {
        role.setId(id);
        return roleMapper.updateByPrimaryKeySelective(role);
    }

    @Override
    public int delete(Long id) {
        return roleMapper.deleteByPrimaryKey(id);

    }

    @Resource
    private UmsRoleMenuRelationMapper roleMenuRelationMapper;

    @Override
    public int allocMenus(Long roleId, List<Long> menuIds) {
        //删除关系表中原有的关系
        UmsRoleMenuRelationExample example=new UmsRoleMenuRelationExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        roleMenuRelationMapper.deleteByExample(example);
        //这里一条一条插入
        for(Long menuId:menuIds){
            UmsRoleMenuRelation relation=new UmsRoleMenuRelation();
            relation.setRoleId(roleId);
            relation.setMenuId(menuId);
            roleMenuRelationMapper.insertSelective(relation);
        }
        return menuIds.size();

    }

    @Resource
    private UmsRoleResourceRelationMapper roleResourceRelationMapper;
    @Override
    public int allocResources(Long roleId, List<Long> resourceIds) {
        UmsRoleResourceRelationExample example=new UmsRoleResourceRelationExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        roleResourceRelationMapper.deleteByExample(example);

        for(Long resourceId:resourceIds){
            UmsRoleResourceRelation relation=new UmsRoleResourceRelation();
            relation.setRoleId(roleId);
            relation.setResourceId(resourceId);
            roleResourceRelationMapper.insertSelective(relation);
        }
        return resourceIds.size();
    }
    @Resource
    private UmsRoleDAO roleDAO;

    @Override
    public List<UmsMenu> getMenus(Long roleId) {
        return roleDAO.getMenus(roleId);
    }

    @Override
    public List<UmsResource> getResources(Long roleId) {
        return roleDAO.getResources(roleId);
    }
}
