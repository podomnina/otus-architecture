package ru.otus.auth.service.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_role", schema = "auth")
public class UserRole {
    @EmbeddedId
    private UserRoleId id;
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}
