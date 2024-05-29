package ordersim;

public enum Temperature {
    FROZEN,
    COLD,
    HOT;

    public static Temperature fromString(String temperature) {
        switch (temperature.toLowerCase()) {
            case "hot":
                return HOT;
            case "cold":
                return COLD;
            case "frozen":
                return FROZEN;
            default:
                throw new IllegalArgumentException("Invalid temperature: " + temperature);
        }
    }

}
