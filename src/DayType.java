
public enum DayType {

    WORKING("Рабочий", 8),
    WEEKEND("Выходной", 0),
    HOLIDAY("Праздник", 0),
    PRE_HOLIDAY_SHORT("Предпраздничный сокращённый", 7),
    SCHOOL_VACATION("Школьные каникулы", 0);

    private final String description;
    private final int workHoursPerDay;

    DayType(String description, int workHoursPerDay) {
        this.description = description;
        this.workHoursPerDay = workHoursPerDay;
    }

    public String getDescription() {
        return description;
    }

    public int getWorkHoursPerDay() {
        return workHoursPerDay;
    }

    public boolean isWorking() {
        return this == WORKING || this == PRE_HOLIDAY_SHORT;
    }

    @Override
    public String toString() {
        return description + " (" + workHoursPerDay + " ч.)";
    }
}