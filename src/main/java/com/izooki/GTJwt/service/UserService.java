package com.izooki.GTJwt.service;

import com.izooki.GTJwt.entity.Role;
import com.izooki.GTJwt.entity.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);

    Role saveRole(Role role);

    void addRoleToUser(String username, String roleName);

    User getUser(String username);
    User findByUserId(long id);
    List<User> getUsers();
    User updateUser(User user, long id);
    void deleteUser(long id);
}
