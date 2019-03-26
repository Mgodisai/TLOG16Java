package tlog16java;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the WorkMonth class
 * @author Tamás Varga
 * @version 0.1.0
 * @since 2019-03-26
 */
public class WorkMonth {
    private List<WorkDay> days;
    private YearMonth date;
    private long sumPerMonth;
    private long requiredMinPerMonth;

    public WorkMonth(int year, int month) {
        this.date = YearMonth.of(year, month);
        this.days = new ArrayList<>();
    }

    public List<WorkDay> getDays() {
        return days;
    }

    public YearMonth getDate() {
        return date;
    }

    public long getSumPerMonth() {
        return sumPerMonth;
    }

    public long getRequiredMinPerMonth() {
        return requiredMinPerMonth;
    }

    public long getExtraMinPerMonth() {
        //getExtraMinPerMonth():long method should calculate, how many extra minutes did the employee work in the actual month
        return 1;
    }

    public boolean isNewDate(WorkDay wd) {
        for (WorkDay checkedWorkDay : days) {
            if (checkedWorkDay.getActualDay().equals(wd.getActualDay())) {
                return false;
            }
        }
        return true;
    }

    public boolean isSameMonth(WorkDay wd) {
        return wd.getActualDay().getMonthValue()==this.date.getMonthValue();
    }

    public void addWorkDay(WorkDay wd, boolean isWeekendEnabled) {

        if (!isSameMonth(wd)) {
            System.out.println("A hozzáadni kívánt munkanap ("+wd.getActualDay() +") nem ebben a hónapban ("+ this.getDate()+ ") van!");
        } else if (!isNewDate(wd)) {
            System.out.println(wd.getActualDay()+" már szerepel ennél a hónapnál!");
        } else if (!wd.isWeekday() && !isWeekendEnabled) {
            System.out.println(wd.getActualDay()+" hétvégére esik, és hétvégi munkavégzés nem engedélyezett!");
        }  else {
            days.add(wd);
            System.out.println(wd.getActualDay()+ " sikeresen hozzáadva: "+this.getDate()+ " hó");
        }
    }

    public void addWorkDay(WorkDay wd) {

        addWorkDay(wd, false);

    }
}
