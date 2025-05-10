package rca.rw.secure.services;

import rca.rw.secure.dtos.auth.AuthResponse;
import rca.rw.secure.dtos.auth.LoginDTO;
import rca.rw.secure.dtos.auth.RegisterUserDTO;
import rca.rw.secure.dtos.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    public ResponseEntity<ApiResponse<AuthResponse>> login(LoginDTO signInDTO);

    public ResponseEntity<ApiResponse<AuthResponse>> register(RegisterUserDTO registerUserDTO);
}

