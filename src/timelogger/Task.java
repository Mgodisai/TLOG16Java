package timelogger;

import timelogger.exceptions.*;

import java.time.LocalTime;

/**
 * This is the Task class
 *
 * @author Tamás Varga
 * @version 0.2.0
 * @since 2019-03-26
 */
public class Task implements Comparable<Task> {
    private static final String REDMINE_TASK_ID_PATTERN = "\\d{4}";
    private static final String LTT_TASK_ID_PATTERN = "LT-\\d{4}";
    private String taskId;
    private LocalTime startTime;
    private LocalTime endTime;
    private String comment;

    public Task(String taskId, String comment, LocalTime startTime, LocalTime endTime)
            throws NotExpectedTimeOrderException, EmptyTimeFieldException,
            InvalidTaskIdException, NoTaskIdException {

        setTaskId(taskId);
        setStartTime(startTime);
        setEndTime(endTime);
        setComment(comment);
    }

    public Task(String taskId, String comment, int startHour, int startMin, int endHour, int endMin)
            throws NotExpectedTimeOrderException, EmptyTimeFieldException,
            InvalidTimeFieldException, InvalidTaskIdException, NoTaskIdException {

        this(taskId, comment, timeToLocalTime(startHour, startMin), timeToLocalTime(endHour, endMin));
    }

    public Task(String taskId, String comment, String startTime, String endTime)
            throws NotExpectedTimeOrderException, EmptyTimeFieldException,
            InvalidTimeFieldException, InvalidTaskIdException, NoTaskIdException {

        this(taskId, comment, timeToLocalTime(startTime), timeToLocalTime(endTime));
    }

    public Task(String taskId) throws InvalidTaskIdException, NoTaskIdException {
        this.setTaskId(taskId);
    }

    String getTaskId() {
        return taskId;
    }

    void setTaskId(String taskId) throws InvalidTaskIdException, NoTaskIdException {
        if (taskId == null) throw new NoTaskIdException("TaskID is missing");

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

    void setStartTime(LocalTime startTime) throws NotExpectedTimeOrderException, EmptyTimeFieldException {
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

    void setStartTime(String startTime)
            throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTimeFieldException {

        if (startTime == null || startTime.isBlank()) {
            throw new EmptyTimeFieldException("TimeField cannot be empty!");
        } else {
            setStartTime(timeToLocalTime(startTime));
        }
    }

    void setStartTime(int startHour, int startMin)
            throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTimeFieldException {
        setStartTime(timeToLocalTime(startHour, startMin));
    }

    LocalTime getEndTime() {
        return endTime;
    }

    void setEndTime(String endTime)
            throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTimeFieldException {
        setEndTime(timeToLocalTime(endTime));
    }

    void setEndTime(LocalTime endTime) throws NotExpectedTimeOrderException, EmptyTimeFieldException {
        if (this.startTime != null && this.startTime.isAfter(endTime)) {
            throw new NotExpectedTimeOrderException("The new End time is before the current EndTime");
        } else {
            this.endTime = endTime;
            if (this.startTime != null && !Util.isMultipleQuarterHour(this.startTime, this.endTime)) {
                this.endTime = Util.roundToMultipleQuarterHour(this.startTime, endTime);
            } else {
                this.endTime = endTime;
            }
        }
    }

    void setEndTime(int endHour, int endMin)
            throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTimeFieldException {

        setEndTime(timeToLocalTime(endHour, endMin));
    }


    private static LocalTime timeToLocalTime(String time) throws InvalidTimeFieldException, EmptyTimeFieldException {
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

    private static LocalTime timeToLocalTime(int hour, int min) throws InvalidTimeFieldException {
        if (hour < 0 || hour > 24 || min < 0 || min > 60) {
            throw new InvalidTimeFieldException("The time data is invalid!");
        } else {
            return LocalTime.of(hour, min);
        }
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

    long getMinPerTask() throws EmptyTimeFieldException {

        if (this.startTime == null || this.endTime == null) {
            throw new EmptyTimeFieldException("The time fields are empty!");
        }
            return (endTime.getHour() - startTime.getHour()) * 60 - startTime.getMinute() + endTime.getMinute();
    }

    @Override
    public int compareTo(Task o) {
        //1st level - start times
        int timeComparation = this.startTime.compareTo(o.getStartTime());
        //2nd level - end times
        if (timeComparation == 0) {
            if (this.endTime == null) {
                timeComparation = -1;
            } else if (o.getEndTime() == null) {
                timeComparation = 1;
            } else {
                timeComparation = this.endTime.compareTo(o.getEndTime());
            }
        }
        //3rd level taskID
        if (timeComparation == 0) {
            timeComparation = this.taskId.compareTo(o.getTaskId());
        }

        return timeComparation;
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
