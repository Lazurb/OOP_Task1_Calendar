package Data_model;
import java.time.LocalDate;
import java.util.Objects;

    public record Vacation(LocalDate start, LocalDate end) {

        public Vacation {
            Objects.requireNonNull(start, "начало");
            Objects.requireNonNull(end, "конец");
            if (end.isBefore(start)) {
                throw new IllegalArgumentException("Отпуск не заканчивается раньше начала");
            }
        }

        public boolean contains(LocalDate date) {
            return !date.isBefore(start) && !date.isAfter(end);
        }
    }

