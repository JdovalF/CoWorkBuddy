package com.tophelp.coworkbuddy.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity(name = "rooms")
@NoArgsConstructor
@AllArgsConstructor
public class Room {

  @Id
  private UUID id;

  @Column(nullable = false)
  private String name;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @OneToMany(mappedBy = "room")
  private Set<Task> tasks;

  @OneToMany(mappedBy = "room")
  private Set<Worker> workers;

  private LocalDateTime creationDate;

}
