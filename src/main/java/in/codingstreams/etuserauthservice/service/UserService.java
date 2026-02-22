package in.codingstreams.etuserauthservice.service;

import in.codingstreams.etuserauthservice.dto.ChangePasswordRequest;
import in.codingstreams.etuserauthservice.dto.UpdateUserRequest;
import in.codingstreams.etuserauthservice.dto.UserDto;
import in.codingstreams.etuserauthservice.entity.User;
import in.codingstreams.etuserauthservice.enums.UpdateRequestType;
import in.codingstreams.etuserauthservice.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto getUserById(String userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOptional.get();
        return mapUserToDto(user);
    }

    public void changePassword(String userId, ChangePasswordRequest changePasswordRequest) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        user.setUpdatedAt(System.currentTimeMillis());
        userRepository.save(user);
    }

    public UserDto updateUserInfo(String userId, UpdateUserRequest updateUserRequest) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOptional.get();

        if (updateUserRequest.getType() == UpdateRequestType.NAME) {
            user.setFirstName(updateUserRequest.getFirstName());
            user.setLastName(updateUserRequest.getLastName());
        } else if (updateUserRequest.getType() == UpdateRequestType.EMAIL) {
            if (userRepository.existsByEmail(updateUserRequest.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
            user.setEmail(updateUserRequest.getEmail());
        }

        user.setUpdatedAt(System.currentTimeMillis());
        userRepository.save(user);

        return mapUserToDto(user);
    }

    private UserDto mapUserToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setCreatedAt(user.getCreatedAt());
        userDto.setUpdatedAt(user.getUpdatedAt());
        return userDto;
    }
}
