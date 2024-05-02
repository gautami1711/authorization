package com.gautami.authorization.Repository;

import com.gautami.authorization.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findRoleByRoleName(Role.RoleName roleName);
}
