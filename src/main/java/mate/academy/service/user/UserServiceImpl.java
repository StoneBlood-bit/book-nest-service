package mate.academy.service.user;

import lombok.RequiredArgsConstructor;
import mate.academy.dto.user.UserInfoResponseDto;
import mate.academy.dto.user.UserRegistrationRequestDto;
import mate.academy.dto.user.UserRegistrationResponseDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.exception.RegistrationException;
import mate.academy.mapper.UserMapper;
import mate.academy.model.User;
import mate.academy.repository.user.UserRepository;
import mate.academy.security.AuthenticationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getLogger(AuthenticationService.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserRegistrationResponseDto register(
            UserRegistrationRequestDto requestDto
    ) {
        logger.info("Method register was called, user: {}", requestDto.getEmail());
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("User with email "
                    + requestDto.getEmail() + " already exists.");
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(User.Role.CUSTOMER);

        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    @Override
    public UserInfoResponseDto getById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find user with id: " + id)
        );
        return userMapper.toInfoDto(user);
    }
}
