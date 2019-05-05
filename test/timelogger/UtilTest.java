package timelogger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import timelogger.exceptions.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest {
    List<Task> list1, list2, list3, list4;

    @BeforeEach
    void init() throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, InvalidTimeFieldException, NoTaskIdException {

            list1 = new ArrayList<>();
            list1.add(new Task("1111", "First test task", "6:30", "6:45"));

            list2 = new ArrayList<>();
            list2.add(new Task("2222", "Second test task", "6:30", "6:30"));

            list3 = new ArrayList<>();
            list3.add(new Task("3333", "Third test task", "6:30", "7:00"));

            list4 = new ArrayList<>();
            list4.add(new Task("4444", "Fourth test task", "6:30", "7:30"));
    }

    @Test
    void roundToMultipleQuarterHourTest() {
        LocalTime t = Util.roundToMultipleQuarterHour(LocalTime.of(7,30), LocalTime.of(7,50));
        assertEquals(LocalTime.of(7,45), t);

    }

    @Test
    void isMultipleQuarterHourTest() throws EmptyTimeFieldException, NotExpectedTimeOrderException {
        assertFalse(Util.isMultipleQuarterHour(LocalTime.of(7,30), LocalTime.of(7,50)));
        assertTrue(Util.isMultipleQuarterHour(LocalTime.of(7,30), LocalTime.of(7,45)));
    }

    @Test
        void isMultipleQuarterHourExceptionTest() {

        assertThrows(EmptyTimeFieldException.class, () -> {
            Util.isMultipleQuarterHour(null, LocalTime.of(7,50));
        });

        assertThrows(NotExpectedTimeOrderException.class, () -> {
            Util.isMultipleQuarterHour(LocalTime.of(8,30), LocalTime.of(7,45));
        });
    }

    @Test
    void isSeparatedTimeTest() throws NotExpectedTimeOrderException, EmptyTimeFieldException, InvalidTaskIdException, InvalidTimeFieldException, NoTaskIdException, NotSeparatedTimesException {
        list1 = new ArrayList<>();
        list1.add(new Task("1111", "First test task", "6:30", "6:45"));

        list2 = new ArrayList<>();
        list2.add(new Task("2222", "Second test task", "6:30", "6:30"));

        list3 = new ArrayList<>();
        list3.add(new Task("3333", "Third test task", "6:30", "7:00"));

        list4 = new ArrayList<>();
        list4.add(new Task("4444", "Fourth test task", "6:30", "7:30"));

        assertTrue(Util.isSeparatedTime(list1, new Task("9999", "", "5:30", "6:30")));
        assertTrue(Util.isSeparatedTime(list1, new Task("9999", "", "6:45", "7:00")));
        assertTrue(Util.isSeparatedTime(list2, new Task("9999", "", "5:30", "6:30")));
        assertTrue(Util.isSeparatedTime(list4, new Task("9999", "", "7:30", "7:30")));
        assertFalse(Util.isSeparatedTime(list3, new Task("9999", "", "6:00", "6:45")));
        assertFalse(Util.isSeparatedTime(list3, new Task("9999", "", "6:30", "6:45")));
        assertFalse(Util.isSeparatedTime(list3, new Task("9999", "", "6:45", "7:15")));
        assertFalse(Util.isSeparatedTime(list3, new Task("9999", "", "6:45", "7:00")));

        // Szerintem true
        assertTrue(Util.isSeparatedTime(list2, new Task("9999", "", "6:30", "7:00")));
        assertTrue(Util.isSeparatedTime(list4, new Task("9999", "", "6:30", "6:30")));



    }


}