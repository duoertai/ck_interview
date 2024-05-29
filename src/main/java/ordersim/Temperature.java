package ordersim;

public enum Temperature {
    FROZEN,
    COLD,
    HOT;

    public static Temperature fromString(String s) {
        return switch (s.toLowerCase()) {
            case "frozen" -> FROZEN;
            case "cold" -> COLD;
            case "hot" -> HOT;
            default -> throw new IllegalArgumentException("Invalid temperature: " + s);
        };
    }
}
