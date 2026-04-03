package dev.krishnaprasad.nexus.mapper;

import dev.krishnaprasad.nexus.dto.NexusConfig;
import dev.krishnaprasad.nexus.entity.NexusConfigEntity;

/**
 * Mapper that converts NexusConfig DTOs to entity and back.
 */
/**
 * Data mapper for NexusConfig and NexusConfigEntity.
 */
public class NexusConfigMapper {

    private NexusConfigMapper() {
    }

    public static NexusConfigEntity toEntity(NexusConfig dto) {
        return NexusConfigEntity.builder()
                .apiName(dto.getApiName())
                .capacity(dto.getCapacity())
                .refreshRate(dto.getRefreshRate())
                .build();
    }

    public static NexusConfig toDto(NexusConfigEntity entity) {
        return new NexusConfig(entity.getApiName(), entity.getCapacity(), entity.getRefreshRate());
    }
}
