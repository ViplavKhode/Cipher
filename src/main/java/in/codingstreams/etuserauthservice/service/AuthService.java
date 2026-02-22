package in.codingstreams.etuserauthservice.service;

import in.codingstreams.etuserauthservice.dto.AuthRequest;
import in.codingstreams.etuserauthservice.dto.AuthResponse;
import in.codingstreams.etuserauthservice.entity.User;
import in.codingstreams.etuserauthservice.repository.UserRepository;
import in.codingstreams.etuserauthservice.util.JwtUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtUtils jwtUtils, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse signup(AuthRequest authRequest) {
        if (userRepository.existsByEmail(authRequest.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setFirstName(authRequest.getFirstName());
        user.setLastName(authRequest.getLastName());
        user.setEmail(authRequest.getEmail());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        user.setCreatedAt(System.currentTimeMillis());
        user.setUpdatedAt(System.currentTimeMillis());

        userRepository.save(user);

        String token = jwtUtils.generateToken(authRequest.getEmail());

        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(token);
        authResponse.setExpiresIn(jwtUtils.getExpirationTime());

        return authResponse;
    }

    public AuthResponse login(AuthRequest authRequest) {
        Optional<User> userOptional = userRepository.findByEmail(authRequest.getEmail());

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtils.generateToken(authRequest.getEmail());

        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(token);
        authResponse.setExpiresIn(jwtUtils.getExpirationTime());

        return authResponse;
    }

    public boolean verifyToken(String token) {
        return jwtUtils.validateToken(token);
    }

    public String getEmailFromToken(String token) {
        return jwtUtils.extractEmail(token);
    }
}
