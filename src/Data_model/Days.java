package Data_model;

public enum Days {
    WORKING("Р", "рабочий", true, 8.0),
    WEEKEND("В", "выходной", false, 0.0),
    HOLIDAY("П", "праздник", false, 0.0),
    PRE_HOLIDAY("С", "предпраздничный сокращённый", true, 7.0),
    VACATION("К", "каникулы", false, 0.0);

    private final String code;
    private final String title;
    private final boolean working;
    private final double workingHours;

    Days(String code, String title, boolean working, double workingHours) {
        this.code = code;
        this.title = title;
        this.working = working;
        this.workingHours = workingHours;
    }

    public String code() {
        return code;
    }

    public String title() {
        return title;
    }

    public boolean isWorking() {
        return working;
    }

    public double workingHours() {
        return workingHours;
    }

    @Override
    public String toString() {
        return code;
    }
}