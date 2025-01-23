package com.ty.Entities;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ty.Enum.PresentationStatus;
import com.ty.Enum.Role;
import com.ty.Enum.Status;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(
    name = "users",
    uniqueConstraints = @UniqueConstraint(columnNames = "email")
)
public class User {
    @Id
    private int uid;
    
    @Version
    private Integer version;

    @Column( updatable = false, unique = true)
    private String email;

    private String name;

    @Column( nullable = false)
    private long uphone;

    private String password;

    @Enumerated(EnumType.STRING)
    private PresentationStatus presentation;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Role role;

    
    private double user_total_score;
    
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Presentation> presentations;



    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Rating> ratings;

    @CreationTimestamp
    @Column( updatable = false)
    private LocalDateTime reg_time;
}
