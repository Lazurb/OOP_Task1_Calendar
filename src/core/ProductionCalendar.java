package core;


import Data_model.Days;
import Data_model.Holiday;
import Data_model.Vacation;
import Data_model.transfer_holiday;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


public class ProductionCalendar {

    private final int year;
    private final List<Holiday> holidays;
    private final List<transfer_holiday> transfers;
    private final List<Vacation> vacations;

    private final Map<LocalDate, Days> overrides = new HashMap<>();

    private final Map<LocalDate, Days> cache = new HashMap<>();

    public ProductionCalendar(int year,
                              List<Holiday> holidays,
                              List<transfer_holiday> transfers,
                              List<Vacation> vacations) {
        if (year < 1900 || year > 9999) {
            throw new IllegalArgumentException("Некорректный год: " + year);
        }
        this.year = year;
        this.holidays = new ArrayList<>(Objects.requireNonNull(holidays));
        this.transfers = new ArrayList<>(Objects.requireNonNull(transfers));
        this.vacations = new ArrayList<>(Objects.requireNonNull(vacations));

        // Проверим, что все праздники и переносы относятся к этому году
        for (Holiday h : this.holidays) {
            if (h.date().getYear() != year) {
                throw new IllegalArgumentException("Праздник вне года календаря: " + h.date());
            }
        }
        for (transfer_holiday t : this.transfers) {
            if (t.from().getYear() != year || t.to().getYear() != year) {
                throw new IllegalArgumentException("Перенос вне года календаря: " + t);
            }
        }
    }

    public int year() {
        return year;
    }

    public List<Holiday> holidays() {
        return Collections.unmodifiableList(holidays);
    }

    public List<transfer_holiday> transfers() {
        return Collections.unmodifiableList(transfers);
    }

    public List<Vacation> vacations() {
        return Collections.unmodifiableList(vacations);
    }

    public void overrideDay(LocalDate date, Days type) {
        requireSameYear(date);
        overrides.put(date, Objects.requireNonNull(type));
        cache.remove(date);
    }

    public void clearOverrides() {
        overrides.clear();
        cache.clear();
    }

    public Days dayType(LocalDate date) {
        requireSameYear(date);

        Days cached = cache.get(date);
        if (cached != null) {
            return cached;
        }

        Days result = computeDayType(date);
        cache.put(date, result);
        return result;
    }

    private Days computeDayType(LocalDate date) {
        Days override = overrides.get(date);
        if (override != null) {
            return override;
        }


        for (Vacation v : vacations) {
            if (v.contains(date)) {
                return Days.VACATION;
            }
        }

        if (isHoliday(date)) {
            return Days.HOLIDAY;
        }

        if (isTransferredWeekend(date)) {
            return Days.WEEKEND;
        }

        DayOfWeek dow = date.getDayOfWeek();
        if (dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY) {
            return Days.WEEKEND;
        }

        if (isPreHoliday(date)) {
            return Days.PRE_HOLIDAY;
        }

        return Days.WORKING;
    }

    public boolean isHoliday(LocalDate date) {
        for (Holiday h : holidays) {
            if (h.date().equals(date)) {
                return true;
            }
        }
        return false;
    }

    public Holiday holidayOn(LocalDate date) {
        for (Holiday h : holidays) {
            if (h.date().equals(date)) {
                return h;
            }
        }
        return null;
    }

    public boolean isTransferredWeekend(LocalDate date) {
        for (transfer_holiday t : transfers) {
            if (t.to().equals(date)) {
                return true;
            }
        }
        return false;
    }

    public boolean isTransferredFrom(LocalDate date) {
        for (transfer_holiday t : transfers) {
            if (t.from().equals(date)) {
                return true;
            }
        }
        return false;
    }

    public boolean isPreHoliday(LocalDate date) {
        LocalDate next = date.plusDays(1);
        if (next.getYear() != year) {
            return false;
        }
        if (!isHoliday(next)) {
            return false;
        }
        DayOfWeek dow = date.getDayOfWeek();
        if (dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY) {
            return false;
        }
        return true;
    }

    public List<LocalDate> allDates() {
        List<LocalDate> result = new ArrayList<>(365 + 1);
        LocalDate d = LocalDate.of(year, Month.JANUARY, 1);
        LocalDate end = LocalDate.of(year, Month.DECEMBER, 31);
        while (!d.isAfter(end)) {
            result.add(d);
            d = d.plusDays(1);
        }
        return result;
    }

    public Set<LocalDate> workingDates() {
        Set<LocalDate> result = new HashSet<>();
        for (LocalDate d : allDates()) {
            if (dayType(d).isWorking()) {
                result.add(d);
            }
        }
        return result;
    }

    private void requireSameYear(LocalDate date) {
        if (date.getYear() != year) {
            throw new IllegalArgumentException(
                    "Дата " + date + " не относится к " + year + " году");
        }
    }
}