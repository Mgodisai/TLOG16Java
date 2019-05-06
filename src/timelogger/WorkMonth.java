package timelogger;

import timelogger.exceptions.NotNewDateException;
import timelogger.exceptions.NotTheSameMonthException;
import timelogger.exceptions.WeekendNotEnabledException;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorkMonth implements Comparable<WorkMonth> {
    private List<WorkDay> days;
    private YearMonth date;
    private long sumPerMonth;
    private long requiredMinPerMonth;

    public WorkMonth(int year, int month) {
        this.date = YearMonth.of(year, month);
        this.days = new ArrayList<>();
    }

    List<WorkDay> getDays() {
        return days;
    }

    YearMonth getDate() {
        return date;
    }

    long getSumPerMonth() {
        return this.sumPerMonth = this.days
                .stream()
                .mapToLong(WorkDay::getSumPerDay)
                .sum();
    }

    long getRequiredMinPerMonth() {
        return requiredMinPerMonth = this.days
                .stream()
                .mapToLong(WorkDay::getRequiredMinPerDay)
                .sum();
    }

    long getExtraMinPerMonth() {
        return getSumPerMonth()-getRequiredMinPerMonth();
    }

    private boolean isNewDate(WorkDay wd) {
        return days.stream()
            .filter(day->wd.getActualDay().equals(day.getActualDay()))
            .findAny().isEmpty();
    }

    private boolean isSameMonth(WorkDay wd) {
        return wd.getActualDay().getMonthValue()==this.date.getMonthValue();
    }

    public void addWorkDay(WorkDay wd, boolean isWeekendEnabled) {

        if (!isSameMonth(wd)) {
            throw new NotTheSameMonthException("This day is not in this month");
        }

        if (!isNewDate(wd)) {
            throw new NotNewDateException(wd.getActualDay()+" is already exists in this month!");
        }

        if (!Util.isWeekday(wd.getActualDay()) && !isWeekendEnabled) {
            throw new WeekendNotEnabledException("The specified workday is on weekend and working on weekends is not enabled");
        }

            days.add(wd);
            System.out.println(wd.getActualDay()+ " successfully added!");
            Collections.sort(days);
    }

    public void addWorkDay(WorkDay wd) {

        addWorkDay(wd, false);

    }
    @Override
    public int compareTo(WorkMonth wm) {
        return this.date.compareTo(wm.getDate());
    }

    @Override
    public String toString() {
        return this.date.format(DateTimeFormatter.ofPattern("yyyy-MM"));
    }
}
