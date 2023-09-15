package com.izooki.GTJwt.utils;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class RoleToUserForm {
    private String username;
    private String roleName;
}
