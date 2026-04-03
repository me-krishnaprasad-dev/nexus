package dev.krishnaprasad.nexus.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Nexus rate limit config values.
 *
 * <p>Contains API name and bucket parameters required by the control plane.</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NexusConfig {

    @NotBlank(message = "api_name is required")
    @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$", message = "api_name must be a slug (lowercase letters, digits, hyphens)")
    private String apiName;

    @Min(value = 1, message = "capacity must be > 0")
    private int capacity;

    @Min(value = 1, message = "refresh_rate must be > 0")
    private int refreshRate;
}
