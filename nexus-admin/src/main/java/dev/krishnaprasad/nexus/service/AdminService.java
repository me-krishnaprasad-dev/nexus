package dev.krishnaprasad.nexus.service;

import dev.krishnaprasad.nexus.dto.NexusConfig;
import dev.krishnaprasad.nexus.dto.NexusState;

import java.util.List;
import java.util.Set;

/**
 * Service Interface for Admin operations.
 *
 * <p>This service defines the control-plane contract for Nexus API rule and state management.</p>
 *
 * @author krishnaprasad.dev
 */
public interface AdminService {

    /**
     * This method will be used to list all the configs in the system.
     * It will return a list of NexusConfig objects.
     * @return List of NexusConfig objects
     */
    List<NexusConfig> listConfigs();

    /**
     * This method will be used to retrieve a single configuration by API name.
     * @param apiName API identifier (slug)
     * @return NexusConfig if present
     */
    NexusConfig getConfig(String apiName);

    /**
     * This method will be used to create or update a configuration entry.
     * @param config NexusConfig payload with validated fields
     * @return saved NexusConfig object
     */
    NexusConfig saveConfig(NexusConfig config);

    /**
     * This method will be used to delete the config for an API.
     * @param apiName API identifier
     */
    void deleteConfig(String apiName);

    /**
     * This method will be used to read runtime state for a specific user and API.
     * @param apiName API identifier
     * @param userId user identifier
     * @return NexusState object
     */
    NexusState getState(String apiName, String userId);

    /**
     * This method will be used to reset runtime state bucket for a given user to 0 tokens.
     * @param apiName API identifier
     * @param userId user identifier
     * @return NexusState new state object after reset
     */
    NexusState resetState(String apiName, String userId);

    /**
     * This method will be used to discover API names from active state keys and configured rules.
     * @return unique set of API names
     */
    Set<String> discoverApis();
}
