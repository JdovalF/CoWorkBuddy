package com.tophelp.coworkbuddy.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "roles")
public class Role {
    @Id
    private UUID id;
    @Column(nullable = false)
    private String name;
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
}
