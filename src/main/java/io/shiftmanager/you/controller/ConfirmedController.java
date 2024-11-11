package io.shiftmanager.you.controller;

import io.shiftmanager.you.model.Confirmed;
import io.shiftmanager.you.service.ConfirmedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/confirmed")
public class ConfirmedController {

    private final ConfirmedService confirmedService;

    @Autowired
    public ConfirmedController(ConfirmedService confirmedService) {
        this.confirmedService = confirmedService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Confirmed> getConfirmedById(@PathVariable Long id) {
        Confirmed confirmed = confirmedService.getConfirmedById(id);
        return confirmed != null ? ResponseEntity.ok(confirmed) : ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Confirmed>> getConfirmedByUserId(@PathVariable Long userId) {
        List<Confirmed> confirmedList = confirmedService.getConfirmedByUserId(userId);
        return ResponseEntity.ok(confirmedList);
    }

    @PostMapping
    public ResponseEntity<Confirmed> createConfirmed(@RequestBody Confirmed confirmed) {
        Confirmed createdConfirmed = confirmedService.createConfirmed(confirmed);
        return ResponseEntity.ok(createdConfirmed);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConfirmed(@PathVariable Long id) {
        confirmedService.deleteConfirmed(id);
        return ResponseEntity.noContent().build();
    }
}
