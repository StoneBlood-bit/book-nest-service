package mate.academy.service.user;

import mate.academy.dto.user.UserInfoResponseDto;
import mate.academy.dto.user.UserRegistrationRequestDto;
import mate.academy.dto.user.UserRegistrationResponseDto;

public interface UserService {
    UserRegistrationResponseDto register(UserRegistrationRequestDto requestDto);

    UserInfoResponseDto getById(Long id);
}
