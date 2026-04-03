package dev.krishnaprasad.nexus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * DTO for Nexus runtime state bucket.
 *
 * <p>Includes API name, user ID, current tokens, and last updated timestamp.</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NexusState {
    private String apiName;
    private String userId;
    private int tokens;
    private Instant lastUpdated;
}
