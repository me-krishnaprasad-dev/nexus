package dev.krishnaprasad.nexus.repository;

import dev.krishnaprasad.nexus.dto.NexusState;
import dev.krishnaprasad.nexus.mapper.NexusStateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

/**
 * Redis repository for Nexus state hash entries under nexus:state:{apiName}.
 */
@Repository
@RequiredArgsConstructor
public class StateRepository {

    private final RedisTemplate<String, Object> redisTemplate;


    private String stateHashKey(String apiName) {
        return "nexus:state:" + apiName;
    }

    /**
     * This method will be used to fetch a user's NexusState from Redis by apiName and userId.
     */
    public NexusState findState(String apiName, String userId) {
        HashOperations<String, String, NexusState> hashOps = redisTemplate.opsForHash();
        return hashOps.get(stateHashKey(apiName), userId);
    }

    /**
     * This method will be used to save a NexusState entry into Redis.
     */
    public NexusState saveState(String apiName, String userId, int tokens) {
        HashOperations<String, String, NexusState> hashOps = redisTemplate.opsForHash();
        NexusState state = NexusStateMapper.toNexusState(apiName, userId, tokens);
        hashOps.put(stateHashKey(apiName), userId, state);
        return state;
    }

    /**
     * This method will be used to remove a user's state from Redis.
     */
    public void deleteState(String apiName, String userId) {
        HashOperations<String, String, NexusState> hashOps = redisTemplate.opsForHash();
        hashOps.delete(stateHashKey(apiName), userId);
    }
}

