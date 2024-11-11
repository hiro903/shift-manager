package io.shiftmanager.you.controller;

import io.shiftmanager.you.model.User;
import io.shiftmanager.you.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private List<User> testUsers;

    @BeforeEach
    void setUp() {
        // テストデータの準備
        testUser = new User();
        testUser.setUserId(1L);
        testUser.setUsername("testUser");
        testUser.setEmail("test@example.com");

        User user2 = new User();
        user2.setUserId(2L);
        user2.setUsername("testUser2");
        user2.setEmail("test2@example.com");

        testUsers = Arrays.asList(testUser, user2);
    }

    @Test
    void getUserById_Success() throws Exception {
        // UserServiceのモック設定
        when(userService.getUserById(1L)).thenReturn(testUser);

        // APIリクエストの実行と検証
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void getAllUsers_Success() throws Exception {
        // UserServiceのモック設定
        when(userService.getAllUsers()).thenReturn(testUsers);

        // APIリクエストの実行と検証
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].userId").value(1))
                .andExpect(jsonPath("$[1].userId").value(2));
    }

    @Test
    void createUser_Success() throws Exception {
        // UserServiceのモック設定
        when(userService.createUser(any(User.class))).thenReturn(testUser);

        // APIリクエストの実行と検証
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.username").value("testUser"));
    }

    @Test
    void updateUser_Success() throws Exception {
        // UserServiceのモック設定
        when(userService.updateUser(any(User.class))).thenReturn(testUser);

        // APIリクエストの実行と検証
        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.username").value("testUser"));
    }

    @Test
    void deleteUser_Success() throws Exception {
        // UserServiceのモック設定
        doNothing().when(userService).deleteUser(1L);

        // APIリクエストの実行と検証
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }
}