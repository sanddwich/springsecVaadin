package com.example.application.restControllers;

import com.example.application.entities.User;
import com.example.application.restControllers.inputDTO.AuthDataInput;
import com.example.application.security.jwt.JwtTokenProvider;
import com.example.application.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class RestAPIAuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public RestAPIAuthController(
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider,
            UserService userService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthDataInput authDataInput) {
        try {
            this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authDataInput.getUsername(), authDataInput.getPass())
            );

            User user = this.userService.findByUsername(authDataInput.getUsername()).stream().findFirst().get();

            if (user == null)
                throw new UsernameNotFoundException("User with username: " + authDataInput.getUsername() + " not found");

            String token = this.jwtTokenProvider.createToken(user);

            Map<Object, Object> response = new HashMap<>();
            response.put("username", authDataInput.getUsername());
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch(AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password");
        }
    }
}
