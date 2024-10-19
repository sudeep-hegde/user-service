package com.shopperzaar.User_Service.repository;

import com.shopperzaar.User_Service.domain.Roles;
import com.shopperzaar.User_Service.domain.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRolesRepository extends JpaRepository<UserRoles, Long> {
}
