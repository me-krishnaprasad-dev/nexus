package dev.krishnaprasad.nexus.repository;

import dev.krishnaprasad.nexus.entity.NexusConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA repository for persisted Nexus config table.
 */
@Repository
public interface NexusConfigJpaRepository extends JpaRepository<NexusConfigEntity, String> {
}
