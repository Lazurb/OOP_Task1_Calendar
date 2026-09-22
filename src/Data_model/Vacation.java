package Data_model;
import java.time.LocalDate;
import java.util.Objects;

    public record Vacation(LocalDate start, LocalDate end) {

        public Vacation {
            Objects.requireNonNull(start, "start");
            Objects.requireNonNull(end, "end");
            if (end.isBefore(start)) {
                throw new IllegalArgumentException("Vacation end must not be before start");
            }
        }

        public boolean contains(LocalDate date) {
            return !date.isBefore(start) && !date.isAfter(end);
        }
    }

