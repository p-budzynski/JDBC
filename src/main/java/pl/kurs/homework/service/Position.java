package pl.kurs.homework.service;

public enum Position {
    INTERN("intern"),
    JUNIOR_DEV("junior dev"),
    MID_DEV("mid dev"),
    SENIOR_DEV("senior dev"),
    LEAD_DEV("lead dev");

    private final String dbValue;

    Position(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    public static Position fromDbValue(String dbValue) {
        for (Position position : Position.values()) {
            if (position.dbValue.equalsIgnoreCase(dbValue)) {
                return position;
            }
        }
        throw new IllegalArgumentException("Unknown position: " + dbValue);
    }
}
