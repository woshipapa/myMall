package com.papa.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.papa.dao.UmsAdminRoleRelationDAO;
import com.papa.mbg.mapper.UmsAdminRoleRelationMapper;
import com.papa.mbg.model.UmsAdmin;
import com.papa.mbg.model.UmsAdminRoleRelation;
import com.papa.mbg.model.UmsAdminRoleRelationExample;
import com.papa.mbg.model.UmsResource;
import com.papa.service.RedisService;
import com.papa.service.UmsAdminCacheService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class UmsAdminCacheServiceImpl implements UmsAdminCacheService {



    @Value("${redis.key.admin}")
    private String REDIS_KEY_ADMIN;

    @Value("${redis.key.resourceList}")
    private String REDIS_KEY_RESOURCE_LIST;

    @Value("${redis.database}")
    private String REDIS_DATABASE;


    @Value("${redis.expire.common}")
    private Long expire;


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

    /**
     * 以下俩个是当相关用户的角色改变(角色分配的资源改变)时，要对redis缓存中的用户资源信息删除
     * @param roleId
     */
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

    /**
     * 当用户分配的角色改变时，对缓存修改
     * @param adminId
     */

    @Override
    public void delResourceListByAdmin(Long adminId) {
        String prefix = REDIS_DATABASE+":"+REDIS_KEY_RESOURCE_LIST+":";
        redisService.del(prefix+adminId);
    }



    public void setResourceList(Long adminId,List<UmsResource> resources){
        String key = REDIS_DATABASE+":"+REDIS_KEY_RESOURCE_LIST+":"+adminId;
        redisService.set(key,resources,expire);
    }

    public List<UmsResource> getResourceList(Long adminId){
        return (List<UmsResource>)redisService.get(REDIS_DATABASE+":"+REDIS_KEY_RESOURCE_LIST+":"+adminId);
    }

    @Override
    public void setAdmin(String username,UmsAdmin umsAdmin) {
        String key = REDIS_DATABASE+":"+REDIS_KEY_ADMIN+":"+username;
        redisService.set(key,umsAdmin,expire);
    }

    @Override
    public UmsAdmin getAdmin(String username) {
        return (UmsAdmin) redisService.get(REDIS_DATABASE+":"+REDIS_KEY_ADMIN+":"+username);
    }

}
