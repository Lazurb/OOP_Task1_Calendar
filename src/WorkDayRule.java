import java.time.LocalDate;

public sealed interface WorkDayRule permits StandardWeekRule, HolidayTransferRule, SchoolVacationRule {
    java.util.Optional<DayType> resolve(LocalDate date);
    String ruleName();
}