package rca.rw.secure;

import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import rca.rw.secure.dtos.user.CreateUserDTO;
import rca.rw.secure.enums.user.EUserRole;
import rca.rw.secure.models.Role;
import rca.rw.secure.models.User;
import rca.rw.secure.repos.IUserRepo;
import rca.rw.secure.services.RoleService;
import rca.rw.secure.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class DbInitialiser {

    private final RoleService roleService;
    private final UserService userService;
    private final IUserRepo userRepo;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.firstName}")
    private String adminFirstName;

    @Value("${admin.lastName}")
    private String adminLastName;

    public DbInitialiser(RoleService roleService, UserService userService, IUserRepo userRepo) {
        this.roleService = roleService;
        this.userService = userService;
        this.userRepo = userRepo;
    }

    @PostConstruct
    public void seedData() {
        Set<EUserRole> userRoleSet = new HashSet<>();
        userRoleSet.add(EUserRole.ADMIN);
        userRoleSet.add(EUserRole.USER);
        for (EUserRole role : userRoleSet) {
            if (!this.roleService.isRolePresent(role)) {
                this.roleService.createRole(role);
            }
        }

        Optional<User> isUserPresent = userRepo.findUserByEmail(adminEmail);
        if (isUserPresent.isEmpty()) {
            CreateUserDTO createUserDTO = new CreateUserDTO(
                    adminEmail,
                    adminUsername,
                    adminPassword,
                    adminFirstName,
                    adminLastName
            );
           User userEntity = this.userService.createUserEntity(createUserDTO);
            Role role = this.roleService.getRoleByName(EUserRole.ADMIN);
            userEntity.getRoles().add(role);
            userRepo.save(userEntity);
        }
    }
}
