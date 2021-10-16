package com.buka.dao;

import com.buka.pojo.User;

public interface UserDao {


     User findUserByName(String userName);
}
