package Enum;

public enum UrgencyLevel {
    LEVEL_1(1, "Routine check-up"),
    LEVEL_2(2, "Mild symptoms"),
    LEVEL_3(3, "Persistent symptoms"),
    LEVEL_4(4, "Severe discomfort"),
    LEVEL_5(5, "Serious condition"),
    LEVEL_6(6, "Emergency");

    private final int score;
    private final String description;

    UrgencyLevel(int score, String description) {
        this.score = score;
        this.description = description;
    }

    public int getScore() {
        return score;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        // Hiển thị trong combo box: "1 - Routine check-up"
        return score + " - " + description;
    }
}


