package Data_model;

import java.time.LocalDate;
import java.util.Objects;
public record transfer_holiday(LocalDate from, LocalDate to) {

    public transfer_holiday {
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(to, "to");
        if (from.equals(to)) {
            throw new IllegalArgumentException("Transfer dates must be different");
        }
    }
}
