package io.shiftmanager.you.mapper;


import io.shiftmanager.you.model.Confirmed;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ConfirmedMapper {
    @Select("SELECT * FROM confirmed WHERE confirmed_id = #{confirmedId}")
    Confirmed getConfirmedById(Long confirmedId);

    @Select("SELECT * FROM confirmed WHERE user_id = #{userId}")
    List<Confirmed> getConfirmedByUserId(Long userId);

    @Insert("INSERT INTO confirmed (user_id, shift_id, confirmed_date, timezone) " +
            "VALUES (#{userId}, #{shiftId}, #{confirmedDate}, #{timezone})")
    @Options(useGeneratedKeys = true, keyProperty = "confirmedId")
    int insertConfirmed(Confirmed confirmed);

    @Delete("DELETE FROM confirmed WHERE confirmed_id = #{confirmedId}")
    int deleteConfirmed(Long confirmedId);
}