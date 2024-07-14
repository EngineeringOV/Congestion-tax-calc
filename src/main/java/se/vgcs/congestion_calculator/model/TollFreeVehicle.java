package se.vgcs.congestion_calculator.model;

public enum TollFreeVehicle {
    MOTORCYCLE("Motorcycle"),
    TRACTOR("Tractor"),
    EMERGENCY("Emergency"),
    DIPLOMAT("Diplomat"),
    FOREIGN("Foreign"),
    MILITARY("Military");

    private final String type;

    TollFreeVehicle(String type) {
        this.type = type;
    }

    public static boolean isTollFreeVehicle(String vehicle) {
        for (TollFreeVehicle v : values()) {
            if (v.type.equalsIgnoreCase(vehicle)) {
                return true;
            }
        }
        return false;
    }
}