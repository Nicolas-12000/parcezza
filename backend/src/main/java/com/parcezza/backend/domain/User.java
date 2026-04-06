package com.parcezza.backend.domain;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter @NoArgsConstructor
@ToString(exclude = "passwordHash")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotEmpty @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotEmpty @Size(min = 4, max = 200)
    private String fullName;

    @Column(nullable = false)
    private Boolean enabled = true;

    @JsonIgnore
    @Column(nullable = false, length = 255)
    private String passwordHash;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade =  CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    void addRole(Role role) {
        roles.add(role);
    }

    void removeRole(Role role) {
        roles.remove(role);
    }

    boolean hasRole(String roleName){
        return roles.stream().anyMatch(r -> r.getRoleName().equals(roleName));
    }

    void addAddress(Address address) {
        addresses.add(address);
        address.setUser(this);
    }

    void removeAddress(Address address) {
        addresses.remove(address);
        address.setUser(null);
    }

    Optional<Address> getPrimaryAddress() {
        return addresses.stream().filter(Address::isPrimary).findFirst();
    }

    void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

}
