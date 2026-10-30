package cl.duoc.dsy1105.rinconsur;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/probadores")
@CrossOrigin(origins = "*")
public class FittingRoomController {
    private final FittingRoomService service;

    public FittingRoomController(FittingRoomService service) { this.service = service; }

    @GetMapping
    public List<FittingRoom> getRooms() { return service.findAll(); }

    @PutMapping("/{id}/ocupar")
    public FittingRoom occupy(@PathVariable int id, @RequestBody OccupancyRequest request) {
        return service.occupy(id, request);
    }

    @PutMapping("/{id}/liberar")
    public FittingRoom release(@PathVariable int id) { return service.release(id); }

    @PutMapping("/{id}/limpieza-completa")
    public FittingRoom finishCleaning(@PathVariable int id) { return service.finishCleaning(id); }

    @PutMapping("/{id}/alerta")
    public FittingRoom markAlert(@PathVariable int id) { return service.markAlert(id); }

    @PutMapping("/{id}/sync")
    public FittingRoom sync(@PathVariable int id, @RequestBody RoomSyncRequest request) {
        return service.sync(id, request);
    }
}
