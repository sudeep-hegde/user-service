package com.shopperzaar.User_Service.repository;


import com.shopperzaar.User_Service.domain.Roles;
import com.shopperzaar.User_Service.domain.User;
import com.shopperzaar.User_Service.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<Roles, Long> {

    Optional<Roles>  findByRole(UserRole role);
}
