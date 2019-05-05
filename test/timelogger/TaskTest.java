package timelogger;

import org.junit.jupiter.api.*;
import timelogger.exceptions.*;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    static final LocalTime localTimeNullStartTime = null;
    static final LocalTime localTimeNullEndTime = null;
    static final String stringNullStartTime = null;
    static final String stringNullEndTime = null;
    static final String stringNullTaskID = null;

    @DisplayName("Constructor exception test (TimeOrderException")
    @Test
    void constructorExceptionTest1() {

        assertThrows(NotExpectedTimeOrderException.class, () -> {
            Task task = new Task("1234", "First test task1", "8:45", "7:30");
        });

    }

    @DisplayName("Constructor exception without endTime")
    @Test
    void constructorExceptionTest2() {

        assertThrows(EmptyTimeFieldException.class, () -> {
            Task task = new Task("1234", "First test task1", "8:45", stringNullEndTime);
        });

    }

    @DisplayName("Constructor exception with invalid redmine task1 id")
    @Test
    void constructorExceptionTest3() {

        assertThrows(InvalidTaskIdException.class, () -> {
            Task task = new Task("12345");
        });

    }


    @DisplayName("Constructor exception with invalid LT task1 id")
    @Test
    void constructorExceptionTest4() {

        assertThrows(InvalidTaskIdException.class, () -> {
            Task task2 = new Task("LT1234");
        });

    }

    @DisplayName("Constructor exception without taskID")
    @Test
    void constructorExceptionTest5() {

        assertThrows(NoTaskIdException.class, () -> {
            Task task2 = new Task(null);
        });

    }

    @DisplayName("Testing different methods")
    @Test
    void methodTests() throws NotExpectedTimeOrderException, EmptyTimeFieldException,
            InvalidTimeFieldException, InvalidTaskIdException, NoTaskIdException {

        Task task1 = new Task("1234", "Second test task1", "7:30", "8:45");
        Task task2 = new Task("4567");
        Task task3 = new Task("1234", "", "7:30", "8:45");
        Task task4 = new Task("LT-1232", "", "7:30", "7:50");
        Task task5 = new Task("1234", "", 7, 30, 7, 45);

        assertEquals(75l, task1.getMinPerTask());
        assertThrows(EmptyTimeFieldException.class, task2::getMinPerTask);
        assertTrue(task3.getComment().isEmpty());
        assertEquals(LocalTime.of(7, 45), task4.getEndTime());

        task3.setStartTime(LocalTime.of(7, 40));
        assertEquals(LocalTime.of(8, 40), task3.getEndTime());
        task3.setStartTime("7:50");
        assertEquals(LocalTime.of(8, 35), task3.getEndTime());
        task3.setStartTime(7, 58);
        assertEquals(LocalTime.of(8, 28), task3.getEndTime());
        task1.setEndTime(8, 40);
        assertEquals(LocalTime.of(8, 45), task1.getEndTime());
        task1.setEndTime(LocalTime.of(8, 40));
        assertEquals(LocalTime.of(8, 45), task1.getEndTime());
        task1.setEndTime("8:40");
        assertEquals(LocalTime.of(8, 45), task1.getEndTime());

        assertThrows(NoTaskIdException.class, () -> task1.setTaskId(null));
        assertThrows(InvalidTaskIdException.class, () -> task1.setTaskId("23456"));
        assertThrows(InvalidTaskIdException.class, () -> task1.setTaskId("LT-121a"));
        assertThrows(NotExpectedTimeOrderException.class, () -> task1.setStartTime("13:00"));
        assertThrows(NotExpectedTimeOrderException.class, () -> task1.setEndTime(7, 0));
        task5.setStartTime(7, 00);
        assertEquals(LocalTime.of(7, 00), task5.getStartTime());
        task5.setEndTime(8, 00);
        assertEquals(LocalTime.of(8, 00), task5.getEndTime());

        Task task6 = new Task("LT-9876", "comment like this", "8:30", "16:00");
        assertEquals("LT-9876", task6.getTaskId());
        assertEquals("comment like this", task6.getComment());
        assertEquals(LocalTime.of(8,30), task6.getStartTime());
        assertEquals(LocalTime.of(16,00), task6.getEndTime());
    }
}