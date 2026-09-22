package Data_model;

import java.time.LocalDate;
import java.util.Objects;
public record transfer_holiday(LocalDate from, LocalDate to) {

    public transfer_holiday {
        Objects.requireNonNull(from, "откуда");
        Objects.requireNonNull(to, "куда");
        if (from.equals(to)) {
            throw new IllegalArgumentException("Даты переноса должны быть разными");
        }
    }
}
