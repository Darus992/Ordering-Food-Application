package pl.dariuszgilewicz.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class UserFormDTO {

    @Schema(description = "Username of the user", example = "john_doe")
    @NotEmpty(message = "Username is required.")
    @Size(min = 5, message = "Username must contain at least 5 characters.")
    private String username;

    @Schema(description = "Email of the user", example = "john.doe@example.com")
    @NotEmpty(message = "User email is required.")
    private String email;

    @Schema(description = "Password of the user")
    @NotEmpty(message = "Password is required.")
    @Size(message = "Password must contain at least 5 characters.", min = 5)
    private String password;
}
