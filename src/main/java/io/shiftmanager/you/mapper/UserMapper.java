package io.shiftmanager.you.mapper;


import io.shiftmanager.you.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    // 既存のメソッド（メソッド名を統一）
    @Select("SELECT * FROM users WHERE user_id = #{userId}")
    User getUserById(Long userId);

    @Select("SELECT * FROM users")
    List<User> getAllUsers();

    @Insert("INSERT INTO users (username, password, email, is_active, is_admin) " +
            "VALUES (#{username}, #{password}, #{email}, #{isActive}, #{isAdmin})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    void insert(User user);

    @Update("UPDATE users SET " +
            "username = #{username}, " +
            "email = #{email}, " +
            "is_active = #{isActive}, " +
            "is_admin = #{isAdmin}" +
            // パスワードが設定されている場合のみ更新
            "${password != null ? ', password = #{password}' : ''} " +
            "WHERE user_id = #{userId}")
    void update(User user);

    @Delete("DELETE FROM users WHERE user_id = #{userId}")
    void delete(Long userId);

    // Spring Security用の追加メソッド
    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUsername(String username);

    @Select("SELECT * FROM users WHERE email = #{email}")
    User findByEmail(String email);

    // 追加の便利なメソッド
    @Select("SELECT COUNT(*) FROM users WHERE username = #{username}")
    int countByUsername(String username);

    @Select("SELECT COUNT(*) FROM users WHERE email = #{email}")
    int countByEmail(String email);

    @Update("UPDATE users SET is_active = #{isActive} WHERE user_id = #{userId}")
    void updateActiveStatus(@Param("userId") Long userId, @Param("isActive") boolean isActive);
}