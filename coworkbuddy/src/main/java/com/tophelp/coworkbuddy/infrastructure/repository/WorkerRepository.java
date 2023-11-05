package com.tophelp.coworkbuddy.infrastructure.repository;

import com.tophelp.coworkbuddy.domain.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, UUID> {
}
