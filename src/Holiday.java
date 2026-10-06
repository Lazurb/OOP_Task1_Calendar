import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public record Holiday(LocalDate date, String name, boolean transferable) {

    public Holiday {
        if (date == null) throw new IllegalArgumentException("Дата праздника не может быть null");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Название праздника не может быть пустым");
    }

    public String formatted() {
        var dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.FULL_STANDALONE, Locale.forLanguageTag("ru"));
        return String.format("%s (%s) — %s%s",
                date,
                dayOfWeek,
                name,
                transferable ? " [переносится]" : "");
    }
}