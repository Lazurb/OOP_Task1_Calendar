package model;

import java.time.LocalDate;
import java.util.Optional;

public sealed interface CalendarRule
        permits StandardWeekRule, HolidayTransferRule, VacationRule {

    Optional<DayType> resolve(LocalDate date);

    String ruleName();
}