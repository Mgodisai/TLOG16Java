package com.mgodisai;

import java.time.LocalTime;

class Task {
    private String taskId;
    private LocalTime startTime;
    private LocalTime endTime;
    private String comment;

    public Task(String taskId, String comment, int startHour, int startMin, int endHour, int endMin) {
        this.taskId = taskId;
        this.startTime = LocalTime.of(startHour, startMin);
        this.endTime = LocalTime.of(endHour, endMin);
        this.comment = comment;
    }

    public Task(String taskId, String comment, String startTime, String endTime) {
        this.taskId = taskId;
        this.startTime = LocalTime.of(Integer.parseInt(startTime.split(":")[0]), Integer.parseInt(startTime.split(":")[1]));
        this.endTime = LocalTime.of(Integer.parseInt(endTime.split(":")[0]), Integer.parseInt(endTime.split(":")[1]));
        this.comment = comment;
    }

    private boolean isValidTaskId(String taskId) {
        String pattern = "\\d{4}|LT-\\d{4}";
        return taskId.matches(pattern);
    }

    public boolean isValidTaskId() {
        String pattern = "\\d{4}|LT-\\d{4}";
        return this.taskId.matches(pattern);
    }

    public boolean isMultipleQuarterHour() {
        return getMinPerTask()%15 == 0;
    }

    public long getMinPerTask() {
        return ( endTime.getHour() - startTime.getHour() ) * 60 - startTime.getMinute() + endTime.getMinute();
    }

    public String getTaskId() {
        return taskId;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getComment() {
        return comment;
    }
}
