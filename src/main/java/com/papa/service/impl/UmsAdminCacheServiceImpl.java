package com.papa.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.papa.dao.UmsAdminRoleRelationDAO;
import com.papa.mbg.mapper.UmsAdminRoleRelationMapper;
import com.papa.mbg.model.UmsAdmin;
import com.papa.mbg.model.UmsAdminRoleRelation;
import com.papa.mbg.model.UmsAdminRoleRelationExample;
import com.papa.service.RedisService;
import com.papa.service.UmsAdminCacheService;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

public class UmsAdminCacheServiceImpl implements UmsAdminCacheService {



    @Value("${redis.key.admin}")
    private String REDIS_KEY_ADMIN;

    @Value("${redis.key.resourceList}")
    private String REDIS_KEY_RESOURCE_LIST;

    @Value("${redis.database}")
    private String REDIS_DATABASE;




    @Resource
    private UmsAdminRoleRelationDAO adminRoleRelationDAO;

    @Resource
    private UmsAdminRoleRelationMapper adminRoleRelationMapper;

    @Resource
    private RedisService redisService;

    /**
     * 当资源修改时，要删除掉在redis中的相关用户的资源信息
     * @param resourceId
     */
    @Override
    public void delResourceListByResource(Long resourceId) {
        List<Long> adminIds = adminRoleRelationDAO.getAdminIdByResource(resourceId);
        if(CollUtil.isNotEmpty(adminIds)){
            String prefix = REDIS_DATABASE+":"+REDIS_KEY_RESOURCE_LIST+":";
            List<String> keys = adminIds.stream().map(adminId -> prefix+adminId).collect(Collectors.toList());
            redisService.del(keys);
        }
    }

    @Override
    public void delResourceListByRole(Long roleId) {
        UmsAdminRoleRelationExample example = new UmsAdminRoleRelationExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        List<UmsAdminRoleRelation> relations = adminRoleRelationMapper.selectByExample(example);
        if(CollUtil.isNotEmpty(relations)){
            String prefix = REDIS_DATABASE+":"+REDIS_KEY_RESOURCE_LIST+":";
            List<String> keys = relations.stream().map(admin ->prefix+admin.getAdminId()).collect(Collectors.toList());
            redisService.del(keys);
        }

    }

    @Override
    public void delResourceListByRoles(List<Long> roleIds) {
        UmsAdminRoleRelationExample example = new UmsAdminRoleRelationExample();
        example.createCriteria().andRoleIdIn(roleIds);
        List<UmsAdminRoleRelation> relations = adminRoleRelationMapper.selectByExample(example);
        if(CollUtil.isNotEmpty(relations)){
            String prefix = REDIS_DATABASE+":"+REDIS_KEY_RESOURCE_LIST+":";
            List<String> keys = relations.stream().map(relation ->prefix+relation.getAdminId()).collect(Collectors.toList());
            redisService.del(keys);
        }



    }


}
