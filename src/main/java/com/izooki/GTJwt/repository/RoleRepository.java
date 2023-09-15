package com.izooki.GTJwt.repository;

import com.izooki.GTJwt.entity.Role;
import com.izooki.GTJwt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String roleName);
}
