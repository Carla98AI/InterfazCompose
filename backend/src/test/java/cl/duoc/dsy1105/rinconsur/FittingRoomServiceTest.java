package cl.duoc.dsy1105.rinconsur;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FittingRoomServiceTest {
    private FittingRoomService service;

    @BeforeEach
    void setUp() { service = new FittingRoomService(); }

    @Test
    void assignsAndReleasesAnAvailableRoom() {
        var assigned = service.occupy(3, new OccupancyRequest(3, "Vestuario", System.currentTimeMillis()));
        assertEquals("OCCUPIED", assigned.status());
        assertEquals(3, assigned.itemCount());
        assertEquals("Vestuario", assigned.category());

        var released = service.release(3);
        assertEquals("CLEANING", released.status());
        assertEquals(0, released.itemCount());
        assertTrue(released.startedAt() == null);
        assertEquals("AVAILABLE", service.finishCleaning(3).status());
    }

    @Test
    void refusesAnInvalidAssignment() {
        assertThrows(ResponseStatusException.class,
                () -> service.occupy(3, new OccupancyRequest(0, "", System.currentTimeMillis())));
    }

    @Test
    void refusesToAssignAnOccupiedRoom() {
        assertThrows(ResponseStatusException.class,
                () -> service.occupy(1, new OccupancyRequest(1, "Vestuario", System.currentTimeMillis())));
    }

    @Test
    void completesCleaningAndRecordsAlertAcknowledgement() {
        assertEquals("AVAILABLE", service.finishCleaning(8).status());
        var marked = service.markAlert(1);
        assertTrue(marked.alertSent());
        assertEquals("OCCUPIED", marked.status());
    }

    @Test
    void appliesAValidatedOfflineSnapshot() {
        var synced = service.sync(3, new RoomSyncRequest(
                "OCCUPIED", System.currentTimeMillis(), 2, "Calzado", false
        ));
        assertEquals("OCCUPIED", synced.status());
        assertEquals(2, synced.itemCount());
        assertEquals("Calzado", synced.category());
    }
}
