package io.shiftmanager.you.model;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Alias("Shift")
public class Shift {
    private Long shiftId;
    private Long userId;
    private LocalDate date;

    public enum TimeZone {
        morning,
        afternoon
    }

    private TimeZone timezone;
    private String status;  // "pending", "approved", "rejected"
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 一括登録用
    private String dayOfWeek;  // "MONDAY", "TUESDAY" など
}
