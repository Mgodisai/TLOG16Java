package timelogger;

import org.jetbrains.annotations.NotNull;
import timelogger.exceptions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class WorkDay implements Comparable<WorkDay> {
    private List<Task> tasks;
    private long requiredMinPerDay;
    private LocalDate actualDay;
    private long sumPerDay;

    private static final long DEFAULT_REQUIRED_MIN_PER_DAY = 450;

    public WorkDay(long requiredMinPerDay, LocalDate actualDay) {
        setRequiredMinPerDay(requiredMinPerDay);
        setActualDay(actualDay);
        this.tasks = new ArrayList<>();
    }

    public WorkDay(long requiredMinPerDay, int year, int month, int day) {
        this(requiredMinPerDay, LocalDate.of(year, month, day));
    }

    public WorkDay(int year, int month, int day) {
        this(DEFAULT_REQUIRED_MIN_PER_DAY, LocalDate.of(year, month, day));
    }

    public WorkDay(long requiredMinPerDay) {
        this(requiredMinPerDay, LocalDate.now());
    }

    public WorkDay(LocalDate actualDay) {
        this(DEFAULT_REQUIRED_MIN_PER_DAY, actualDay);
    }

    public WorkDay() {
        this(DEFAULT_REQUIRED_MIN_PER_DAY, LocalDate.now());
    }

    List<Task> getTasks() {
        return tasks;
    }

    long getRequiredMinPerDay() {
        return requiredMinPerDay;
    }

    LocalDate getActualDay() {
        return actualDay;
    }

    long getSumPerDay() {

        return sumPerDay=this.tasks.stream()
                .mapToLong(i-> i.getMinPerTask())
                .sum();
    }

    public void setActualDay(LocalDate actualDay) {
        if (actualDay.isAfter(LocalDate.now())) {
            throw new FutureWorkException("Actual Day can not be later than today!");
        } else {
            this.actualDay = actualDay;
        }
    }

    public void setActualDay(int year, int month, int day) {
        setActualDay(LocalDate.of(year, month, day));
    }

    public void setRequiredMinPerDay(long requiredMinPerDay) {
        if (requiredMinPerDay<0) {
            throw new NegativeMinutesOfWorkException("RequiredMinPerDay can not be negative!");
        } else {
            this.requiredMinPerDay = requiredMinPerDay;
        }
    }

    long getExtraMinPerDay() {
        return getSumPerDay()-getRequiredMinPerDay();
    }

    LocalTime getLatestEndTime() {
        Optional<Task> opt = tasks.stream().max(Task::compareTo);
        return opt.isPresent() ? opt.get().getEndTime() : null;
    }

    public void addTask(Task t) throws NotSeparatedTimesException{
        if (t == null) {
            System.out.println("The object is null");
            return;
        }
        if (!Util.isSeparatedTime(this.tasks, t)) throw new NotSeparatedTimesException(t.toString() + " can not add to WorkDay");

        tasks.add(t);
        System.out.println(t.toString()+ " successfully added");
    }


    @Override
    public int compareTo(@NotNull WorkDay o) {
        return this.getActualDay().compareTo(o.getActualDay());
    }

    @Override
    public String toString() {
        return getActualDay().toString()+" reqMinPerDay: "+getRequiredMinPerDay()+" min";
    }
}

