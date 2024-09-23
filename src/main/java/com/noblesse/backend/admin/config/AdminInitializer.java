package com.noblesse.backend.admin.config;

import com.noblesse.backend.admin.dto.AdminDTO;
import com.noblesse.backend.admin.entity.Admin;
import com.noblesse.backend.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (adminRepository.findByEmail("admin@gmail.com") == null) {
            AdminDTO adminDTO = AdminDTO.of(
                    "admin@gmail.com",
                    passwordEncoder.encode("admin"),
                    "ROLE_ADMIN"
            );
            Admin admin = Admin.from(adminDTO);
            adminRepository.save(admin);
        }
    }
}
