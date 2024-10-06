package com.noblesse.backend.admin.config;

import com.noblesse.backend.admin.dto.AdminDTO;
import com.noblesse.backend.admin.entity.Admin;
import com.noblesse.backend.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Checking for default admin account...");

        String defaultAdminEmail = "admin@triplay.com";
        String defaultAdminPassword = "admin";

        Optional<Admin> existingAdmin = adminRepository.findByEmail(defaultAdminEmail);

        if (existingAdmin.isEmpty()) {
            log.info("Default admin account not found. Creating...");

            AdminDTO adminDTO = AdminDTO.of(
                    defaultAdminEmail,
                    passwordEncoder.encode(defaultAdminPassword),
                    "ROLE_ADMIN"
            );
            Admin admin = Admin.from(adminDTO);

            try {
                adminRepository.save(admin);
                log.info("Default admin account created successfully.");
            } catch (Exception e) {
                log.error("Error creating default admin account: ", e);
            }
        } else {
            log.info("Default admin account already exists. Skipping creation.");
        }
    }
}