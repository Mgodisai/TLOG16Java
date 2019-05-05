package timelogger;

import timelogger.exceptions.EmptyTimeFieldException;
import timelogger.exceptions.NotExpectedTimeOrderException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Util {

    static LocalTime roundToMultipleQuarterHour(LocalTime startTime, LocalTime endTime) {
        long minPerTask = (endTime.getHour() - startTime.getHour()) * 60 -
                startTime.getMinute() + endTime.getMinute();
        long rem = minPerTask % 15;
        if (rem > 7) {
            System.out.println("End time was rounded to: " + endTime.plusMinutes(15 - rem));
            return endTime.plusMinutes(15 - rem);
        } else {
            System.out.println("End time was rounded to: " + endTime.minusMinutes(rem));
            return endTime.minusMinutes(rem);
        }
    }

    static boolean isWeekday(LocalDate time) {
        return time.getDayOfWeek().getValue() < 6;
    }

    public static boolean isWeekday() {
        return isWeekday(LocalDate.now());
    }

    public static long minPerTask(LocalTime startTime, LocalTime endTime) throws EmptyTimeFieldException, NotExpectedTimeOrderException {
        if (startTime == null || endTime == null) {
            throw new EmptyTimeFieldException("The time fields are empty!");
        }

        if (startTime.isAfter(endTime)) {
            throw new NotExpectedTimeOrderException("The Start Time is after EndTime");
        }

        return (endTime.getHour() - startTime.getHour()) * 60 - startTime.getMinute() + endTime.getMinute();

    }

    public static boolean isMultipleQuarterHour(LocalTime startTime, LocalTime endTime) throws EmptyTimeFieldException, NotExpectedTimeOrderException {
        return minPerTask(startTime, endTime) % 15 == 0;
    }

    public static boolean isMultipleQuarterHour(long minPerTask) {
        return minPerTask % 15 == 0;
    }

    public static boolean isSeparatedTime(List<Task> tasks, Task t) {
        if (tasks.size() == 0) {
            return true;
        }
        LocalTime startTime = t.getStartTime();
        LocalTime endTime = t.getEndTime() == null ? startTime : t.getEndTime();
        LocalTime checkedEndTime;
        for (Task checkedTask : tasks) {

            if (checkedTask.equals(t)) continue;

            checkedEndTime = checkedTask.getEndTime() == null ? checkedTask.getStartTime() : checkedTask.getEndTime();

            if (!checkedEndTime.isAfter(startTime) || !checkedTask.getStartTime().isBefore(endTime)) {

            } else {
                System.out.println(t.getTaskId() + " [s:" + t.getStartTime() + ", e:" + t.getEndTime() + "] \n" +
                        "the task duration is overlapping with task: " + checkedTask.getTaskId() + " [" + checkedTask.getStartTime() + " - " + checkedTask.getEndTime() + "]");
                return false;
            }
        }
        return true;

    }


}
