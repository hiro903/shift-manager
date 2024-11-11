package io.shiftmanager.you.mapper;

import io.shiftmanager.you.model.User;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    private User createTestUser(String username, String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword("testPassword");
        user.setEmail(email);
        user.setIsActive(true);
        user.setIsAdmin(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }

    @Test
    @Transactional
    public void testInsertAndGetUser() {
        // テスト用ユーザーの作成
        User user = createTestUser("testUser", "test@example.com");

        // ユーザーの挿入
        userMapper.insertUser(user);
        assertThat(user.getUserId()).isNotNull();

        // 挿入したユーザーの取得
        User retrievedUser = userMapper.getUserById(user.getUserId());

        // 検証
        assertThat(retrievedUser).isNotNull();
        assertThat(retrievedUser.getUsername()).isEqualTo("testUser");
        assertThat(retrievedUser.getEmail()).isEqualTo("test@example.com");
        assertThat(retrievedUser.isActive()).isTrue();
        assertThat(retrievedUser.isAdmin()).isFalse();
        assertThat(retrievedUser.getCreatedAt()).isNotNull();
        assertThat(retrievedUser.getUpdatedAt()).isNotNull();
    }

    @Test
    @Transactional
    public void testGetAllUsers() {
        // 複数のテストユーザーを作成
        User user1 = createTestUser("user1", "user1@example.com");
        User user2 = createTestUser("user2", "user2@example.com");
        user2.setIsAdmin(true);

        // ユーザーの挿入
        userMapper.insertUser(user1);
        userMapper.insertUser(user2);

        // 全ユーザーの取得
        List<User> users = userMapper.getAllUsers();

        // 検証
        assertThat(users).isNotEmpty();
        assertThat(users).hasSizeGreaterThanOrEqualTo(2);

        // 取得したユーザーの内容を検証
        User foundUser1 = users.stream()
                .filter(u -> u.getEmail().equals("user1@example.com"))
                .findFirst()
                .orElse(null);
        User foundUser2 = users.stream()
                .filter(u -> u.getEmail().equals("user2@example.com"))
                .findFirst()
                .orElse(null);

        assertThat(foundUser1).isNotNull();
        assertThat(foundUser2).isNotNull();
        assertThat(foundUser2.isAdmin()).isTrue();
    }

    @Test
    @Transactional
    public void testUpdateUser() {
        // テスト用ユーザーの作成と挿入
        User user = createTestUser("originalName", "original@example.com");
        userMapper.insertUser(user);
        Long userId = user.getUserId();

        // 更新前の状態を保存
        LocalDateTime originalCreatedAt = user.getCreatedAt();

        // 少し待ってから更新（タイムスタンプの差を確実にするため）
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // ユーザー情報の更新
        user.setUsername("updatedName");
        user.setEmail("updated@example.com");
        user.setIsAdmin(true);
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateUser(user);

        // 更新されたユーザーの取得と検証
        User updatedUser = userMapper.getUserById(userId);

        assertThat(updatedUser.getUsername()).isEqualTo("updatedName");
        assertThat(updatedUser.getEmail()).isEqualTo("updated@example.com");
        assertThat(updatedUser.isAdmin()).isTrue();
        assertThat(updatedUser.getCreatedAt()).isEqualTo(originalCreatedAt);
        assertThat(updatedUser.getUpdatedAt()).isAfter(originalCreatedAt);
    }

    @Test
    @Transactional
    public void testDeleteUser() {
        // テスト用ユーザーの作成と挿入
        User user = createTestUser("deleteTest", "delete@example.com");
        userMapper.insertUser(user);
        Long userId = user.getUserId();

        // 削除前の存在確認
        assertThat(userMapper.getUserById(userId)).isNotNull();

        // ユーザーの削除
        int result = userMapper.deleteUser(userId);

        // 検証
        assertThat(result).isEqualTo(1);
        assertThat(userMapper.getUserById(userId)).isNull();
    }
}