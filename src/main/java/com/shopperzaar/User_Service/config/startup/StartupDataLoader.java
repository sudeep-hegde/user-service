package com.shopperzaar.User_Service.config.startup;

import com.shopperzaar.User_Service.domain.Roles;
import com.shopperzaar.User_Service.domain.UserRole;
import com.shopperzaar.User_Service.exception.AppException;
import com.shopperzaar.User_Service.repository.RolesRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Not working? need to check
 */
@Component
@RequiredArgsConstructor
public class StartupDataLoader {
    private final RolesRepository rolesRepository;

    @PostConstruct
    @Transactional
    public void loadData() {
        if (rolesRepository.count() == 0) {
            Roles adminRole = rolesRepository.findByRole(UserRole.ADMIN)
                    .orElseThrow(() -> new AppException("role not found", HttpStatus.NOT_FOUND));
            Roles userRole = rolesRepository.findByRole(UserRole.USER)
                    .orElseThrow(() -> new AppException("role not found", HttpStatus.NOT_FOUND));

            if (adminRole == null) {
                adminRole = new Roles();
                adminRole.setRole(UserRole.ADMIN);
                adminRole.setDescription("default admin role");
                rolesRepository.save(adminRole);
            }

            if (userRole == null) {
                userRole = new Roles();
                userRole.setRole(UserRole.USER);
                userRole.setDescription("default user role");
                rolesRepository.save(userRole);
            }
        }
    }

}
