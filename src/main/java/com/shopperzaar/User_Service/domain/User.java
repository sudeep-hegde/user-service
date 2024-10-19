package com.shopperzaar.User_Service.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;


@Entity(name="app_user")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String uuid;

    @Column
    private String email;

    @Column(length = 512)
    private String password;

    @Column
    private String first_name;

    @Column
    private String last_name;

    @Column
    private String phone_number;

    @Column
    private boolean verified;

    //Role access column?

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<UserRoles> userRoles;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime dateCreated;

    @LastModifiedDate
    @Column
    private LocalDateTime lastUpdated;

    @PrePersist
    private void setDateCreated(){
//        this.role = UserRole.USER;
        this.uuid = "USR-" + UUID.randomUUID();
        this.dateCreated = LocalDateTime.now();
        this.verified = false;
    }

    @PreUpdate
    private void setDateUpdated(){
        this.lastUpdated = LocalDateTime.now();
    }
}
