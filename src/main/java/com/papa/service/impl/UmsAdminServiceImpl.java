package com.papa.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.github.pagehelper.PageHelper;

import com.papa.common.util.RequestUtil;
import com.papa.common.util.SpringUtil;
import com.papa.dao.UmsAdminRoleRelationDAO;
import com.papa.dao.UserAdminPermissionDAO;
import com.papa.dto.AdminParam;
import com.papa.dto.AdminUserDetails;
import com.papa.dto.UmsAdminPasswordParam;
import com.papa.mbg.mapper.UmsAdminLoginLogMapper;
import com.papa.mbg.mapper.UmsAdminMapper;
import com.papa.mbg.mapper.UmsAdminRoleRelationMapper;
import com.papa.mbg.mapper.UmsRoleMapper;
import com.papa.mbg.model.*;
import com.papa.security.util.JwtTokenUtil;
import com.papa.service.UmsAdminCacheService;
import com.papa.service.UmsAdminService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
@Service

public class UmsAdminServiceImpl implements UmsAdminService {


    @Resource
    private UmsAdminMapper umsAdminMapper;

    @Resource
    private UserAdminPermissionDAO dao;
//    @Resource
//    private UserDetailsService userDetailsService;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 根据用户名返回用户基本信息，
     * @param name
     * @return UmsAdmin
     */
    @Resource
    private PasswordEncoder passwordEncoder;
    @Override
    public UmsAdmin getAdminByName(String name) {
        UmsAdmin admin = getCache().getAdmin(name);
        if(admin!=null) return admin;
        UmsAdminExample example=new UmsAdminExample();
        UmsAdminExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(name);
        List<UmsAdmin> admins=umsAdminMapper.selectByExample(example);
        if(CollUtil.isNotEmpty(admins)){
            admin = admins.get(0);
            getCache().setAdmin(name,admin);
            return admin;
        }
        return null;
    }

    /**
     * 将用户信息保存到数据库中的用户表里
     * @param adminParam 控制层传来的用户信息参数
     * @return 返回保存的用户信息来判断是否注册成功
     */
    @Override
    public UmsAdmin register(AdminParam adminParam) {
        UmsAdmin umsAdmin=new UmsAdmin();
        BeanUtils.copyProperties(adminParam,umsAdmin);
        umsAdmin.setCreateTime(new Date());
        umsAdmin.setStatus(1);
        //不能重复注册相同的用户
        UmsAdminExample example=new UmsAdminExample();
        UmsAdminExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(umsAdmin.getUsername());
        List<UmsAdmin> umsAdmins=umsAdminMapper.selectByExample(example);
        if(umsAdmins!=null&&umsAdmins.size()>0) return null;

        //密码要加密存储在数据库中
        String encode = passwordEncoder.encode(umsAdmin.getPassword());
        umsAdmin.setPassword(encode);
        umsAdminMapper.insertSelective(umsAdmin);
        return umsAdmin;


    }

    /**
     *
     * @param userName
     * @param password
     * @return 返回值不是null就是token，控制层可以进行返回到客户端
     */

    @Override
    public String login(String userName, String password) {
        if(StringUtils.hasText(userName)&&StringUtils.hasText(password)){
            UserDetails userDetails= loadUserDetailsByUserName(userName);
            //验证密码
            if(!passwordEncoder.matches(password,userDetails.getPassword())){
                throw new BadCredentialsException("密码错误");
            }
            UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            String token = jwtTokenUtil.generateToken(userDetails);

            insertLoginLog(userName);
            return token;
        }
        return null;
    }

    /**
     * 登陆时调用，插入用户的登陆记录
     */
    @Resource
    private UmsAdminLoginLogMapper mapper;

