package dev.krishnaprasad.nexus.controller;

import dev.krishnaprasad.nexus.dto.ApiResponse;
import dev.krishnaprasad.nexus.dto.NexusConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "nexus-admin", description = "Control plane management for Nexus")
@RequestMapping("/api/v1")
@Validated
public interface AdminController {

    /**
     * This method will be used to list all rate-limiting configuration rules.
     * It will return a list of NexusConfig objects wrapped in ApiResponse.
     */
    @Operation(summary = "List all configs")
    @GetMapping("/configs")
    ResponseEntity<ApiResponse<?>> listConfigs();

    /**
     * This method will be used to fetch a single rule by API name.
     * It will return that config wrapped in ApiResponse.
     */
    @Operation(summary = "Get config by API name")
    @GetMapping("/configs/{apiName}")
    ResponseEntity<ApiResponse<?>> getConfig(@PathVariable String apiName);

    /**
     * This method will be used to create or update a config rule.
     * It will return the saved NexusConfig wrapped in ApiResponse.
     */
    @Operation(summary = "Create or update config")
    @PostMapping("/configs")
    ResponseEntity<ApiResponse<?>> createOrUpdateConfig(@RequestBody NexusConfig config);

    /**
     * This method will be used to delete a rule by API name.
     */
    @Operation(summary = "Delete config by API name")
    @DeleteMapping("/configs/{apiName}")
    ResponseEntity<ApiResponse<?>> deleteConfig(@PathVariable String apiName);

    /**
     * This method will be used to fetch user's current token bucket state for an API.
     * It will return NexusState wrapped in ApiResponse.
     */
    @Operation(summary = "Get runtime state for a user")
    @GetMapping("/state/{apiName}/{userId}")
    ResponseEntity<ApiResponse<?>> getState(@PathVariable String apiName, @PathVariable String userId);

    /**
     * This method will be used to reset a user's runtime state for an API.
     * It will return the new NexusState after reset wrapped in ApiResponse.
     */
    @Operation(summary = "Reset runtime state for a user")
    @DeleteMapping("/state/{apiName}/{userId}")
    ResponseEntity<ApiResponse<?>> resetState(@PathVariable String apiName, @PathVariable String userId);

    /**
     * This method will be used to discover all known APIs by scanning state keys.
     */
    @Operation(summary = "Discover known APIs")
    @GetMapping("/discovery/apis")
    ResponseEntity<ApiResponse<?>> discoverApis();
}


