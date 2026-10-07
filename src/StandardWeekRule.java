import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Optional;

public final class StandardWeekRule implements WorkDayRule {

    @Override
    public Optional<DayType> resolve(LocalDate date) {
        var dow = date.getDayOfWeek();
        return switch (dow) {
            case SATURDAY, SUNDAY -> Optional.of(DayType.WEEKEND);
            default -> Optional.of(DayType.WORKING);
        };
    }

    @Override
    public String ruleName() {
        return "Стандартная пятидневка";
    }
}