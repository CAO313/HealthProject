package com.buka.service;


import com.alibaba.dubbo.config.annotation.Reference;
import com.buka.pojo.Permission;
import com.buka.pojo.Role;
import com.buka.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SpringSecurityUserService implements UserDetailsService {
   @Reference
   private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userService.findByUserName(userName);
        if(user == null){
            return null;
        }
        List<GrantedAuthority> list = new ArrayList();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            list.add( new SimpleGrantedAuthority(role.getKeyword()));
            Set<Permission> permissions =  role.getPermissions();
            for (Permission permission : permissions) {
                list.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }
        return new org.springframework.security.core.userdetails.User(userName,user.getPassword(),list);
    }
}
