package rca.rw.secure.impls;

import rca.rw.secure.dtos.auth.AuthResponse;
import rca.rw.secure.exceptions.CustomException;
import rca.rw.secure.dtos.auth.LoginDTO;
import rca.rw.secure.dtos.auth.RegisterUserDTO;
import rca.rw.secure.dtos.response.ApiResponse;
import rca.rw.secure.dtos.user.CreateUserDTO;
import rca.rw.secure.models.User;
import rca.rw.secure.repos.IUserRepo;
import rca.rw.secure.security.JwtTokenProvider;
import rca.rw.secure.security.UserPrincipal;
import rca.rw.secure.services.AuthService;
import rca.rw.secure.services.AuthService;
import rca.rw.secure.services.UserService;
import rca.rw.secure.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final IUserRepo userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationProvider authenticationProvider;

    @Override
    public ResponseEntity<ApiResponse<AuthResponse>> register(RegisterUserDTO registerUserDTO) {
        try {
            CreateUserDTO createUserDTO = new CreateUserDTO(registerUserDTO);
            User user = userService.createUserEntity(createUserDTO);
            userRepository.save(user);
            Authentication authentication = authenticateUser(new LoginDTO(createUserDTO.getEmail(), createUserDTO.getPassword()));
            AuthResponse response = generateJwtAuthenticationResponse(authentication);
            return ApiResponse.success("Successfully registered user", HttpStatus.OK, response);
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    @Override
    public ResponseEntity<ApiResponse<AuthResponse>> login(LoginDTO loginDTO) {
        try {
            Authentication authentication = authenticateUser(loginDTO);
            AuthResponse response = generateJwtAuthenticationResponse(authentication);
            return ApiResponse.success("Successfully logged in", HttpStatus.OK, response);
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    private Authentication authenticateUser(LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());
        Authentication authentication = authenticationProvider.authenticate(authRequest);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    private AuthResponse generateJwtAuthenticationResponse(Authentication authentication) {
        String jwt = jwtTokenProvider.generateAccessToken(authentication);
        UserPrincipal userPrincipal = UserUtils.getLoggedInUser();
        assert userPrincipal != null;
        User user = userService.findUserById(userPrincipal.getId());
        user.setFullName(user.getFirstName() + " " + user.getLastName());
        return new AuthResponse(jwt, user);
    }
}

