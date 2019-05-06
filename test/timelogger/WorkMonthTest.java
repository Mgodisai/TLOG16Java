package timelogger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import timelogger.exceptions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WorkMonthTest {

    Task task1, task2, task3;

    WorkDay wd1, wd2, wd2b, wd3, wd4, wd5, wd6;

    @DisplayName("Test tasks")
    @BeforeEach
    void init() throws NotExpectedTimeOrderException, NegativeMinutesOfWorkException,
            EmptyTimeFieldException, FutureWorkException, InvalidTimeFieldException,
            InvalidTaskIdException, NoTaskIdException {

            task1 = new Task("1111", "First test task1", "7:30", "8:45");
            task2 = new Task("2222", "Second test task1", "8:45", "09:45");
            task3 = new Task("2222");

            wd1 = new WorkDay(420, 2016, 9,12);
            wd2 = new WorkDay(420, 2016, 9,1);
            wd2b = new WorkDay(420, 2016, 9,1);

            wd3 = new WorkDay(420, 2019,4,1);
            wd4 = new WorkDay(420, 2019, 4, 30);

            wd5 = new WorkDay(2016,8,28);
            wd6 = new WorkDay(2019, 3, 1);

    }
    @Test
    void methodTests() throws NotSeparatedTimesException, WeekendNotEnabledException,
            NotNewDateException, NotTheSameMonthException {

        wd1.addTask(task1);
        wd2.addTask(task2);

        WorkMonth wm1 = new WorkMonth(2016, 9);
        wm1.addWorkDay(wd1);
        wm1.addWorkDay(wd2);
        assertEquals(135, wm1.getSumPerMonth());
        assertEquals(-705, wm1.getExtraMinPerMonth());
        assertThrows(NotNewDateException.class, () -> wm1.addWorkDay(wd2b));
        assertThrows(NotTheSameMonthException.class, () -> wm1.addWorkDay(wd5));

        WorkMonth wm2 = new WorkMonth(2019,3);
        assertEquals(0, wm2.getSumPerMonth());
        assertEquals(0, wm2.getExtraMinPerMonth());
        assertEquals(0, wm2.getRequiredMinPerMonth());

        wd6.addTask(task3);
        wm2.addWorkDay(wd6);
        assertThrows(EmptyTimeFieldException.class, () -> wm2.getSumPerMonth());

        WorkMonth wm3 = new WorkMonth(2019, 4);
        wm3.addWorkDay(wd3);
        wm3.addWorkDay(wd4);
        assertEquals(840, wm3.getRequiredMinPerMonth());

        WorkMonth wm4 = new WorkMonth(2016,9);
        wm4.addWorkDay(wd1);
        assertEquals(wd1.getSumPerDay(), wm4.getSumPerMonth());


        WorkMonth wm5 = new WorkMonth(2016,8);
        wm5.addWorkDay(wd5, true);
        assertEquals(wd5.getSumPerDay(), wm5.getSumPerMonth());

        WorkMonth wm6 = new WorkMonth(2016,8);

        assertThrows(WeekendNotEnabledException.class, () -> wm6.addWorkDay(wd5, false));
    }
}