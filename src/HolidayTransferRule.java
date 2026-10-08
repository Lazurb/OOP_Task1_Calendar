
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public final class HolidayTransferRule implements WorkDayRule {

    private final List<Holiday> holidays;

    public HolidayTransferRule(List<Holiday> holidays) {
        this.holidays = List.copyOf(holidays);
    }

    @Override
    public Optional<DayType> resolve(LocalDate date) {
        for (var holiday : holidays) {
            if (holiday.date().equals(date)) {
                return Optional.of(DayType.HOLIDAY);
            }
        }

        var transferred = findTransferredTo(date);
        if (transferred.isPresent()) {
            return Optional.of(DayType.HOLIDAY);
        }

        if (isPreHolidayShortened(date)) {
            return Optional.of(DayType.PRE_HOLIDAY_SHORT);
        }

        return Optional.empty();
    }

    private Optional<Holiday> findTransferredTo(LocalDate date) {
        for (var holiday : holidays) {
            if (!holiday.transferable()) continue;
            var original = holiday.date();
            if (original.getDayOfWeek() == DayOfWeek.SATURDAY
                    || original.getDayOfWeek() == DayOfWeek.SUNDAY) {
                var candidate = original.plusDays(1);
                while (candidate.getDayOfWeek() == DayOfWeek.SATURDAY
                        || candidate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    candidate = candidate.plusDays(1);
                }
                if (candidate.equals(date)) {
                    return Optional.of(holiday);
                }
            }
        }
        return Optional.empty();
    }

    private boolean isPreHolidayShortened(LocalDate date) {
        var nextDay = date.plusDays(1);
        for (var holiday : holidays) {
            if (holiday.date().equals(nextDay) && !holiday.transferable()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String ruleName() {
        return "Перенос праздников";
    }

    public List<Holiday> getHolidays() {
        return holidays;
    }
}