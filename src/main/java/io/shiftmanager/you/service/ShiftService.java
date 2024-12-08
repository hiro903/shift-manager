package io.shiftmanager.you.service;

import io.shiftmanager.you.model.Shift;
import io.shiftmanager.you.mapper.ShiftMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShiftService {
    // MapperをDI
    private final ShiftMapper shiftMapper;

    // 単一シフトの登録
    @Transactional
    public void registerShift(Shift shift) {
        // 初期状態は未承認
        shift.setStatus("pending");
        shiftMapper.insert(shift);
    }

    // 期間指定でのシフト取得
    public List<Shift> getShiftsByPeriod(Long userId, LocalDate startDate, LocalDate endDate) {
        return shiftMapper.findByUserIdAndDateRange(userId, startDate, endDate);
    }

    // シフトの更新
    @Transactional
    public void updateShift(Shift shift) {
        shiftMapper.update(shift);
    }

    // シフトの削除
    @Transactional
    public void deleteShift(Long shiftId) {
        shiftMapper.delete(shiftId);
    }

    // 曜日指定での一括登録
    @Transactional
    public void bulkRegisterByDayOfWeek(Long userId, DayOfWeek dayOfWeek,
                                        LocalDate startDate, LocalDate endDate,
                                        Shift.TimeZone timezone) {
        List<Shift> shifts = new ArrayList<>();
        LocalDate current = startDate;

        // 指定期間内の特定曜日を全て取得
        while (!current.isAfter(endDate)) {
            if (current.getDayOfWeek() == dayOfWeek) {
                Shift shift = new Shift();
                shift.setUserId(userId);
                shift.setDate(current);
                shift.setTimezone(timezone);
                shift.setStatus("pending");
                shifts.add(shift);
            }
            current = current.plusDays(1);
        }

        if (!shifts.isEmpty()) {
            shiftMapper.bulkInsert(shifts);
        }
    }
}