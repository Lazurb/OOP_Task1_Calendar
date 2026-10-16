package test;
import exception.InvalidDateException;
import model.*;
import java.time.LocalDate;
import java.util.List;

public class CalendarTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println(" Запуск тестов Производственного календаря...\n");

        testStandardWeekRule();
        testHolidayRuleIdentifiesHoliday();
        testHolidayTransferRuleTransfersWeekendHoliday();
        testPreHolidayShortDay();
        testVacationRule();
        testWorkCalendarGetEntry();
        testCountWorkingDaysAndHours();
        testMonthlySummary();
        testEntriesBetween();
        testInvalidDateExceptionForOutOfRangeDate();
        testInvalidDateExceptionForInvalidRange();

        System.out.println("\n" + "=".repeat(50));
        System.out.printf(" Пройдено: %d%n", passed);
        System.out.printf(" Провалено: %d%n", failed);
        System.out.println("=".repeat(50));

        if (failed == 0) {
            System.out.println(" Все тесты успешно пройдены");
        } else {
            System.out.println(" Есть ошибки, проверьте вывод выше.");
            System.exit(1);
        }
    }

    private static WorkCalendar createTestCalendar() {
        List<Holiday> holidays = List.of(
                new Holiday(LocalDate.of(2024, 1, 1), "Новый год", false),
                new Holiday(LocalDate.of(2024, 2, 23), "День защитника Отечества", true),
                new Holiday(LocalDate.of(2024, 3, 8), "Международный женский день", true),
                new Holiday(LocalDate.of(2024, 5, 1), "Праздник Весны и Труда", true),
                new Holiday(LocalDate.of(2024, 5, 9), "День Победы", false)
        );
        List<VacationRule.VacationPeriod> vacations = List.of(
                new VacationRule.VacationPeriod(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 10)),
                new VacationRule.VacationPeriod(LocalDate.of(2024, 6, 1), LocalDate.of(2024, 8, 31))
        );

        var holidayRule = new HolidayTransferRule(holidays);
        var vacationRule = new VacationRule(vacations);
        var weekRule = new StandardWeekRule();

        return new WorkCalendar(List.of(holidayRule, vacationRule, weekRule));
    }

    private static void assertEquals(Object expected, Object actual, String testName) {
        if (expected == null && actual == null) {
            pass(testName);
            return;
        }
        if (expected != null && expected.equals(actual)) {
            pass(testName);
        } else {
            fail(testName, "Ожидалось: " + expected + ", но получено: " + actual);
        }
    }

    private static void assertTrue(boolean condition, String testName) {
        if (condition) pass(testName);
        else fail(testName, "Ожидалось true, но получено false");
    }
    

    private static void assertInvalidDateException(Runnable executable, String expectedMessage, String expectedContext, String testName) {
        try {
            executable.run();
            fail(testName, "Ожидалось исключение InvalidDateException, но оно не было выброшено");
        } catch (InvalidDateException e) {
            if (expectedMessage.equals(e.getMessage()) && expectedContext.equals(e.getOperationContext())) {
                pass(testName);
            } else {
                fail(testName, "Сообщение или контекст не совпадают.\n  Ожидалось: '" + expectedMessage + "' ['" + expectedContext + "']\n  Получено:  '" + e.getMessage() + "' ['" + e.getOperationContext() + "']");
            }
        } catch (Exception e) {
            fail(testName, "Ожидалось InvalidDateException, но получено: " + e.getClass().getSimpleName());
        }
    }

    private static void pass(String testName) {
        System.out.println("Тест пройден: " + testName);
        passed++;
    }

    private static void fail(String testName, String message) {
        System.out.println(" Тест не пройден: " + testName);
        System.out.println("  " + message);
        failed++;
    }


    private static void testStandardWeekRule() {
        StandardWeekRule rule = new StandardWeekRule();
        assertEquals(DayType.WEEKEND, rule.resolve(LocalDate.of(2024, 1, 13)).orElse(null), "StandardWeekRule: Суббота");
        assertEquals(DayType.WORKING, rule.resolve(LocalDate.of(2024, 1, 15)).orElse(null), "StandardWeekRule: Понедельник");
    }

    private static void testHolidayRuleIdentifiesHoliday() {
        List<Holiday> holidays = List.of(new Holiday(LocalDate.of(2024, 1, 1), "Новый год", false));
        HolidayTransferRule rule = new HolidayTransferRule(holidays);
        assertEquals(DayType.HOLIDAY, rule.resolve(LocalDate.of(2024, 1, 1)).orElse(null), "HolidayRule: Прямое совпадение праздника");
    }

    private static void testHolidayTransferRuleTransfersWeekendHoliday() {
        List<Holiday> holidays = List.of(new Holiday(LocalDate.of(2024, 3, 9), "Тестовый праздник в субботу", true));
        HolidayTransferRule rule = new HolidayTransferRule(holidays);

        assertEquals(DayType.HOLIDAY, rule.resolve(LocalDate.of(2024, 3, 9)).orElse(null), "TransferRule: Сам праздник в субботу");
        assertEquals(DayType.HOLIDAY, rule.resolve(LocalDate.of(2024, 3, 11)).orElse(null), "TransferRule: Перенос на понедельник");
        assertTrue(rule.resolve(LocalDate.of(2024, 3, 12)).isEmpty(), "TransferRule: Обычный вторник после переноса");
    }

    private static void testPreHolidayShortDay() {
        List<Holiday> holidays = List.of(new Holiday(LocalDate.of(2024, 5, 1), "Праздник Весны и Труда", true));
        HolidayTransferRule rule = new HolidayTransferRule(holidays);
        assertEquals(DayType.PRE_HOLIDAY_SHORT, rule.resolve(LocalDate.of(2024, 4, 30)).orElse(null), "PreHoliday: 30 апреля перед 1 мая");
    }

    private static void testVacationRule() {
        List<VacationRule.VacationPeriod> periods = List.of(
                new VacationRule.VacationPeriod(LocalDate.of(2024, 6, 1), LocalDate.of(2024, 8, 31))
        );
        VacationRule rule = new VacationRule(periods);

        assertEquals(DayType.VACATION, rule.resolve(LocalDate.of(2024, 7, 15)).orElse(null), "VacationRule: Внутри периода каникул");
        assertTrue(rule.resolve(LocalDate.of(2024, 5, 31)).isEmpty(), "VacationRule: Вне периода каникул");
    }

    private static void testWorkCalendarGetEntry() {
        WorkCalendar calendar = createTestCalendar();

        CalendarEntry entryHoliday = calendar.getEntry(LocalDate.of(2024, 1, 1));
        assertEquals(DayType.HOLIDAY, entryHoliday.getType(), "Calendar GetEntry: 1 января - праздник");

        CalendarEntry entryWorking = calendar.getEntry(LocalDate.of(2024, 4, 15));
        assertEquals(DayType.WORKING, entryWorking.getType(), "Calendar GetEntry: 15 апреля - рабочий день");
    }

    private static void testCountWorkingDaysAndHours() {
        WorkCalendar calendar = createTestCalendar();

        LocalDate from = LocalDate.of(2024, 4, 1);
        LocalDate to = LocalDate.of(2024, 4, 5);

        assertEquals(5L, calendar.countWorkingDays(from, to), "CountWorkingDays: 5 дней");
        assertEquals(40, calendar.countWorkingHours(from, to), "CountWorkingHours: 40 часов");
    }

    private static void testMonthlySummary() {
        WorkCalendar calendar = createTestCalendar();
        WorkCalendar.WorkingSummary summary = calendar.monthlySummary(2024, 4);

        assertNotNull(summary, "MonthlySummary: объект не null");
        assertEquals(22L, summary.workingDays(), "MonthlySummary: 22 рабочих дня");
        assertEquals(175, summary.workingHours(), "MonthlySummary: 175 рабочих часов");
    }

    private static void testEntriesBetween() {
        WorkCalendar calendar = createTestCalendar();
        LocalDate from = LocalDate.of(2024, 4, 1);
        LocalDate to = LocalDate.of(2024, 4, 3);

        List<CalendarEntry> entries = calendar.entriesBetween(from, to);

        assertEquals(3, entries.size(), "EntriesBetween: размер списка");
        assertEquals(LocalDate.of(2024, 4, 1), entries.get(0).getDate(), "EntriesBetween: первая дата");
        assertEquals(LocalDate.of(2024, 4, 3), entries.get(2).getDate(), "EntriesBetween: последняя дата");
    }

    private static void testInvalidDateExceptionForOutOfRangeDate() {
        WorkCalendar calendar = createTestCalendar();
        assertInvalidDateException(
                () -> calendar.getDayType(LocalDate.of(2019, 1, 1)),
                "Дата 2019-01-01 вне диапазона",
                "getDayType",
                "Exception: Дата вне диапазона"
        );
    }

    private static void testInvalidDateExceptionForInvalidRange() {
        WorkCalendar calendar = createTestCalendar();
        LocalDate from = LocalDate.of(2024, 2, 1);
        LocalDate to = LocalDate.of(2024, 1, 1);

        assertInvalidDateException(
                () -> calendar.countWorkingDays(from, to),
                "Начало позже конца",
                "countWorkingDays",
                "Exception: Неверный диапазон дат"
        );
    }

    private static void assertNotNull(Object obj, String testName) {
        if (obj != null) pass(testName);
        else fail(testName, "Ожидался не-null объект, но получено null");
    }
}