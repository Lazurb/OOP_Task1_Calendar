package model;

import exception.InvalidDateException;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class WorkCalendar {

    private static final LocalDate MIN_DATE = LocalDate.of(2020, 1, 1);
    private static final LocalDate MAX_DATE = LocalDate.of(2030, 12, 31);

    private final List<CalendarRule> rules;
    private final Map<LocalDate, CalendarEntry> cache;

    public WorkCalendar(List<CalendarRule> rules) {
        if (rules == null || rules.isEmpty()) {
            throw new IllegalArgumentException("Календарь должен содержать хотя бы одно правило");
        }
        this.rules = List.copyOf(rules);
        this.cache = new HashMap<>();
    }

    public DayType getDayType(LocalDate date) {
        validateDate(date, "getDayType");
        return computeDayType(date);
    }

    public CalendarEntry getEntry(LocalDate date) {
        validateDate(date, "getEntry");
        return cache.computeIfAbsent(date, d ->
                new CalendarEntry(d, computeDayType(d), ""));
    }

    public String printMonth(int year, int month) {
        validateYearMonth(year, month, "printMonth");
        var ym = YearMonth.of(year, month);
        var sb = new StringBuilder();
        sb.append(String.format("%n Календарь: %s %d %n", monthName(month), year));
        sb.append(String.format("%-12s | %-35s%n", "Дата", "Тип дня"));
        sb.append("-".repeat(50)).append("\n");
        for (int day = 1; day <= ym.lengthOfMonth(); day++) {
            var entry = getEntry(ym.atDay(day));
            sb.append(String.format("%-12s | %-35s%n", entry.getDate(), entry));
        }
        return sb.toString();
    }

    public long countWorkingDays(LocalDate from, LocalDate to) {
        validateRange(from, to, "countWorkingDays");
        long count = 0;
        var cur = from;
        while (!cur.isAfter(to)) {
            if (computeDayType(cur).isWorking()) count++;
            cur = cur.plusDays(1);
        }
        return count;
    }

    public int countWorkingHours(LocalDate from, LocalDate to) {
        validateRange(from, to, "countWorkingHours");
        int hours = 0;
        var cur = from;
        while (!cur.isAfter(to)) {
            hours += computeDayType(cur).getWorkHoursPerDay();
            cur = cur.plusDays(1);
        }
        return hours;
    }

    public WorkingSummary monthlySummary(int year, int month) {
        validateYearMonth(year, month, "monthlySummary");
        var ym = YearMonth.of(year, month);
        var from = ym.atDay(1);
        var to = ym.atEndOfMonth();
        return new WorkingSummary(countWorkingDays(from, to), countWorkingHours(from, to));
    }

    public WorkingSummary quarterlySummary(int year, int quarter) {
        if (quarter < 1 || quarter > 4) {
            throw new InvalidDateException("Номер квартала: 1-4", "quarterlySummary");
        }
        int sm = (quarter - 1) * 3 + 1;
        var from = LocalDate.of(year, sm, 1);
        var to = YearMonth.of(year, sm + 2).atEndOfMonth();
        return new WorkingSummary(countWorkingDays(from, to), countWorkingHours(from, to));
    }

    public WorkingSummary annualSummary(int year) {
        if (year < MIN_DATE.getYear() || year > MAX_DATE.getYear()) {
            throw new InvalidDateException("Год вне диапазона", "annualSummary");
        }
        var from = LocalDate.of(year, 1, 1);
        var to = LocalDate.of(year, 12, 31);
        return new WorkingSummary(countWorkingDays(from, to), countWorkingHours(from, to));
    }

    public List<Holiday> holidaysOfYear(int year) {
        return rules.stream()
                .filter(r -> r instanceof HolidayTransferRule)
                .map(r -> ((HolidayTransferRule) r).getHolidays())
                .flatMap(Collection::stream)
                .filter(h -> h.date().getYear() == year)
                .sorted(Comparator.comparing(Holiday::date))
                .toList();
    }

    public List<CalendarEntry> entriesBetween(LocalDate from, LocalDate to) {
        validateRange(from, to, "entriesBetween");
        var list = new ArrayList<CalendarEntry>();
        var cur = from;
        while (!cur.isAfter(to)) {
            list.add(getEntry(cur));
            cur = cur.plusDays(1);
        }
        return Collections.unmodifiableList(list);
    }

    private DayType computeDayType(LocalDate date) {
        for (var rule : rules) {
            var result = rule.resolve(date);
            if (result.isPresent()) return result.get();
        }
        return DayType.WORKING;
    }

    private void validateDate(LocalDate date, String ctx) {
        if (date == null) throw new InvalidDateException("Дата null", ctx);
        if (date.isBefore(MIN_DATE) || date.isAfter(MAX_DATE))
            throw new InvalidDateException("Дата " + date + " вне диапазона", ctx);
    }

    private void validateRange(LocalDate from, LocalDate to, String ctx) {
        if (from == null || to == null) throw new InvalidDateException("Границы null", ctx);
        if (from.isAfter(to)) throw new InvalidDateException("Начало позже конца", ctx);
        validateDate(from, ctx);
        validateDate(to, ctx);
    }

    private void validateYearMonth(int year, int month, String ctx) {
        if (month < 1 || month > 12) throw new InvalidDateException("Месяц 1-12", ctx);
        if (year < MIN_DATE.getYear() || year > MAX_DATE.getYear())
            throw new InvalidDateException("Год вне диапазона", ctx);
    }

    private static String monthName(int m) {
        return Month.of(m).getDisplayName(TextStyle.FULL_STANDALONE, Locale.forLanguageTag("ru"));
    }

    public record WorkingSummary(long workingDays, int workingHours) {
        @Override
        public String toString() {
            return String.format("Рабочих дней: %d, часов: %d", workingDays, workingHours);
        }
    }
}