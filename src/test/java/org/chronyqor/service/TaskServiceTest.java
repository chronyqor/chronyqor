package org.chronyqor.service;

import org.chronyqor.domain.Task;
import org.chronyqor.domain.User;
import org.chronyqor.dto.TaskCreateRequest;
import org.chronyqor.dto.TaskResponse;
import org.chronyqor.mapper.TaskMapper;
import org.chronyqor.repository.TaskRepository;
import org.chronyqor.security.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    @Mock
    private TaskMapper mapper;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private TaskService taskService;

    @Test
    void shouldCreateTask() {
        // Given
        User mockUser = User.builder().id(1L).username("testuser").build();
        TaskCreateRequest request = new TaskCreateRequest("Test Task", "Desc", null, null);
        Task task = Task.builder().title("Test Task").user(mockUser).build();
        TaskResponse responseDto = new TaskResponse(1L, "Test Task", "Desc", null, null, null, null);

        when(securityUtils.getCurrentUser()).thenReturn(mockUser);
        when(mapper.toEntity(any())).thenReturn(task);
        when(repository.save(any())).thenReturn(task);
        when(mapper.toResponse(any())).thenReturn(responseDto);

        // When
        TaskResponse result = taskService.create(request);

        // Then
        assertThat(result.title()).isEqualTo("Test Task");
        verify(repository).save(task);
        verify(securityUtils).getCurrentUser();
    }
}
