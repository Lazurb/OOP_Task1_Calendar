package model;

import java.time.LocalDate;
import java.util.Objects;

public final class CalendarEntry {

    private final LocalDate date;
    private final DayType type;
    private final String note;

    public CalendarEntry(LocalDate date, DayType type, String note) {
        if (date == null) throw new NullPointerException("Дата не может быть null");
        if (type == null) throw new NullPointerException("Тип дня не может быть null");
        this.date = date;
        this.type = type;
        this.note = note != null ? note : "";
    }

    public CalendarEntry(LocalDate date, DayType type) {
        this(date, type, "");
    }

    public LocalDate getDate() { return date; }
    public DayType getType() { return type; }
    public String getNote() { return note; }
    public int getWorkHours() { return type.getWorkHoursPerDay(); }
    public boolean isWorkingDay() { return type.isWorking(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CalendarEntry that)) return false;
        return Objects.equals(date, that.date) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, type);
    }

    @Override
    public String toString() {
        return String.format("%s: %s%s", date, type.getDescription(),
                note.isEmpty() ? "" : " [" + note + "]");
    }
}