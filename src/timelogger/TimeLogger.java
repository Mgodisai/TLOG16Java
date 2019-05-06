package timelogger;

import timelogger.exceptions.NotNewMonthException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class TimeLogger {
    private final List<WorkMonth> months;

    public TimeLogger() {
        months = new ArrayList<>();
    }

    public List<WorkMonth> getMonths() {
        return months;
    }

    private boolean isNewMonth(WorkMonth wm) {

       return months.stream()
               .filter(month->wm.getDate().getMonthValue()==month.getDate().getMonthValue() &&
                       wm.getDate().getYear()==month.getDate().getYear())
               .findAny()
               .isEmpty();
    }

    public void addMonth(WorkMonth wm) {
        if (isNewMonth(wm)) {
            months.add(wm);
            System.out.println(wm.getDate()+ " successfuly added!");
            Collections.sort(months);
        } else {
            throw new NotNewMonthException(wm.toString()+" is already exists in timelogger");
        }
    }


}
