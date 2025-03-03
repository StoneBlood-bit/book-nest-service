package mate.academy.mapper;

import mate.academy.config.MapperConfig;
import mate.academy.dto.user.UserInfoResponseDto;
import mate.academy.dto.user.UserRegistrationRequestDto;
import mate.academy.dto.user.UserRegistrationResponseDto;
import mate.academy.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserRegistrationResponseDto toDto(User user);

    User toModel(UserRegistrationRequestDto requestDto);

    UserInfoResponseDto toInfoDto(User user);
}
