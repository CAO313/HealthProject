package com.buka.dao;

import com.buka.pojo.Permission;
import com.buka.pojo.Role;

import java.util.Set;

public interface PermissionDao {
    Set<Permission> findByRoleId(Integer roleId);
}
