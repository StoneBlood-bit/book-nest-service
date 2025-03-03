package mate.academy.dto.user;

import lombok.Data;

@Data
public class UserInfoResponseDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private int tokens;
}
