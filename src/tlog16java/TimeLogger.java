package tlog16java;

import java.util.ArrayList;
import java.util.List;

public class TimeLogger {
    private List<WorkMonth> months;

    public TimeLogger() {
        months = new ArrayList<>();
    }

    public List<WorkMonth> getMonths() {
        return months;
    }

    public boolean isNewMonth(WorkMonth wm) {
        for (WorkMonth checkedWorkMonth : months) {
            if (checkedWorkMonth.getDate().getMonthValue()==wm.getDate().getMonthValue()) {
                return false;
            }
        }
        return true;
    }

    public void addMonth(WorkMonth wm) {
        if (isNewMonth(wm)) {
            months.add(wm);
            System.out.println(wm.getDate()+ " sikeresen felvéve!");
        } else {
            System.out.println(wm.getDate()+" már van a listában!");
        }
    }
}
