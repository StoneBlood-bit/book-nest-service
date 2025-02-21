package mate.academy.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import mate.academy.annotation.FieldMatch;

@Data
@FieldMatch(
        first = "password",
        second = "repeatPassword",
        message = "Passwords do not match"
)
public class UserRegistrationRequestDto {
    @NotBlank
    @Email
    private String email;

    @Size(min = 8, max = 100)
    @NotBlank
    private String password;

    @NotBlank
    private String repeatPassword;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;
}
