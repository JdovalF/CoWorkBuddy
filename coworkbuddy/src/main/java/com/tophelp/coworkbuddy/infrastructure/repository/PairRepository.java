package com.tophelp.coworkbuddy.infrastructure.repository;

import com.tophelp.coworkbuddy.domain.Pair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PairRepository extends JpaRepository<Pair, UUID> {
  List<Pair> findAllByPairId(UUID pairId);

  List<Pair> findAllPairsByWorkerRoomId(UUID roomId);
}
