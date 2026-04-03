package dev.krishnaprasad.nexus.mapper;

import dev.krishnaprasad.nexus.dto.NexusState;

import java.time.Instant;

/**
 * Data mapper for NexusState conversion helpers.
 */
public class NexusStateMapper {

    private NexusStateMapper() {
        // static helper class
    }

    public static NexusState toNexusState(String apiName, String userId, int tokens) {
        // Build a NexusState using current time for lastUpdated.
        return new NexusState(apiName, userId, tokens, Instant.now());
    }
}
