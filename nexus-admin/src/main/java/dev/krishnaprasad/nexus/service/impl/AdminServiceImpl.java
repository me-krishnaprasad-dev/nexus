package dev.krishnaprasad.nexus.service.impl;

import dev.krishnaprasad.nexus.dto.NexusConfig;
import dev.krishnaprasad.nexus.dto.NexusState;
import dev.krishnaprasad.nexus.entity.NexusConfigEntity;
import dev.krishnaprasad.nexus.exception.ResourceNotFoundException;
import dev.krishnaprasad.nexus.mapper.NexusConfigMapper;
import dev.krishnaprasad.nexus.mapper.NexusStateMapper;
import dev.krishnaprasad.nexus.repository.NexusConfigJpaRepository;
import dev.krishnaprasad.nexus.repository.StateRepository;
import dev.krishnaprasad.nexus.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of {@link AdminService} to manage configuration and runtime state.
 * Uses a JPA repository for persisted config values and Redis for runtime state values.
 *
 * @author me-krishnaprasad-dev
 */
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final NexusConfigJpaRepository nexusConfigJpaRepository;
    private final StateRepository stateRepository;
    private final RedisConnectionFactory connectionFactory;

    @Override
    public List<NexusConfig> listConfigs() {
        return nexusConfigJpaRepository.findAll().stream().map(NexusConfigMapper::toDto).toList();
    }

    /**
     * This method will be used to fetch configuration for a given API name.
     * It will throw ResourceNotFoundException if config is missing.
     *
     * @param apiName API identifier.
     * @return NexusConfig for the API.
     */
    @Override
    public NexusConfig getConfig(String apiName) {
        NexusConfigEntity entity = nexusConfigJpaRepository.findById(apiName)
                .orElseThrow(() -> new ResourceNotFoundException("Config not found for apiName: " + apiName));
        return NexusConfigMapper.toDto(entity);
    }

    /**
     * This method will be used to create or update a Nexus configuration.
     * It returns the saved NexusConfig DTO.
     *
     * @param config NexusConfig details to save.
     * @return saved NexusConfig.
     */
    @Override
    public NexusConfig saveConfig(NexusConfig config) {
        NexusConfigEntity entity = NexusConfigMapper.toEntity(config);
        NexusConfigEntity saved = nexusConfigJpaRepository.save(entity);
        return NexusConfigMapper.toDto(saved);
    }

    /**
     * This method will be used to delete an API config if it exists.
     * It will throw ResourceNotFoundException when apiName is not present.
     *
     * @param apiName API identifier.
     */
    @Override
    public void deleteConfig(String apiName) {
        if (!nexusConfigJpaRepository.existsById(apiName)) {
            throw new ResourceNotFoundException("Config not found for apiName: " + apiName);
        }
        nexusConfigJpaRepository.deleteById(apiName);
    }

    /**
     * This method will be used to fetch user runtime state by API and user ID.
     * It will throw ResourceNotFoundException when state is not found.
     *
     * @param apiName API identifier.
     * @param userId  user identifier.
     * @return NexusState for user and API.
     */
    @Override
    public NexusState getState(String apiName, String userId) {
        NexusState state = stateRepository.findState(apiName, userId);
        if (state == null) {
            throw new ResourceNotFoundException("State not found for apiName: " + apiName + " userId: " + userId);
        }
        return state;
    }

    /**
     * This method will be used to reset a user's runtime state.
     * It clears user state in Redis and returns a zero-token NexusState.
     *
     * @param apiName API identifier.
     * @param userId  user identifier.
     * @return zeroed NexusState after reset.
     */
    @Override
    public NexusState resetState(String apiName, String userId) {
        stateRepository.deleteState(apiName, userId);
        return NexusStateMapper.toNexusState(apiName, userId, 0);
    }

    /**
     * This method will be used to discover APIs from Redis state and persisted configs.
     * It returns a set of unique API names.
     *
     * @return set of API names.
     */
    @Override
    public Set<String> discoverApis() {
        Set<String> apiNames = new HashSet<>();

        // Use SCAN to avoid blocking Redis with KEYS for large keyspace.
        try (RedisConnection connection = connectionFactory.getConnection()) {
            Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().match("nexus:state:*").count(1000).build());

            // iterate page by page
            while (cursor.hasNext()) {
                String key = new String(cursor.next(), StandardCharsets.UTF_8);

                // key format: nexus:state:{apiName}
                if (key.startsWith("nexus:state:")) {
                    String apiName = key.substring("nexus:state:".length());
                    apiNames.add(apiName);
                }
            }
        }

        // Include configured APIs even if no state key exists yet.
        nexusConfigJpaRepository.findAll().forEach(c -> apiNames.add(c.getApiName()));

        return apiNames;
    }
}
