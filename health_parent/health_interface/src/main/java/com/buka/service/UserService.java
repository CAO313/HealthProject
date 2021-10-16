package com.buka.service;

import com.buka.pojo.User;

public interface UserService {
    User findByUserName(String userName);
}
