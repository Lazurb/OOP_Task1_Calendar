import exception.InvalidDateException;
import model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("ПРОИЗВОДСТВЕННЫЙ КАЛЕНДАРЬ");

        var holidays = createHolidays();
        System.out.printf("%n[1] Праздников: %d%n", holidays.size());
        holidays.forEach(h -> System.out.println("    • " + h.formatted()));

        var vacations = createVacations();
        System.out.printf("%n[2] Периодов каникул: %d%n", vacations.size());
        vacations.forEach(v -> System.out.printf("    • %s — %s%n", v.start(), v.end()));

        var holidayRule = new HolidayTransferRule(holidays);
        var vacationRule = new VacationRule(vacations);
        var weekRule = new StandardWeekRule();
        var calendar = new WorkCalendar(List.of(holidayRule, vacationRule, weekRule));

        System.out.println("\n[3] Правила календаря:");
        System.out.println("    1. " + holidayRule.ruleName());
        System.out.println("    2. " + vacationRule.ruleName());
        System.out.println("    3. " + weekRule.ruleName());

        System.out.println("\n[4] Календарь на январь 2024:");
        System.out.println(calendar.printMonth(2024, 1));

        System.out.println("[5] Определение типа дат:");
        var dates = List.of(
                LocalDate.of(2024, 1, 1),  LocalDate.of(2024, 1, 2),
                LocalDate.of(2024, 1, 6),  LocalDate.of(2024, 1, 7),
                LocalDate.of(2024, 1, 8),  LocalDate.of(2024, 1, 13),
                LocalDate.of(2024, 1, 14), LocalDate.of(2024, 1, 15),
                LocalDate.of(2024, 1, 16), LocalDate.of(2024, 1, 20),
                LocalDate.of(2024, 2, 23), LocalDate.of(2024, 2, 24),
                LocalDate.of(2024, 3, 8),  LocalDate.of(2024, 3, 9),
                LocalDate.of(2024, 3, 28), LocalDate.of(2024, 5, 1),
                LocalDate.of(2024, 5, 9),  LocalDate.of(2024, 6, 12),
                LocalDate.of(2024, 11, 4), LocalDate.of(2024, 12, 31)
        );
        for (var d : dates) {
            System.out.printf("    %s → %s%n", d, calendar.getEntry(d).getType().getDescription());
        }

        System.out.println("\n[6] Январь 2024: " + calendar.monthlySummary(2024, 1));
        System.out.println("[7] Февраль 2024: " + calendar.monthlySummary(2024, 2));
        System.out.println("\n[8] I квартал 2024: " + calendar.quarterlySummary(2024, 1));
        System.out.println("\n[9] 2024 год: " + calendar.annualSummary(2024));

        System.out.println("\n[10] Праздники 2024:");
        calendar.holidaysOfYear(2024).forEach(h -> System.out.println("    • " + h.formatted()));

        System.out.println("\n[11] Записи 1–7 января 2024:");
        calendar.entriesBetween(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 7))
                .forEach(e -> System.out.println("    " + e));

        System.out.println("\n[12] Обработка ошибок:");
        try {
            calendar.getDayType(LocalDate.of(2019, 1, 1));
        } catch (InvalidDateException e) {
            System.out.println("  " + e.getMessage() + " [" + e.getOperationContext() + "]");
        }
        try {
            calendar.quarterlySummary(2024, 5);
        } catch (InvalidDateException e) {
            System.out.println("  " + e.getMessage() + " [" + e.getOperationContext() + "]");
        }
        try {
            calendar.countWorkingDays(LocalDate.of(2024, 2, 1), LocalDate.of(2024, 1, 1));
        } catch (InvalidDateException e) {
            System.out.println("     " + e.getMessage() + " [" + e.getOperationContext() + "]");
        }

        System.out.println("\n[13] equals / hashCode:");
        var e1 = new CalendarEntry(LocalDate.of(2024, 1, 1), DayType.HOLIDAY);
        var e2 = new CalendarEntry(LocalDate.of(2024, 1, 1), DayType.HOLIDAY);
        var e3 = new CalendarEntry(LocalDate.of(2024, 1, 1), DayType.WORKING);
        System.out.println("    e1.equals(e2): " + e1.equals(e2));
        System.out.println("    hash совпадает: " + (e1.hashCode() == e2.hashCode()));
        System.out.println("    e1.equals(e3): " + e1.equals(e3));

        int total = holidays.size() + vacations.size() + dates.size() + 7;
        System.out.printf("%n Объекты: %d%n", total);
        System.out.println(" Готово.");
    }

    private static List<Holiday> createHolidays() {
        var list = new ArrayList<Holiday>();
        list.add(new Holiday(LocalDate.of(2024, 1, 1), "Новый год", false));
        list.add(new Holiday(LocalDate.of(2024, 1, 2), "Новогодние каникулы", false));
        list.add(new Holiday(LocalDate.of(2024, 1, 3), "Новогодние каникулы", false));
        list.add(new Holiday(LocalDate.of(2024, 1, 4), "Новогодние каникулы", false));
        list.add(new Holiday(LocalDate.of(2024, 1, 5), "Новогодние каникулы", false));
        list.add(new Holiday(LocalDate.of(2024, 1, 6), "Рождество Христово", false));
        list.add(new Holiday(LocalDate.of(2024, 1, 7), "Рождество Христово", false));
        list.add(new Holiday(LocalDate.of(2024, 1, 8), "Новогодние каникулы", false));
        list.add(new Holiday(LocalDate.of(2024, 2, 23), "День защитника Отечества", true));
        list.add(new Holiday(LocalDate.of(2024, 3, 8), "Международный женский день", true));
        list.add(new Holiday(LocalDate.of(2024, 5, 1), "Праздник Весны и Труда", true));
        list.add(new Holiday(LocalDate.of(2024, 5, 9), "День Победы", false));
        list.add(new Holiday(LocalDate.of(2024, 6, 12), "День России", true));
        list.add(new Holiday(LocalDate.of(2024, 11, 4), "День народного единства", false));
        list.add(new Holiday(LocalDate.of(2024, 12, 31), "Канун Нового года", false));
        return list;
    }

    private static List<VacationRule.VacationPeriod> createVacations() {
        var list = new ArrayList<VacationRule.VacationPeriod>();
        list.add(new VacationRule.VacationPeriod(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 10)));
        list.add(new VacationRule.VacationPeriod(LocalDate.of(2024, 3, 25), LocalDate.of(2024, 3, 31)));
        list.add(new VacationRule.VacationPeriod(LocalDate.of(2024, 5, 27), LocalDate.of(2024, 5, 31)));
        list.add(new VacationRule.VacationPeriod(LocalDate.of(2024, 6, 1), LocalDate.of(2024, 8, 31)));
        list.add(new VacationRule.VacationPeriod(LocalDate.of(2024, 10, 28), LocalDate.of(2024, 11, 3)));
        return list;
    }
}