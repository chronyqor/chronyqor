package org.chronyqor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.chronyqor.dto.*;
import org.chronyqor.service.TaskService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@Tag(name = "Tasks", description = "Task Management API")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new task")
    public TaskResponse create(@Valid @RequestBody TaskCreateRequest request) {
        return service.create(request);
    }

    @GetMapping
    @Operation(summary = "Get all tasks (paginated)")
    public PageResponse<TaskResponse> getAll(
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable
    ) {
        return service.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by ID")
    public TaskResponse getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing task")
    public TaskResponse update(@PathVariable Long id, @Valid @RequestBody TaskUpdateRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a task")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
