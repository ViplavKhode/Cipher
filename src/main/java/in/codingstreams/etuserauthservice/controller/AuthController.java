package in.codingstreams.etuserauthservice.controller;

import in.codingstreams.etuserauthservice.dto.AuthRequest;
import in.codingstreams.etuserauthservice.dto.AuthResponse;
import in.codingstreams.etuserauthservice.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody AuthRequest authRequest) {
        try {
            AuthResponse authResponse = authService.signup(authRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        try {
            AuthResponse authResponse = authService.login(authRequest);
            return ResponseEntity.ok(authResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<Boolean> verifyToken(@RequestParam String token) {
        boolean isValid = authService.verifyToken(token);
        return ResponseEntity.ok(isValid);
    }
}
