package com.izooki.GTJwt.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
@Table(name = "user", uniqueConstraints = @UniqueConstraint( columnNames = "username"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotEmpty(message = "Name cannot be empty")
    private String name;
    @NotEmpty(message = "User name cannot be empty")
    private String username;
    @NotEmpty(message = "Password cannot be empty")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable (
            name = "user_roles",
            joinColumns = @JoinColumn (
                    name = "userId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn (
                    name = "roleId", referencedColumnName = "id"
            )
    )
    private Set<Role> roles = new HashSet<>();

}
