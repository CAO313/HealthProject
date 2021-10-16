package com.buka.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.buka.dao.PermissionDao;
import com.buka.dao.RoleDao;
import com.buka.dao.UserDao;
import com.buka.pojo.Permission;
import com.buka.pojo.Role;
import com.buka.pojo.User;
import com.buka.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;


    @Override
    public User findByUserName(String userName) {
        User user = userDao.findUserByName(userName);
        if(user==null){
            return null;
        }
        Integer userId = user.getId();
        Set<Role> roles = roleDao.findByUserId(userId);

        for (Role role : roles) {
            Integer roleId = role.getId();
            Set<Permission> permissions =  permissionDao.findByRoleId(roleId);
            role.setPermissions(permissions);
        }
        user.setRoles(roles);
        return user;
    }
}
