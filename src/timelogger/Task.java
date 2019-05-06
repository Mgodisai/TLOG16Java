package timelogger;

import timelogger.exceptions.*;

import java.time.LocalTime;

public class Task implements Comparable<Task> {
    private static final String REDMINE_TASK_ID_PATTERN = "\\d{4}";
    private static final String LTT_TASK_ID_PATTERN = "LT-\\d{4}";
    private String taskId;
    private LocalTime startTime;
    private LocalTime endTime;
    private String comment;

    public Task(String taskId, String comment, LocalTime startTime, LocalTime endTime) {
        setTaskId(taskId);
        setStartTime(startTime);
        setEndTime(endTime);
        setComment(comment);
    }

    public Task(String taskId, String comment, int startHour, int startMin, int endHour, int endMin) {
        this(taskId, comment, timeToLocalTime(startHour, startMin), timeToLocalTime(endHour, endMin));
    }

    public Task(String taskId, String comment, String startTime, String endTime) {
        this(taskId, comment, timeToLocalTime(startTime), timeToLocalTime(endTime));
    }

    public Task(String taskId) {
        this.setTaskId(taskId);
    }

    private static LocalTime timeToLocalTime(String time) {
        // Regex time pattern
        String timePattern = "^(0[0-9]|1[0-9]|2[0-3]|[0-9]):[0-5][0-9]$";

        if (time == null) {
            throw new EmptyTimeFieldException("The time is missing");
        }

        if (time.matches(timePattern)) {
            int hour = Integer.parseInt(time.split(":")[0]);
            int min = Integer.parseInt(time.split(":")[1]);
            return timeToLocalTime(hour, min);
        } else {
            throw new InvalidTimeFieldException("The time format is invalid!");
        }
    }

    private static LocalTime timeToLocalTime(int hour, int min) {
        if (hour < 0 || hour > 24 || min < 0 || min > 60) {
            throw new InvalidTimeFieldException("The time data is invalid!");
        } else {
            return LocalTime.of(hour, min);
        }
    }

    String getTaskId() {
        return taskId;
    }

    void setTaskId(String taskId) {
        if (taskId == null) {
            throw new NoTaskIdException("TaskID is missing");
        }

        if (isValidTaskId(taskId)) {
            this.taskId = taskId;
        } else {
            throw new InvalidTaskIdException("Invalid TaskID");
        }
    }

    String getComment() {
        return comment;
    }

    void setComment(String comment) {
        this.comment = comment;
    }

    LocalTime getStartTime() {
        return startTime;
    }

    void setStartTime(LocalTime startTime) {
        if (startTime == null) {
            throw new EmptyTimeFieldException("StartTimeField cannot be empty!");
        }

        if (this.endTime != null && startTime.isAfter(this.endTime)) {
            throw new NotExpectedTimeOrderException("The StartTime is after the current EndTime");
        }

        this.startTime = startTime;

        if (this.endTime != null && !Util.isMultipleQuarterHour(this.startTime, this.endTime)) {
            setEndTime(Util.roundToMultipleQuarterHour(this.startTime, this.endTime));
        }

    }

    void setStartTime(String startTime) {

        if (startTime == null || startTime.isBlank()) {
            throw new EmptyTimeFieldException("TimeField cannot be empty!");
        } else {
            setStartTime(timeToLocalTime(startTime));
        }
    }

    void setStartTime(int startHour, int startMin) {
        setStartTime(timeToLocalTime(startHour, startMin));
    }

    LocalTime getEndTime() {
        return endTime;
    }

    void setEndTime(LocalTime endTime) {

        if (endTime == null) {
            throw new EmptyTimeFieldException("EndTime cannot be empty!");
        }

        if (this.startTime != null && this.startTime.isAfter(endTime)) {
            throw new NotExpectedTimeOrderException("The new End time is before the current EndTime");
        }
            this.endTime = endTime;
        if (this.startTime != null && !Util.isMultipleQuarterHour(this.startTime, this.endTime)) {
            this.endTime = Util.roundToMultipleQuarterHour(this.startTime, endTime);
        }
    }

    void setEndTime(String endTime) {

        if (endTime == null || endTime.isBlank()) {
            throw new EmptyTimeFieldException("TimeField cannot be empty!");
        } else {
            setEndTime(timeToLocalTime(endTime));
        }
    }

    void setEndTime(int endHour, int endMin) {
        setEndTime(timeToLocalTime(endHour, endMin));
    }

    public boolean isValidTaskId(String taskId) {
        return (isValidRedmineTaskId(taskId) || isValidLTTTaskId(taskId));
    }

    public boolean isValidTaskId() {
        return (isValidRedmineTaskId(this.taskId) || isValidLTTTaskId(this.taskId));
    }

    public boolean isValidRedmineTaskId(String taskId) {
        return taskId.matches(REDMINE_TASK_ID_PATTERN);
    }

    public boolean isValidRedmineTaskId() {
        return this.taskId.matches(REDMINE_TASK_ID_PATTERN);
    }

    public boolean isValidLTTTaskId(String taskId) {
        return taskId.matches(LTT_TASK_ID_PATTERN);
    }

    public boolean isValidLTTTaskId() {
        return this.taskId.matches(LTT_TASK_ID_PATTERN);
    }

    long getMinPerTask() {

        if (this.startTime == null || this.endTime == null) {
            throw new EmptyTimeFieldException("One of the time field is empty!");
        }
        return (endTime.getHour() - startTime.getHour()) * 60 - startTime.getMinute() + endTime.getMinute();
    }

    @Override
    public int compareTo(Task o) {

        //1st level - start times
        int timeComp = this.startTime.compareTo(o.getStartTime());

        //2nd level - end times
        if (timeComp == 0) {
            if (this.endTime == null) {
                timeComp = -1;
            } else if (o.getEndTime() == null) {
                timeComp = 1;
            } else {
                timeComp = this.endTime.compareTo(o.getEndTime());
            }
        }

        //3rd level taskID
        if (timeComp == 0) {
            timeComp = this.taskId.compareTo(o.getTaskId());
        }

        return timeComp;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id: ");
        str.append(getTaskId());
        str.append(", comment: ");
        str.append(getComment());
        str.append(", startTime: ");
        str.append(getStartTime());
        str.append(", endTime: ");
        str.append(getEndTime());
        return str.toString();
    }

}
