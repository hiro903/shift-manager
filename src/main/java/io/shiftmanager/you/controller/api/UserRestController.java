package io.shiftmanager.you.controller.api;


import io.shiftmanager.you.model.User;
import io.shiftmanager.you.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserRestController {

    private final UserService userService;

    // すべてのユーザーを取得
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }


    //IDによるユーザー取得
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    //新規ユーザー情報
    @PostMapping("/{id}")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        if (userService.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("このユーザー名は既に使用されています");
        }
        if (userService.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("このメールアドレスは既に使用されています");
        }
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);

    }

    //ユーザー情報の更新
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody User user) {
        User existingUser = userService.findByEmail(user.getEmail());
        if (existingUser != null && !existingUser.getUserId().equals(id)) {
            throw new IllegalArgumentException("このメールアドレスは既に使用されています");
        }
        user.setUserId(id);
        return ResponseEntity.ok(userService.updateUser(user));
    }

    //ユーザーの削除

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    //ユーザー名による検索
    @GetMapping("/search")
    public ResponseEntity<User> findByUsername(@RequestParam String username) {
        User user = userService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> updateUserStatus(
            @PathVariable Long id,
            @RequestParam boolean isActive) {
        User user = userService.getUserById(id);
        user.setIsActive(isActive);
        return ResponseEntity.ok(userService.updateUser(user));
    }
}
