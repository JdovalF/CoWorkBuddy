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

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity(name = "workers")
@NoArgsConstructor
@AllArgsConstructor
public class Worker {

  @Id
  private UUID id;

  @Column(nullable = false)
  private String name;

  private boolean active;

  @ManyToOne
  @JoinColumn(name = "room_id")
  private Room room;

  @ManyToOne
  @JoinColumn(name = "task_id")
  private Task task;

  @OneToMany(mappedBy = "worker")
  private List<Pair> pairs;

}
