import java.time.LocalDate;

public sealed interface CalendarRule permits StandardWeekRule, HolidayTransferRule, VacationRule {
    java.util.Optional<DayType> resolve(LocalDate date);
    String ruleName();
}