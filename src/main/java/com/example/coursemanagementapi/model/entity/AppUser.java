package com.example.coursemanagementapi.model.entity;

import com.example.coursemanagementapi.model.dto.AppUserDTO;
import com.example.coursemanagementapi.model.enums.Role;
import com.example.coursemanagementapi.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "app_users")
public class AppUser extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "full_name")
    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "app_user_roles",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "role")                       // column name inside tbl_user_roles
    private List<Role> roles = new ArrayList<>();

    @Column(name = "user_status")
    @Enumerated(EnumType.STRING)
    private Status userStatus;

    @Column(name = "user_info",columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode userInfo;


    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                .toList();
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return email;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() { return true; }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() { return true; }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @JsonIgnore
    @Override
    public boolean isEnabled() { return true; }


    public AppUserDTO toAppUserDTOWithoutTimestamps() {
        Map<String, Object> userInfoMap = null;
        try {
            if (this.userInfo != null && !this.userInfo.isEmpty()) {
                ObjectMapper mapper = new ObjectMapper();
                userInfoMap = mapper.convertValue(this.userInfo, Map.class);
            }
        } catch (Exception e) {
            userInfoMap = Map.of();
        }
        return AppUserDTO.builder()
                .userId(this.userId)
                .fullName(this.fullName)
                .email(this.email)
                .status(this.userStatus)
                .role(this.roles)
                .userInfo(userInfoMap)
                .createdAt(null)
                .updatedAt(null)
                .build();
    }
}

