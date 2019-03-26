import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WorkDay {
    private List<Task> tasks;
    private long requiredMinPerDay;
    private LocalDate actualDay;
    private long sumPerDay;

    public WorkDay(long requiredMinPerDay, LocalDate actualDay) {
        this.requiredMinPerDay = requiredMinPerDay;
        this.actualDay = actualDay;
        this.tasks = new ArrayList<>();
    }

    public WorkDay(long requiredMinPerDay) {
        this(requiredMinPerDay, LocalDate.now());
    }

    public WorkDay(LocalDate actualDay) {
        this(450, actualDay);
    }

    public WorkDay() {
        this(450, LocalDate.now());
    }

    public long getRequiredMinPerDay() {
        return requiredMinPerDay;
    }

    public LocalDate getActualDay() {
        return actualDay;
    }

    public long getSumPerDay() {
        return sumPerDay;
    }

    public long getExtraMinPerDay() {
        return requiredMinPerDay - sumPerDay;
    }

    public boolean isSeparatedTime(Task t) {

        for (Task checkedTask : tasks) {

            if ((!checkedTask.getStartTime().isAfter(t.getStartTime()) && !checkedTask.getEndTime().isAfter(t.getStartTime())) ||
                    (!checkedTask.getStartTime().isBefore(t.getEndTime()) && !checkedTask.getEndTime().isBefore(t.getEndTime()))) {

            } else {
                return false;
            }
        }
        return true;
    }

    public void addTask(Task t) {
        if (t.isMultipleQuarterHour() && isSeparatedTime(t)) {
            tasks.add(t);
            System.out.println("A hozzáadás sikeres! "+this.actualDay+" - "+t.getTaskId()+": "+t.getComment());
        } else {
            System.out.println("SIKERTELEN! "+this.actualDay+" - "+t.getTaskId()+": "+t.getComment());
        }
    }

    public boolean isWeekday() {
        return this.actualDay.getDayOfWeek().getValue()<6;
    }
}

