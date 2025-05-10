package rca.rw.secure.security;

import lombok.RequiredArgsConstructor;
import rca.rw.secure.exceptions.BadRequestException;
import rca.rw.secure.repos.IUserRepo;
import rca.rw.secure.enums.user.EUserStatus;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rca.rw.secure.models.User;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final IUserRepo userRepo;

    @Transactional
    public UserDetails loadByUserId(Long id) {
        User user = this.userRepo.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserByUsername(String s) throws BadRequestException {
        User user = userRepo.findUserByEmailOrUsername(s, s).orElseThrow(() -> new UsernameNotFoundException("user not found with email or username of " + s));
        if (!user.getStatus().equals(EUserStatus.ACTIVE)) {
            throw new BadRequestException("User is not active");
        }
        return UserPrincipal.create(user);
    }
}