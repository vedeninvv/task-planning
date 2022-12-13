package com.practice.taskplanning.datasetup;

import com.practice.taskplanning.service.TaskService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TaskSetUp implements DataSetUp {
    private final TaskService taskService;

    public TaskSetUp(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Обновление статусов заданий при старте приложения
     */
    @EventListener(ApplicationReadyEvent.class)
    public void updatingTaskStatusIfDeadlineOverdue() {
        taskService.updatingTaskStatusIfDeadlineOverdue();
    }
}
