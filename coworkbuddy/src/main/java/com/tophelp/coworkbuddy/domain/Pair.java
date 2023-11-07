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

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity(name = "pairs")
@NoArgsConstructor
@AllArgsConstructor
public class Pair {

  @Id
  private UUID id;

  @Column(nullable = false)
  private UUID pairId;

  @ManyToOne
  @JoinColumn(name = "worker_id")
  private Worker worker;

  private LocalDateTime lastPairingDate;

}
