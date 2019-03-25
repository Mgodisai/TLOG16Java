package com.mgodisai;

import java.time.LocalDate;
import java.util.List;

class WorkDay {
    private List<Task> tasks;
    private long requiredMinPerDay;
    private LocalDate actualDay;
    private long sumPerDay;
}
