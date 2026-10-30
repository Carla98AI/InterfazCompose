package cl.duoc.dsy1105.rinconsur;

public record RoomSyncRequest(
        String status,
        Long startedAt,
        int itemCount,
        String category,
        boolean alertSent
) { }
