package io.shiftmanager.you.model;

import lombok.Data;
import org.apache.ibatis.type.Alias;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Alias("Confirmed")
public class Confirmed {
    private Long confirmedId;
    private Long userId;
    private Long shiftId;
    private LocalDate confirmedDate;
    private Timezone timezone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum Timezone {
        morning, afternoon
    }
}