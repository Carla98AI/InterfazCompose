package cl.duoc.dsy1105.rinconsur;

public record FittingRoom(
        int id,
        String name,
        String status,
        Long startedAt,
        int itemCount,
        String category,
        boolean alertSent
) {
    public FittingRoom withOccupancy(int items, String category, long start) {
        return new FittingRoom(id, name, "OCCUPIED", start, items, category, false);
    }

    public FittingRoom available() {
        return new FittingRoom(id, name, "AVAILABLE", null, 0, "", false);
    }

    public FittingRoom cleaning() {
        return new FittingRoom(id, name, "CLEANING", null, 0, "", false);
    }

    public FittingRoom withAlertSent() {
        return new FittingRoom(id, name, status, startedAt, itemCount, category, true);
    }
}
