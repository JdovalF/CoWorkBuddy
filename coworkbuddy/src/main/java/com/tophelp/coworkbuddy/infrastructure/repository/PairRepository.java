package com.tophelp.coworkbuddy.infrastructure.repository;

import com.tophelp.coworkbuddy.domain.Pair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PairRepository extends JpaRepository<Pair, UUID> {

  @Query(value = "SELECT * FROM pairs p WHERE p.pair_id IN (" +
                 "SELECT p_inner.pair_id FROM pairs p_inner " +
                 "WHERE p_inner.worker_id IN :workerIds " +
                 "GROUP BY p_inner.pair_id HAVING COUNT(DISTINCT p_inner.worker_id) = :size) " +
                 "AND p.worker_id IN :workerIds " +
                 "ORDER BY p.last_pairing_date DESC LIMIT 1",
      nativeQuery = true)
  Optional<Pair> findMostRecentPairing(@Param("workerIds") List<UUID> workerIds, @Param("size") long size);

  List<Pair> findAllByWorkerId(UUID id);
}
