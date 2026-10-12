package model;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public final class VacationRule implements CalendarRule {

    private final List<VacationPeriod> periods;

    public record VacationPeriod(LocalDate start, LocalDate end) {
        public VacationPeriod {
            if (start == null || end == null) {
                throw new IllegalArgumentException("Даты каникул не могут быть null");
            }
            if (start.isAfter(end)) {
                throw new IllegalArgumentException("Начало каникул позже конца");
            }
        }

        public boolean contains(LocalDate date) {
            return !date.isBefore(start) && !date.isAfter(end);
        }
    }

    public VacationRule(List<VacationPeriod> periods) {
        this.periods = List.copyOf(periods);
    }

    @Override
    public Optional<DayType> resolve(LocalDate date) {
        for (var period : periods) {
            if (period.contains(date)) {
                return Optional.of(DayType.VACATION);
            }
        }
        return Optional.empty();
    }

    @Override
    public String ruleName() {
        return "Каникулы";
    }
}