package dev.krishnaprasad.nexus.controller.impl;

import dev.krishnaprasad.nexus.controller.AdminController;
import dev.krishnaprasad.nexus.dto.ApiResponse;
import dev.krishnaprasad.nexus.dto.NexusConfig;
import dev.krishnaprasad.nexus.dto.NexusState;
import dev.krishnaprasad.nexus.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * Controller implementation for Nexus Admin control plane.
 *
 * <p>All request mappings are defined in {@link dev.krishnaprasad.nexus.controller.AdminController}.</p>
 */
@RestController
@RequiredArgsConstructor
public class AdminControllerImpl implements AdminController {

    private final AdminService adminService;

    @Override
    public ResponseEntity<ApiResponse<?>> listConfigs() {
        return ResponseEntity.ok(ApiResponse.success(adminService.listConfigs()));
    }

    @Override
    public ResponseEntity<ApiResponse<?>> getConfig(String apiName) {
        return ResponseEntity.ok(ApiResponse.success(adminService.getConfig(apiName)));
    }

    @Override
    public ResponseEntity<ApiResponse<?>> createOrUpdateConfig(@Valid NexusConfig config) {
        return new ResponseEntity<>(ApiResponse.success(adminService.saveConfig(config)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ApiResponse<?>> deleteConfig(String apiName) {
        adminService.deleteConfig(apiName);
        return ResponseEntity.ok(ApiResponse.success("deleted"));
    }

    @Override
    public ResponseEntity<ApiResponse<?>> getState(String apiName, String userId) {
        return ResponseEntity.ok(ApiResponse.success(adminService.getState(apiName, userId)));
    }

    @Override
    public ResponseEntity<ApiResponse<?>> resetState(String apiName, String userId) {
        NexusState state = adminService.resetState(apiName, userId);
        return ResponseEntity.ok(ApiResponse.success(state));
    }

    @Override
    public ResponseEntity<ApiResponse<?>> discoverApis() {
        Set<String> apis = adminService.discoverApis();
        return ResponseEntity.ok(ApiResponse.success(apis));
    }
}
