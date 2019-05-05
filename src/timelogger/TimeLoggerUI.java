package timelogger;

import timelogger.exceptions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TimeLoggerUI {
    private static TimeLoggerUI instance;
    private Scanner input = new Scanner(System.in);
    private TimeLogger timeLogger;

    private TimeLoggerUI(TimeLogger timeLogger) {
        this.timeLogger = timeLogger;
    }

    public static TimeLoggerUI getInstance(TimeLogger timeLogger) {
        if (instance == null) {
            instance = new TimeLoggerUI(timeLogger);
        }
        return instance;
    }

    private void displayMainMenu() {
        System.out.println("--- TimeLogger Options ---");
        System.out.print(
                "0. Exit\n" +
                        "1. List months\n" +
                        "2. List days\n" +
                        "3. List tasks for a specific day\n" +
                        "4. Add new month: specify year & month with integers\n" +
                        "5. Add day to a specific month\n" +
                        "6. Start a task for a day\n" +
                        "7. Finish a specific task\n" +
                        "8. Delete a task\n" +
                        "9. Modify task\n" +
                        "10. Statistics\n"
        );
        System.out.println("--------------------------");
    }


    public void selectMainMenu() {

        while (true) {
            displayMainMenu();
            switch (intInputMinMax(0, 10, "Please enter a menu item: ")) {
                case 0:
                    System.out.println("bye");
                    System.exit(1);
                    break;
                case 1:
                    listMonths();
                    break;
                case 2:
                    listDays();
                    break;
                case 3:
                    listAllTasks();
                    break;
                case 4:
                    addNewMonth();
                    break;
                case 5:
                    addNewDayToMonth();
                    break;
                case 6:
                    startNewTask();
                    break;
                case 7:
                    finishSpecificTask();
                    break;
                case 8:
                    deleteTask();
                    break;
                case 9:
                    modifyTaskField();
                    break;
                case 10:
                    makeStatistics();
                    break;
                default:
                    System.out.println("Invalid selection, try again!");
            }
        }
    }

    private List<WorkMonth> listMonths() {

        List<WorkMonth> tempWorkMonthsList = timeLogger.getMonths();

        if (tempWorkMonthsList.size() == 0) {
            System.out.println("The TimeLogger does not contain any WorkMonth!");
        } else {
            IntStream
                    .range(0, tempWorkMonthsList.size())
                    .forEach(i -> System.out.println(i + 1 + ": " + tempWorkMonthsList.get(i)));
        }
        return tempWorkMonthsList;
    }

    private WorkMonth listDays() {

        List<WorkMonth> tempWorkMonthsList = listMonths();

        if (tempWorkMonthsList.size() == 0) {
            return null;
        }

        int selection = intInputMinMax(1, tempWorkMonthsList.size(), "Please enter a row number:") - 1;

        WorkMonth tempWorkMonth = tempWorkMonthsList.get(selection);

        if (tempWorkMonth.getDays().isEmpty()) {
            System.out.println("This months does not contain any WorkDay element!");
        } else {
            tempWorkMonth.getDays().forEach(System.out::println);
        }
        return tempWorkMonthsList.get(selection);
    }


    private WorkDay selectWorkDay() {
        WorkMonth tempWorkMonth = listDays();

        if (tempWorkMonth == null || tempWorkMonth.getDays().isEmpty()) {
            return null;
        }

        List<WorkDay> tempWorkDayList = tempWorkMonth.getDays();

        List<Integer> listOfDays = tempWorkDayList
                .stream()
                .map(i -> i.getActualDay().getDayOfMonth())
                .collect(Collectors.toList());

        int selection = intInputList(listOfDays, "Please enter a day number:");

        WorkDay day = tempWorkDayList.stream()
                .filter(d -> d.getActualDay().getDayOfMonth() == selection)
                .findAny().orElse(null);

        if (day == null) {
            System.out.println("Wrong day!");
            return null;
        }
        return day;
    }

    private void listAllTasks() {
        WorkDay day = selectWorkDay();
        if (day == null) {
            System.out.println("Wrong day!");
            return;
        }
        listTasks(day, false);
    }

    private List<Task> listTasks(WorkDay day, boolean isEndTimeNull) {

        if (day == null) {
            System.out.println("Wrong day!");
            return null;
        }

        Predicate<Task> isNull;

        if (isEndTimeNull) {
            isNull = ld -> ld.getEndTime() == null;
        } else {
            isNull = ld -> true;
        }
        List<Task> tempTaskList =
                day.getTasks()
                        .stream()
                        .filter(isNull)
                        .collect(Collectors.toList());

        if (tempTaskList.isEmpty() && !isEndTimeNull) {
            System.out.println("This day does not contain any Task element!");
        } else {
            tempTaskList.sort(Task::compareTo);
            IntStream.range(0, tempTaskList.size())
                    .forEach(i -> System.out.println(i + 1 + ": " + tempTaskList.get(i)));
        }
        return tempTaskList;
    }

    private void addNewMonth() {
        listMonths();
        System.out.println("Add new WorkMonth to TimeLogger...");
        try {
            timeLogger.addMonth(new WorkMonth(intInputMinMax(0, 2999, "year: "),
                    intInputMinMax(1, 12, "month: ")));
        } catch (NotNewMonthException e) {
            System.out.println(e.getMessage());
        }

    }

    private void addNewDayToMonth() {
        WorkMonth tempWorkMonth = listDays();

        if (tempWorkMonth != null) {
            System.out.println("Add new WorkDay to WorkMonth...");
            LocalDate temp = LocalDate.of(
                    tempWorkMonth.getDate().getYear(),
                    tempWorkMonth.getDate().getMonthValue(),
                    intInputMinMax(1, tempWorkMonth.getDate().lengthOfMonth(), "day: ")
            );
            try {
                tempWorkMonth.addWorkDay(new WorkDay(intInputMinMax(0, 1440, "Required working minutes per day: "), temp));
            } catch (NegativeMinutesOfWorkException | FutureWorkException |
                    WeekendNotEnabledException |NotNewDateException | NotTheSameMonthException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    private void startNewTask() {
        WorkDay day = selectWorkDay();

        Task temp = null;

        if (day == null) {
            System.out.println("Day is missing!");
            return;
        }

        listTasks(day, false);

        System.out.println("Start a new task...");
        System.out.println("Enter task ID:");

        while (temp == null) {
            try {
                temp = new Task(input.nextLine());
            } catch (InvalidTaskIdException | NoTaskIdException e) {
                System.out.println(e.getMessage());
                System.out.println("Please enter a valid TaskID (e.g '9543' or 'LT-3212'): ");
            }

        }

        System.out.println("Please add comment to the task (id: " + temp.getTaskId() + ")");
        temp.setComment(input.nextLine());

        LocalTime startTime = null;

        System.out.println("Please add starting time (format: '10:30') to the task (id: " + temp.getTaskId() + ")");
        System.out.println("Latest end time: " + day.getLatestEndTime());

        if (day.getLatestEndTime() != null) {
            System.out.println("Can I use it as StartTime of this new Task?");
            if (isConfirmed()) {
                System.out.println("OK");
                startTime = day.getLatestEndTime();
            }
        }
        while (startTime == null) {
            startTime = localTimeInput("Enter new start time: ");
        }
        try {
            temp.setStartTime(startTime);
        } catch (NotExpectedTimeOrderException | EmptyTimeFieldException e) {
            System.out.println(e.getMessage());
        }
        try {
            day.addTask(temp);
        } catch (NotSeparatedTimesException e) {
            //does not occur due to the above checks
        }
    }


    private void finishSpecificTask() {

        WorkDay day = selectWorkDay();

        if (day == null) {
            System.out.println("No WorkDay selected!");
            return;
        }

        List<Task> emptyEndTimeTasks = listTasks(day, true);

        if (emptyEndTimeTasks == null || emptyEndTimeTasks.isEmpty()) {
            System.out.println("There are no unfinished tasks");
            return;
        }

        int taskSelection = intInputMinMax(1, emptyEndTimeTasks.size(), "Please enter a task row number to finish: ") - 1;

        Task temp = emptyEndTimeTasks.get(taskSelection);

        LocalTime tempTime = null;

        while (tempTime == null) {
            try {
                tempTime = localTimeInput("Enter finish time: ");
                temp.setEndTime(tempTime);

                if (!Util.isSeparatedTime(day.getTasks(), temp)) {

                    tempTime = null;
                }

            } catch (NotExpectedTimeOrderException | EmptyTimeFieldException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(temp+ " task finished!");
    }


    private void deleteTask() {
        WorkDay day = selectWorkDay();
        if (day == null) {
            System.out.println("Day is missing!");
            return;
        }
        List<Task> tasks = listTasks(day, false);

        if (tasks == null) {
            return;
        }
        int taskSelection = intInputMinMax(1, tasks.size(), "Please enter a task row number to delete: ") - 1;
        Task task = tasks.get(taskSelection);
        if (isConfirmed()) {
            System.out.println(task + " was removed!");
            day.getTasks().remove(task);
        } else {
            System.out.println(task + "was not removed");
        }
    }

    private void displayModifyTaskMenu() {
        System.out.println("--- Modifying options ---");
        System.out.print(
                "0. Back to Main\n" +
                        "1. ID\n" +
                        "2. Start time\n" +
                        "3. End time\n" +
                        "4. Comment\n"
        );
        System.out.println("--------------------------");
    }

    private void modifyTaskField() {
        Task task;
        WorkDay day = selectWorkDay();
        if (day == null) {
            System.out.println("Day is missing!");
            return;
        }
        List<Task> tasks = listTasks(day, false);

        if (tasks == null) {
            task = null;
        } else {
            int taskSelection = intInputMinMax(1, tasks.size(), "Please enter a task row number to modify: ") - 1;
            task = tasks.get(taskSelection);

        }
        if (task == null) {
            System.out.println("Task is missing!");
            return;
        }


        int i = 0;
        while (i < 1) {
            System.out.println("Selected task: " + task);
            displayModifyTaskMenu();
            String oldValue, newValue, type;

            switch (intInputMinMax(0, 4, "Please choose the field you want to modify: ")) {
                case 0:
                    i++;
                    break;
                case 1:
                    oldValue = task.getTaskId();
                    type = "TaskID";

                    while (true) {
                        newValue = requestNewValue(type, oldValue);

                        if (newValue != null) {
                            try {
                                task.setTaskId(newValue);
                                System.out.println(type + " has changed");
                                break;
                            } catch (InvalidTaskIdException | NoTaskIdException e) {
                                System.out.println(e.getMessage());
                                System.out.println("Try again?");
                                if (isConfirmed()) continue;
                                System.out.println("No change");
                                break;
                            }
                        } else {
                            System.out.println("Try again?");
                            if (isConfirmed()) continue;
                            System.out.println("No change");
                            break;
                        }
                    }
                    break;

                case 2:
                    String startTime = task.getStartTime().toString();
                    type = "TaskStartTime";
                    String endTime = task.getEndTime().toString();
                    while (true) {

                        newValue = requestNewValue(type, startTime);

                        if (newValue != null) {
                            try {
                                task.setStartTime(newValue);

                                if (Util.isSeparatedTime(day.getTasks(), task)) {
                                    System.out.println(type + " has changed");
                                    break;

                                } else {
                                    System.out.println("StartTime & EndTime are resetted");

                                    task.setStartTime(startTime);
                                    task.setEndTime(endTime);
                                    System.out.println("Try again?");
                                    if (isConfirmed()) continue;

                                    System.out.println("No change");
                                    break;

                                }

                            } catch (EmptyTimeFieldException | InvalidTimeFieldException | NotExpectedTimeOrderException e) {
                                System.out.println(e.getMessage());
                                System.out.println("Try again?");
                                if (isConfirmed()) continue;
                                System.out.println("No change");
                                break;
                            }
                        } else {
                            System.out.println("Try again?");
                            if (isConfirmed()) continue;
                            System.out.println("No change");
                            break;
                        }

                    }
                    break;

                case 3:
                    type = "TaskEndTime";
                    endTime = task.getEndTime().toString();
                    while (true) {

                        newValue = requestNewValue(type, endTime);

                        if (newValue != null) {
                            try {
                                task.setEndTime(newValue);

                                if(Util.isSeparatedTime(day.getTasks(), task)) {
                                    System.out.println(type + " has changed");
                                    break;

                                } else {
                                    System.out.println("EndTime is resetted");

                                    task.setEndTime(endTime);
                                    System.out.println("Try again?");
                                    if (isConfirmed()) continue;

                                    System.out.println("No change");
                                    break;

                                }

                            } catch (EmptyTimeFieldException | InvalidTimeFieldException | NotExpectedTimeOrderException e) {
                                System.out.println(e.getMessage());
                                System.out.println("Try again?");
                                if (isConfirmed()) continue;
                                System.out.println("No change");
                                break;
                            }
                        } else {
                            System.out.println("Try again?");
                            if (isConfirmed()) continue;
                            System.out.println("No change");
                            break;
                        }

                    }
                    break;
                case 4:
                    oldValue = task.getComment();
                    type = "TaskComment";

                    while (true) {
                        newValue = requestNewValue(type, oldValue);

                        if (newValue != null) {
                            task.setComment(newValue);
                            System.out.println(type + " has changed");
                            break;

                        } else {
                            System.out.println("Try again?");
                            if (isConfirmed()) continue;
                            System.out.println("No change");
                            break;
                        }
                    }
                    break;

                default:
                    System.out.println("Invalid option selected, try again");
            }
        }
    }

    private String requestNewValue(String type, String oldValue) {
        String tempValue;
        System.out.println("Modifying " + type);
        System.out.println("Original value: {" + oldValue + "}");
        System.out.print("Enter new value: ");

        Pattern p = Pattern.compile("time");
        if (p.matcher(type.toLowerCase()).find()) {
            tempValue = localTimeInput("").toString();
        } else {
            tempValue = input.nextLine();
        }

        if (tempValue.isBlank()) {
            System.out.println("Original value remains unchanged!");
            return null;
        } else {
            return tempValue;
        }
    }

    private void makeStatistics() {
        List<WorkMonth> tempWorkMonthsList = listMonths();

        if (tempWorkMonthsList.size() == 0) {
            return;
        }
        int selection = intInputMinMax(1, tempWorkMonthsList.size(), "Please enter a row number:") - 1;

        WorkMonth tempWorkMonth = tempWorkMonthsList.get(selection);
        System.out.println("----------------------------");
        System.out.println("Statistics of " + tempWorkMonth.getDate());
        System.out.println("----------------------------");
        System.out.printf("RequiredMinPerMonth: \t% d%n", tempWorkMonth.getRequiredMinPerMonth());
        System.out.printf("SumPerMonth:         \t% d%n", tempWorkMonth.getSumPerMonth());
        System.out.printf("ExtraMinMonth:       \t% d%n", tempWorkMonth.getExtraMinPerMonth());
        System.out.printf("Unfinished tasks:    \t% d%n", tempWorkMonth.getDays().stream()
                .flatMap(d -> d.getTasks().stream())
                .filter(t -> t.getEndTime() == null).count());
        System.out.printf("Total tasks:         \t% d%n", tempWorkMonth.getDays().stream()
                .flatMap(d -> d.getTasks().stream()).count());

        System.out.println("****************************");
        System.out.println("*       Daily breakdown    *");
        System.out.println("****************************");

        IntStream
                .range(0, tempWorkMonth.getDays().size())
                .forEach(i -> System.out.println(tempWorkMonth.getDays().get(i).getActualDay() + "\t" +
                        "requiredMinPerDay:\t" + tempWorkMonth.getDays().get(i).getRequiredMinPerDay() + "\t" +
                        "sumPerDay:\t" + tempWorkMonth.getDays().get(i).getSumPerDay() + "\t" +
                        "extraMinPerDay:\t" + tempWorkMonth.getDays().get(i).getExtraMinPerDay() + "\t" +
                        "unfinished Tasks:\t" + tempWorkMonth.getDays().get(i).getTasks().stream()
                        .filter(t -> t.getEndTime() == null).count() + "\t" +
                        "tasksPerDay: \t" + tempWorkMonth.getDays().get(i).getTasks().stream().count()
                ));
        System.out.println("----------------------------");
    }

    private int intInput() {
        int selection;

        while (true) {
            try {
                selection = input.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Mismatched input, try again!");
                continue;
            } finally {
                input.nextLine();
            }
            return selection;
        }
    }

    private int intInputMinMax(int min, int max, String message) {
        int selection;
        while (true) {
            if (!message.isBlank()) {
                System.out.println(message);
            }

            selection = intInput();

            if (selection < min || selection > max) {
                System.out.println("Invalid number, try again (" + min + "-" + max + ")!");
            } else {
                break;
            }
        }
        return selection;
    }

    private int intInputList(List<Integer> integerList, String message) {
        int selection;

        while (true) {
            if (!message.isEmpty()) {
                System.out.println(message);
            }

            selection = intInput();

            if (!integerList.contains(selection)) {
                System.out.println("Invalid number, try again! (Valid numbers: " + integerList.toString() + ")!");
                continue;
            }
            return selection;
        }
    }

//    private int intInput2() {
//        int selection;
//        String emp;
//
//        while (true) {
//
//            try {
//                emp = input.nextLine();
//            } catch (NoSuchElementException e) {
//                System.out.println("Mismatched input, try again!");
//                continue;
//            }
//
//            if (emp.isEmpty() ||
//                    emp.equals(" ") ||
//                    emp.split(" ").length>1 ||
//                    !emp.trim().chars().allMatch(Character::isDigit)) {
//                System.out.println("Invalid input");
//            } else {
//                selection = Integer.parseInt(emp.trim());
//                break;
//            }
//        }
//        return selection;
//    }

    private LocalTime localTimeInput(String message) {

        String emp;
        String pattern = "^(0[0-9]|1[0-9]|2[0-3]|[0-9]):[0-5][0-9]$";

        while (true) {

            if (!message.isEmpty()) {
                System.out.println(message);
            } else message = "Invalid date, try again with the following format: hh:mm";

            try {
                emp = input.nextLine();

            } catch (NoSuchElementException e) {
                System.out.println("Mismatched input, try again!");
                continue;
            }

            if (emp.isEmpty() || emp.equals(" ")) {
                continue;
            }
            if (emp.matches(pattern)) {
                return LocalTime.of(Integer.parseInt(emp.split(":")[0]), Integer.parseInt(emp.split(":")[1]));
            }
        }
    }

    private boolean isConfirmed() {

        String emp;
        String pattern = "[y|n]";

        while (true) {
            System.out.println("Please confirm: (y)es or (n)o");
            try {
                emp = input.nextLine();

            } catch (NoSuchElementException e) {
                System.out.println("Mismatched input, try again!");
                continue;
            }

            if (emp.isEmpty() || emp.equals(" ")) {
                continue;
            }
            if (emp.toLowerCase().matches(pattern)) {
                return emp.toLowerCase().equals("y");
            }
        }
    }
}
