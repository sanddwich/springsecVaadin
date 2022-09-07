package com.example.application.restControllers;

import com.example.application.entities.User;
import com.example.application.restControllers.inputDTO.AuthDataInput;
import com.example.application.restControllers.inputDTO.TokenDataInput;
import com.example.application.security.jwt.JwtTokenProvider;
import com.example.application.services.UserService;
import org.openxmlformats.schemas.drawingml.x2006.diagram.CTHierBranchStyle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(exposedHeaders = "Access-Control-Allow-Origin", origins = "http://localhost:3000")
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
        Map<Object, Object> response = new HashMap<>();

        try {
            this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authDataInput.getUsername(), authDataInput.getPass())
            );

            User user = this.userService.findByUsername(authDataInput.getUsername()).stream().findFirst().get();

            if (user == null)
                throw new UsernameNotFoundException("User with username: " + authDataInput.getUsername() + " not found");

            String token = this.jwtTokenProvider.createToken(user);

            response.put("username", authDataInput.getUsername());
            response.put("token", token);

            return ResponseEntity.ok(response);

        } catch(AuthenticationException e) {
            response.put("authError:", "Invalid username/password");
            return ResponseEntity.ok(response);
//            throw new BadCredentialsException("Invalid username/password");
        }
    }

    @PostMapping("/check_token")
    public ResponseEntity checkToken(@RequestBody TokenDataInput tokenDataInput) {
        Map<Object, Object> response = new HashMap<>();

//        System.out.println(tokenDataInput.getToken());

        try {
            response.put("tokenIsValid", this.jwtTokenProvider.validateToken(tokenDataInput.getToken()));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("tokenIsValid", false);
            return ResponseEntity.ok(response);
        }
    }

    private HttpHeaders getCORSHeader() {
        HttpHeaders corsHeader = new HttpHeaders();
        corsHeader.add("Access-Control-Allow-Origin", "*");

        return corsHeader;
    }

}
