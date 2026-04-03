package dev.krishnaprasad.nexus.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "nexus_config")
public class NexusConfigEntity {

    @Id
    @Column(name = "api_name", nullable = false, unique = true)
    private String apiName;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "refresh_rate", nullable = false)
    private int refreshRate;
}
