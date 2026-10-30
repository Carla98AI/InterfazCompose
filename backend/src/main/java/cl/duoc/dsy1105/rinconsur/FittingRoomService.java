package cl.duoc.dsy1105.rinconsur;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.IntStream;

@Service
public class FittingRoomService {
    private final ConcurrentMap<Integer, FittingRoom> rooms = new ConcurrentHashMap<>();

    public FittingRoomService() {
        // Datos ficticios para demostrar el flujo sin información real de la tienda.
        long now = System.currentTimeMillis();
        IntStream.rangeClosed(1, 8).forEach(id -> {
            FittingRoom room = new FittingRoom(id, "Probador %02d".formatted(id), "AVAILABLE", null, 0, "", false);
            if (id == 1) room = room.withOccupancy(4, "Vestuario", now - 36 * 60_000L);
            if (id == 2) room = room.withOccupancy(2, "Calzado", now - 8 * 60_000L);
            if (id == 8) room = new FittingRoom(id, room.name(), "CLEANING", null, 0, "", false);
            rooms.put(id, room);
        });
    }

    public List<FittingRoom> findAll() {
        return rooms.values().stream().sorted(Comparator.comparingInt(FittingRoom::id)).toList();
    }

    public FittingRoom occupy(int id, OccupancyRequest request) {
        // El servidor vuelve a validar los datos y el estado para proteger la operación.
        if (request == null || request.itemCount() < 1 || request.itemCount() > 12
                || request.category() == null || request.category().isBlank() || request.startedAt() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La asignación debe incluir 1 a 12 prendas y una categoría.");
        }
        return rooms.compute(id, (key, room) -> {
            if (room == null) throw missing(id);
            if (!"AVAILABLE".equals(room.status())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "El probador no está disponible.");
            }
            return room.withOccupancy(request.itemCount(), request.category().trim(), request.startedAt());
        });
    }

    public FittingRoom release(int id) {
        return rooms.compute(id, (key, room) -> {
            if (room == null) throw missing(id);
            if (!"OCCUPIED".equals(room.status())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Solo se puede finalizar un probador ocupado.");
            }
            return room.cleaning();
        });
    }

    public FittingRoom finishCleaning(int id) {
        return rooms.compute(id, (key, room) -> {
            if (room == null) throw missing(id);
            if (!"CLEANING".equals(room.status())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "El probador no está marcado en limpieza.");
            }
            return room.available();
        });
    }

    public FittingRoom markAlert(int id) {
        return rooms.compute(id, (key, room) -> {
            if (room == null) throw missing(id);
            return room.withAlertSent();
        });
    }

    public FittingRoom sync(int id, RoomSyncRequest request) {
        if (request == null || request.status() == null
                || !List.of("AVAILABLE", "OCCUPIED", "CLEANING").contains(request.status())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado de probador no reconocido.");
        }
        if ("OCCUPIED".equals(request.status())
                && (request.startedAt() == null || request.startedAt() <= 0
                || request.itemCount() < 1 || request.itemCount() > 12
                || request.category() == null || request.category().isBlank())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La ocupación pendiente está incompleta.");
        }
        return rooms.compute(id, (key, room) -> {
            if (room == null) throw missing(id);
            return new FittingRoom(
                    id, room.name(), request.status(),
                    "OCCUPIED".equals(request.status()) ? request.startedAt() : null,
                    "OCCUPIED".equals(request.status()) ? request.itemCount() : 0,
                    "OCCUPIED".equals(request.status()) ? request.category().trim() : "",
                    request.alertSent()
            );
        });
    }

    private ResponseStatusException missing(int id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el probador " + id + ".");
    }
}
