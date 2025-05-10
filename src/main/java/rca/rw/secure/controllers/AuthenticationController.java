package rca.rw.secure.controllers;

import rca.rw.secure.dtos.auth.AuthResponse;
import rca.rw.secure.dtos.auth.LoginDTO;
import rca.rw.secure.dtos.auth.RegisterUserDTO;
import rca.rw.secure.dtos.response.ApiResponse;
import rca.rw.secure.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH, RequestMethod.OPTIONS})
@RequestMapping(path = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginDTO signInDTO) {
        return authService.login(signInDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterUserDTO registerUserDTO) {
        return authService.register(registerUserDTO);
    }
}
