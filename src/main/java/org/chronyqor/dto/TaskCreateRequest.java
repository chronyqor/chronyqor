// ===== dto/TaskCreateRequest.java =====
package org.chronyqor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record TaskCreateRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 100, message = "Title must not exceed 100 characters")
        String title,

        @Size(max = 500, message = "Description must not exceed 500 characters")
        String description,

        @NotNull(message = "Status is required")
        TaskStatus status,

        LocalDateTime dueDate
) {}
