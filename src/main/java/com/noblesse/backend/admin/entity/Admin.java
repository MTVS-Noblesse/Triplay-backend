package com.noblesse.backend.admin.entity;

import com.noblesse.backend.admin.dto.AdminDTO;
import jakarta.persistence.*;

@Entity(name = "Admin")
@Table(name = "admin")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ADMIN_ID")
    private Long adminId;

    @Column(name = "EMAIL", unique = true)
    private String email;

    @Column(name = "PASSWOWRD")
    private String password;

    @Column(name = "ROLE")
    private String role;

    protected Admin() {}

    public Admin(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // DTO를 엔티티로 변환
    public static Admin from(AdminDTO dto) {
        return new Admin(dto.getEmail(), dto.getPassword(), dto.getRole());
    }

    public Long getAdminId() {
        return adminId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
