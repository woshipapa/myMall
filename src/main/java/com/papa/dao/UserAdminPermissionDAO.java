package com.papa.dao;

import com.papa.mbg.model.UmsPermission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserAdminPermissionDAO {
    public List<UmsPermission> getPermissionList(Long id);
}
