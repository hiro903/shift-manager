package io.shiftmanager.you.controller.api;

import io.shiftmanager.you.model.Shift;
import io.shiftmanager.you.service.ShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/shifts")
@RequiredArgsConstructor
public class ShiftRestController {
    private final ShiftService shiftService;

    // 単一シフトの登録
    @PostMapping
    public ResponseEntity<Void> registerShift(@RequestBody Shift shift) {
        shiftService.registerShift(shift);
        return ResponseEntity.ok().build();
    }

    // シフト一覧の取得
    @GetMapping
    public ResponseEntity<List<Shift>> getShifts(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Shift> shifts = shiftService.getShiftsByPeriod(userId, startDate, endDate);
        return ResponseEntity.ok(shifts);
    }

    // シフトの更新
    @PutMapping("/{shiftId}")
    public ResponseEntity<Void> updateShift(
            @PathVariable Long shiftId,
            @RequestBody Shift shift) {
        shift.setShiftId(shiftId);
        shiftService.updateShift(shift);
        return ResponseEntity.ok().build();
    }

    // シフトの削除
    @DeleteMapping("/{shiftId}")
    public ResponseEntity<Void> deleteShift(@PathVariable Long shiftId) {
        shiftService.deleteShift(shiftId);
        return ResponseEntity.noContent().build();
    }

    // 曜日指定での一括登録
    @PostMapping("/bulk")
    public ResponseEntity<Void> bulkRegister(
            @RequestParam Long userId,
            @RequestParam DayOfWeek dayOfWeek,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam Shift.TimeZone timezone) {

        shiftService.bulkRegisterByDayOfWeek(userId, dayOfWeek, startDate, endDate, timezone);
        return ResponseEntity.ok().build();
    }
}