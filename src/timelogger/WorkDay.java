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

    private static final long defaultRequiredMinPerDay=450;

    public WorkDay(long requiredMinPerDay, LocalDate actualDay)
            throws NegativeMinutesOfWorkException, FutureWorkException {
        this.setRequiredMinPerDay(requiredMinPerDay);
        setActualDay(actualDay);
        this.tasks = new ArrayList<>();
    }

    public WorkDay(long requiredMinPerDay, int year, int month, int day)
            throws NegativeMinutesOfWorkException, FutureWorkException {
        this(requiredMinPerDay, LocalDate.of(year, month, day));
    }

    public WorkDay(int year, int month, int day)
            throws NegativeMinutesOfWorkException, FutureWorkException {
        this(defaultRequiredMinPerDay, LocalDate.of(year, month, day));
    }

    public WorkDay(long requiredMinPerDay)
            throws NegativeMinutesOfWorkException, FutureWorkException {
        this(requiredMinPerDay, LocalDate.now());
    }

    public WorkDay(LocalDate actualDay)
            throws NegativeMinutesOfWorkException, FutureWorkException {
        this(defaultRequiredMinPerDay, actualDay);
    }

    public WorkDay()
            throws NegativeMinutesOfWorkException, FutureWorkException {
        this(defaultRequiredMinPerDay, LocalDate.now());
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

        return this.tasks.stream()
                .mapToLong(i-> {
                    try {
                        return i.getMinPerTask();
                    } catch (EmptyTimeFieldException e) {
                        return 0;
                    }
                })
                .sum();
    }

    public void setActualDay(LocalDate actualDay) throws FutureWorkException {
        if (actualDay.isAfter(LocalDate.now())) {
            throw new FutureWorkException("Actual Day can not be later than today!");
        } else {
            this.actualDay = actualDay;
        }
    }

    public void setActualDay(int year, int month, int day) throws FutureWorkException {
        setActualDay(LocalDate.of(year, month, day));
    }

    public void setRequiredMinPerDay(long requiredMinPerDay) throws NegativeMinutesOfWorkException {
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

        boolean isSeparated=Util.isSeparatedTime(this.tasks, t);

        if (!isSeparated) throw new NotSeparatedTimesException(t.toString() + " can not add to WorkDay");

        boolean isMultipleQuarterHour = false;

        try {
            isMultipleQuarterHour = Util.isMultipleQuarterHour(t.getStartTime(), t.getEndTime());
        } catch (EmptyTimeFieldException e) {
            if (t.getStartTime()!=null && t.getEndTime()==null) {
                isMultipleQuarterHour=true;
            }
        } catch (NotExpectedTimeOrderException e) {
            // isMultipleQuarterHour remains false
        }

        if (isSeparated && isMultipleQuarterHour) {
            tasks.add(t);
            System.out.println(t.toString()+ " successfully added");
        } else {
            System.out.println(t.toString() + " can not add to WorkDay");
        }
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

