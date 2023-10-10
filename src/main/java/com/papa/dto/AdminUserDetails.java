package com.papa.dto;

import com.papa.mbg.model.UmsAdmin;
import com.papa.mbg.model.UmsPermission;
import com.papa.mbg.model.UmsResource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AdminUserDetails implements UserDetails {
    private UmsAdmin admin;

    public AdminUserDetails(UmsAdmin admin, List<UmsResource> resourceList) {
        this.admin = admin;
//        this.permissionList = permissionList;
        this.resourceList=resourceList;
    }

//    private List<UmsPermission> permissionList;

    private List<UmsResource> resourceList;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return permissionList.stream().filter(p->p.getValue()!=null)
//                .map(p->new SimpleGrantedAuthority(p.getValue()))
//                .collect(Collectors.toList());
        return resourceList.stream().filter(resource -> resource.getUrl()!=null)
                .map(resource -> new SimpleGrantedAuthority(resource.getId()+":"+resource.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return admin.getPassword();
    }

    @Override
    public String getUsername() {
        return admin.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
