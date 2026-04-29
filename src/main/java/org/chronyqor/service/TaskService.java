package org.chronyqor.service;

import org.chronyqor.exception.EntityNotFoundException;
import org.chronyqor.domain.Task;
import org.chronyqor.dto.*;
import org.chronyqor.mapper.TaskMapper;
import org.chronyqor.repository.TaskRepository;
import org.chronyqor.security.SecurityUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TaskService {

    private final TaskRepository repository;
    private final TaskMapper mapper;
    private final SecurityUtils securityUtils;

    public TaskService(TaskRepository repository, TaskMapper mapper, SecurityUtils securityUtils) {
        this.repository = repository;
        this.mapper = mapper;
        this.securityUtils = securityUtils;
    }

    @CacheEvict(value = "tasks", allEntries = true)
    public TaskResponse create(TaskCreateRequest request) {
        Task task = mapper.toEntity(request);
        task.setUser(securityUtils.getCurrentUser());
        return mapper.toResponse(repository.save(task));
    }

    @Cacheable(value = "tasks", key = "#securityUtils.getCurrentUserId() + '-' + #pageable.pageNumber")
    public PageResponse<TaskResponse> findAll(Pageable pageable) {
        Page<Task> page = repository.findByUserId(securityUtils.getCurrentUserId(), pageable);
        
        List<TaskResponse> content = page.stream()
                .map(mapper::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    public List<TaskResponse> findAll() {
        return repository.findByUserId(securityUtils.getCurrentUserId())
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Cacheable(value = "tasks", key = "#id")
    public TaskResponse findById(Long id) {
        return repository.findByIdAndUserId(id, securityUtils.getCurrentUserId())
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
    }

    @CacheEvict(value = "tasks", allEntries = true)
    public TaskResponse update(Long id, TaskUpdateRequest request) {
        Task task = repository.findByIdAndUserId(id, securityUtils.getCurrentUserId())
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
        mapper.updateEntityFromDto(request, task);
        return mapper.toResponse(repository.save(task));
    }

    @CacheEvict(value = "tasks", allEntries = true)
    public void delete(Long id) {
        if (!repository.existsByIdAndUserId(id, securityUtils.getCurrentUserId())) {
            throw new EntityNotFoundException("Task not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
