package com.tophelp.coworkbuddy.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@Entity(name = "tasks")
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    private UUID id;
    @Column(nullable = false)
    private String name;
    private boolean active;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
}
