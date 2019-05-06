package timelogger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import timelogger.exceptions.EmptyTimeFieldException;
import timelogger.exceptions.FutureWorkException;
import timelogger.exceptions.NegativeMinutesOfWorkException;
import timelogger.exceptions.NotSeparatedTimesException;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class WorkDayTest {

    Task task1, task2, task3, task4, task5, t1, t2;

    @DisplayName("Test task1")
    @BeforeEach
        void init() {
            try {
                task1 = new Task("1111", "First test task1", "7:30", "8:45");
                task2 = new Task("2222", "Second test task1", "8:45", "09:45");
                task3 = new Task("3333", "Third test task1", "9:30", "11:45");
                task4 = new Task("4444", "Fourth test task", "8:30", "9:45");
                task5 = new Task("5555");
                t1 = new Task("1234", "hello", "8:45", "9:50");
                t2 = new Task("2345", "hello2", "8:20", "8:45");
                } catch (Exception e) {

                }
            }

    @DisplayName("Constructor exception with negative requiredMinPerDay")
    @Test
    void constructorExceptionTest1() {

        assertThrows(NegativeMinutesOfWorkException.class, () -> {
            WorkDay day = new WorkDay(-450, 2011,03,25);
        });

    }

    @DisplayName("Constructor exception with future day")
    @Test
    void constructorExceptionTest2() {

        assertThrows(FutureWorkException.class, () -> {
            WorkDay day = new WorkDay(2020,03,25);
        });

    }

    @Test
    void methodTests() throws NegativeMinutesOfWorkException, FutureWorkException, NotSeparatedTimesException, EmptyTimeFieldException {

            WorkDay day = new WorkDay(2019,3,16);
            day.addTask(task1);
            assertEquals(-375, day.getExtraMinPerDay());

            //day without any task
            WorkDay day2 = new WorkDay(2019,3,17);
            assertEquals(-day2.getRequiredMinPerDay(), day2.getExtraMinPerDay());
            assertEquals(0, day2.getSumPerDay());
            assertThrows(NegativeMinutesOfWorkException.class, () -> day2.setRequiredMinPerDay(-450));
            assertThrows(FutureWorkException.class, () -> day2.setActualDay(2019,12,31));
            day2.addTask(task5);
            assertThrows(EmptyTimeFieldException.class, () -> day2.getSumPerDay());

            WorkDay day3 = new WorkDay(2019,3,18);
            day3.addTask(task1);
            day3.addTask(task2);
            assertEquals(135, day3.getSumPerDay());

            WorkDay day4 = new WorkDay(2019,4,23);
            day4.addTask(task1);
            day4.addTask(task3);
            assertEquals(LocalTime.of(11,45), day4.getLatestEndTime());

            //day2 has not any task
            assertNull(day2.getLatestEndTime());

            WorkDay day5 = new WorkDay(2019,4,24);
            day5.addTask(task1);
            assertThrows(NotSeparatedTimesException.class, () -> day5.addTask(task4));

            WorkDay day6 = new WorkDay(500,2019,2,15);
        assertEquals(500, day6.getRequiredMinPerDay());
        assertEquals(LocalDate.of(2019,2,15), day6.getActualDay());

        WorkDay day7 = new WorkDay(2019,2,16);
        assertEquals(450, day7.getRequiredMinPerDay());

      WorkDay day8 = new WorkDay(300);
        assertEquals(LocalDate.now(), day8.getActualDay());
        assertEquals(300, day8.getRequiredMinPerDay());

        WorkDay day9 = new WorkDay();
        assertEquals(LocalDate.now(), day9.getActualDay());
        assertEquals(450, day9.getRequiredMinPerDay());

        day9.setActualDay(2016,9,1);
        assertEquals(LocalDate.of(2016,9,1), day9.getActualDay());
        day9.setRequiredMinPerDay(300);
        assertEquals(300, day9.getRequiredMinPerDay());

        day9.addTask(t1);
        assertThrows(NotSeparatedTimesException.class, () -> day9.addTask(t2));

    }



}