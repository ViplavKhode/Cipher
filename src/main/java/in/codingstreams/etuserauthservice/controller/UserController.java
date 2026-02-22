package in.codingstreams.etuserauthservice.controller;

import in.codingstreams.etuserauthservice.dto.ChangePasswordRequest;
import in.codingstreams.etuserauthservice.dto.UpdateUserRequest;
import in.codingstreams.etuserauthservice.dto.UserDto;
import in.codingstreams.etuserauthservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserInfo(@PathVariable String userId) {
        try {
            UserDto userDto = userService.getUserById(userId);
            return ResponseEntity.ok(userDto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/{userId}/change-password")
    public ResponseEntity<String> changePassword(@PathVariable String userId, 
                                                  @RequestBody ChangePasswordRequest changePasswordRequest) {
        try {
            userService.changePassword(userId, changePasswordRequest);
            return ResponseEntity.ok("Password changed successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{userId}/update")
    public ResponseEntity<UserDto> updateUserInfo(@PathVariable String userId, 
                                                   @RequestBody UpdateUserRequest updateUserRequest) {
        try {
            UserDto userDto = userService.updateUserInfo(userId, updateUserRequest);
            return ResponseEntity.ok(userDto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
