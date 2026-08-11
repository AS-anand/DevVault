package com.devvault.devvault.auth;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class AuthServiceTest {
    @Test
    void register_shouldCreateUserWithUserRole() {
        var command = new RegisterUserCommand(
                "Alice",
                "alice@example.com",
                "9876543210",
                LocalDate.of(1995, 5, 10),
                "password123"
        );

        var authService = new AuthService();

        User user = authService.register(command);

        assertEquals(Role.USER, user.getRole());
    }
}