    private void insertLoginLog(String userName){
        UmsAdmin admin=getAdminByName(userName);
        UmsAdminLoginLog log=new UmsAdminLoginLog();
        log.setAdminId(admin.getId());
        log.setCreateTime(new Date());
        ServletRequestAttributes servletRequestAttributes =(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request=servletRequestAttributes.getRequest();
        log.setIp(RequestUtil.getRequestIp(request));
        log.setUserAgent(RequestUtil.getBrowser(request));
        mapper.insertSelective(log);
    }


    @Override
    public List<UmsPermission> getPermissonsByAdminId(Long id) {
        return dao.getPermissionList(id);
    }

    @Override
    public List<UmsAdmin> list(String keyword, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        UmsAdminExample umsAdminExample=new UmsAdminExample();
        //如果为没给关键字，就可以认为是查询全部了
        if(StringUtils.hasText(keyword)) {
            umsAdminExample.createCriteria().andUsernameLike("%" + keyword + "%");
            umsAdminExample.or(umsAdminExample.createCriteria().andNickNameLike("%"+keyword+"%"));
        }
        return umsAdminMapper.selectByExample(umsAdminExample);
    }

    @Override
    public UmsAdmin getItem(Long id) {
        return umsAdminMapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(Long id, UmsAdmin newAdmin) {
        newAdmin.setId(id);
        UmsAdmin admin=getItem(id);
        if(passwordEncoder.matches(newAdmin.getPassword(), admin.getPassword())||newAdmin.getPassword()==null){
            newAdmin.setPassword(null);//不需要变动
        }else{
            newAdmin.setPassword(passwordEncoder.encode(newAdmin.getPassword()));
        }
        return umsAdminMapper.updateByPrimaryKeySelective(newAdmin);
    }

    @Override
    public int updatePassword(UmsAdminPasswordParam param) {
        String userName = param.getUserName();
        String rawPassword = param.getRawPassword();
        String newPassword = param.getNewPassword();
        if(StringUtils.isEmpty(userName)||StringUtils.isEmpty(rawPassword)||StringUtils.isEmpty(newPassword)){
            //填写有为空
            return -1;
        }
        UmsAdminExample example=new UmsAdminExample();
        example.createCriteria().andUsernameEqualTo(userName);
        List<UmsAdmin> adminList = umsAdminMapper.selectByExample(example);
        if(adminList==null){
            //用户不存在
            return -2;
        }
        UmsAdmin admin=adminList.get(0);
        if(!passwordEncoder.matches(rawPassword, admin.getPassword()))
        {
            //原始密码不正确
            return -3;
        }
        admin.setPassword(passwordEncoder.encode(newPassword));
        umsAdminMapper.updateByPrimaryKeySelective(admin);
        return 1;
    }

    @Override
    public int delete(Long id) {
        int count = umsAdminMapper.deleteByPrimaryKey(id);
        getCache().delResourceListByAdmin(id);
        return count;
    }
    @Resource
    private UmsAdminRoleRelationMapper adminRoleRelationMapper;
    @Resource
    private UmsAdminRoleRelationDAO umsAdminRoleRelationDAO;
    @Resource
    private UmsRoleMapper roleMapper;

    @Override
    public int allocRoles(Long adminID, List<Long> roleIds) {
        UmsAdminRoleRelationExample example=new UmsAdminRoleRelationExample();
        UmsAdminRoleRelationExample.Criteria criteria = example.createCriteria();
        criteria.andAdminIdEqualTo(adminID);
        List<UmsAdminRoleRelation> oldRelations=adminRoleRelationMapper.selectByExample(example);
        int delAffects=adminRoleRelationMapper.deleteByExample(example);
        if(delAffects>0){
            List<Long> oldRoleIds=new ArrayList<>();
            for(UmsAdminRoleRelation relation:oldRelations){
                oldRoleIds.add(relation.getRoleId());
            }
            umsAdminRoleRelationDAO.updateAdminCountInRole(oldRoleIds,"sub");
        }
        List<UmsAdminRoleRelation> relations=new ArrayList<>();
        for(Long id:roleIds){
            UmsAdminRoleRelation relation=new UmsAdminRoleRelation();
            relation.setAdminId(adminID);
            relation.setRoleId(id);
            relations.add(relation);
        }
        int affects=umsAdminRoleRelationDAO.insertRoles(relations);
        if(affects>0){
            //更新一下每个角色下拥有的用户数
            umsAdminRoleRelationDAO.updateAdminCountInRole(roleIds,"add");
        }
        getCache().delResourceListByAdmin(adminID);
        return affects;
    }



    @Override
    public List<UmsRole> getRoles(Long adminId) {
        return umsAdminRoleRelationDAO.getRoles(adminId);
    }


    @Override
    public List<UmsMenu> getMenusByAdmin(Long adminId) {
        return umsAdminRoleRelationDAO.getMenusByAdmin(adminId);
    }

    @Override
    public List<UmsResource> getResourcesByAdmin(Long adminId) {
        List<UmsResource> resourceList = getCache().getResourceList(adminId);
        if(CollUtil.isNotEmpty(resourceList)) return resourceList;
        resourceList = umsAdminRoleRelationDAO.getResourcesByAdmin(adminId);
        if(CollUtil.isNotEmpty(resourceList))
            getCache().setResourceList(adminId,resourceList);
        return resourceList;
    }

    @Override
    public UserDetails loadUserDetailsByUserName(String userName) {
        UmsAdmin admin = getAdminByName(userName);
        if(admin!=null){
            List<UmsResource> resources = getResourcesByAdmin(admin.getId());
            return new AdminUserDetails(admin,resources);
        }
        throw new UsernameNotFoundException("用户名或者密码错误");
    }


    private UmsAdminCacheService getCache(){
        return  SpringUtil.getBean(UmsAdminCacheService.class);
    }
}
