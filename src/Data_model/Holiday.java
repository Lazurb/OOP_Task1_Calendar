package Data_model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Objects;

public record Holiday(LocalDate date, String name, boolean transferred) {

    public Holiday {
        Objects.requireNonNull(date, "дата");
        Objects.requireNonNull(name, "название");
        if (name.isBlank()) {
            throw new IllegalArgumentException("Название праздника не указано");
        }
    }

    public DayOfWeek dayOfWeek() {
        return date.getDayOfWeek();
    }
}
