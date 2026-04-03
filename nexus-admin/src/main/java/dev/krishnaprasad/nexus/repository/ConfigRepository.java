package dev.krishnaprasad.nexus.repository;

import dev.krishnaprasad.nexus.dto.NexusConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Redis repository for Nexus config hash store (nexus:config).
 */
@Repository
@RequiredArgsConstructor
public class ConfigRepository {

    private static final String CONFIG_HASH = "nexus:config";

    private final RedisTemplate<String, Object> redisTemplate;

    private HashOperations<String, String, NexusConfig> getHashOps() {
        return redisTemplate.opsForHash();
    }

    /**
     * This method will be used to list all config entries from Redis.
     * It will return a list of NexusConfig objects.
     */
    public List<NexusConfig> findAll() {
        return new ArrayList<>(getHashOps().entries(CONFIG_HASH).values());
    }

    /**
     * This method will be used to fetch a config entry by apiName from Redis.
     */
    public NexusConfig findByApiName(String apiName) {
        return getHashOps().get(CONFIG_HASH, apiName);
    }

    /**
     * This method will be used to save a config entry into Redis.
     */
    public NexusConfig save(NexusConfig config) {
        getHashOps().put(CONFIG_HASH, config.getApiName(), config);
        return config;
    }

    /**
     * This method will be used to delete a config entry from Redis.
     */
    public void delete(String apiName) {
        getHashOps().delete(CONFIG_HASH, apiName);
    }

    /**
     * This method will be used to check if a config entry exists in Redis.
     */
    public boolean exists(String apiName) {
        return Boolean.TRUE.equals(getHashOps().hasKey(CONFIG_HASH, apiName));
    }
}
