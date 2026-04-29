// ===== mapper/TaskMapper.java =====
package org.chronyqor.mapper;

import org.mapstruct.*;
import org.chronyqor.domain.Task;
import org.chronyqor.dto.*;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    // Создание новой сущности из DTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(source = "status", target = "status")
    Task toEntity(TaskCreateRequest request);

    // Обновление существующей сущности из DTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(source = "status", target = "status")
    void updateEntityFromDto(TaskUpdateRequest request, @MappingTarget Task task);

    // Маппинг сущности в DTO ответа
    @Mapping(source = "status", target = "status")
    TaskResponse toResponse(Task task);

    // ===== Enum маппинг =====
    default TaskStatus mapStatus(Task.Status status) {
        return status == null ? null : TaskStatus.valueOf(status.name());
    }

    default Task.Status mapStatus(TaskStatus status) {
        return status == null ? null : Task.Status.valueOf(status.name());
    }
}
