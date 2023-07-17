package kr.co.realiv.data.dto;

import kr.co.realiv.data.User;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
public class UserUpdateAndDeleteDto {
    private String userId;
    private String updateUserId;
    private String updatePassword;
    private String updateEmail;
    private String checkPassword;

    public boolean isSameUser(User user) {
        return user.getUserId().equals(userId);
    }

    public void isEmpty(User user, PasswordEncoder passwordEncoder) {
        if (this.updateUserId.isEmpty()) {
            this.updateUserId = user.getUserId();
        }
        if(!this.updatePassword.isEmpty()){
            this.updatePassword = passwordEncoder.encode(this.updatePassword);
        }
        if (this.updatePassword.isEmpty()) {
            this.updatePassword = user.getPassword();
        }
        if (this.updateEmail.isEmpty()) {
            this.updateEmail = user.getEmail();
        }
    }
}
