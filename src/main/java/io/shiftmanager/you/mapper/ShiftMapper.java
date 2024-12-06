package io.shiftmanager.you.mapper;

import io.shiftmanager.you.model.Shift;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ShiftMapper {
    @Insert("INSERT INTO shifts (user_id, date, timezone, status, created_at, updated_at) " +
            "VALUES (#{userId}, #{date}, #{timezone}, #{status}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "shiftId")
    void insert(Shift shift);


    @Select("SELECT * FROM shifts WHERE user_id = #{userId} AND date >= #{startDate} AND date <= #{endDate}")
    List<Shift> findByUserIdAndDateRange(Long userId, LocalDate startDate, LocalDate endDate);

    @Update("UPDATE shifts SET timezone = #{timezone}, status = #{status}, updated_at = NOW() " +
            "WHERE shift_id = #{shiftId}")
    void update(Shift shift);

    @Delete("DELETE FROM shifts WHERE shift_id = #{shiftId}")
    void delete(Long shiftId);

    @Insert({"<script>",
            "INSERT INTO shifts (user_id, date, timezone, status, created_at, updated_at) VALUES ",
            "<foreach collection='shifts' item='shift' separator=','>",
            "(#{shift.userId}, #{shift.date}, #{shift.timezone}, #{shift.status}, NOW(), NOW())",
            "</foreach>",
            "</script>"})
    void bulkInsert(@Param("shifts") List<Shift> shifts);
}

