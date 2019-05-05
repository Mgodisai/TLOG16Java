package timelogger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import timelogger.exceptions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TimeLoggerTest {

    Task task1;
    WorkDay wd1;
    WorkMonth wm1, wm2;

    @DisplayName("Test tasks")
    @BeforeEach
    void init() throws NotExpectedTimeOrderException, NegativeMinutesOfWorkException,
            EmptyTimeFieldException, FutureWorkException, InvalidTimeFieldException,
            InvalidTaskIdException, NoTaskIdException {

        task1 = new Task("1111", "First test task1", "7:30", "10:30");
        wd1 = new WorkDay(420, 2016, 4,14);
        wm1 = new WorkMonth(2016,4);
        wm2 = new WorkMonth(2016,4);

    }

    @Test
    void methodTests() throws NotSeparatedTimesException, WeekendNotEnabledException,
            NotNewDateException, NotTheSameMonthException, EmptyTimeFieldException, NotNewMonthException {

        wd1.addTask(task1);
        wm1.addWorkDay(wd1);

        TimeLogger tm = new TimeLogger();
        tm.addMonth(wm1);
        assertEquals(task1.getMinPerTask(), tm.getMonths().get(0).getSumPerMonth());
        assertThrows(NotNewMonthException.class, () -> tm.addMonth(wm2));

    }

}