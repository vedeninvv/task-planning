package com.practice.taskplanning.model.user;

public enum Permission {
    USER_READ_ALL("user:read:all"),
    USER_WRITE_SELF("user:write:self"),
    USER_WRITE_ALL("user:write:all"),
    TEAM_WRITE("team:write"),
    TEAM_READ("team:read"),
    TASK_WRITE("task:write"),
    TASK_READ("task:read"),
    TASK_POINT_WRITE("task_point:write"),
    TASK_POINT_READ("task_point:read"),
    ASSIGN_SELF_TO_TASK("assign_to_task:self"),
    ASSIGN_ALL_TO_TASK("assign_to_task:all"),
    REMOVE_SELF_FROM_TASK("remove_from_task:self"),
    REMOVE_ALL_FROM_TASK("remove_from_task:all"),
    COMPLETE_ASSIGNED_TASK_POINT("complete_task_point:assigned"),
    COMPLETE_ALL_TASK_POINT("complete_task_point:all"),
    ROLLBACK_ASSIGNED_TASK_POINT("rollback_task_point:assigned"),
    ROLLBACK_ALL_TASK_POINT("rollback_task_point:all");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
