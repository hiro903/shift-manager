package io.shiftmanager.you.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

@Data
@Alias("User")
public class User {
    private Long userId;

    @NotBlank(message = "ユーザー名を入力してください")
    @Size(min = 2, max = 20, message = "ユーザー名は2文字以上20文字以下で入力してください")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "ユーザー名は英数字、ハイフン、アンダースコアのみ使用可能です")
    private String username;
    @JsonIgnore
    @NotBlank(message = "パスワードを入力してください")
    @Size(min = 8, max = 20, message = "パスワードは8文字以上20文字以下で入力してください")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "パスワードは少なくとも1つの英字、数字、特殊文字を含める必要があります"
    )
    private String password;

    @NotBlank(message = "メールアドレスを入力してください")
    @Email(message = "有効なメールアドレスを入力してください")
    @Size(max = 100, message = "メールアドレスは100文字以下で入力してください")
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isActive;
    private boolean isAdmin;

    // Spring Security用のメソッド
    public boolean isAdmin() {
        return this.isAdmin;
    }

    public boolean isActive() {
        return this.isActive;
    }
    
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}

