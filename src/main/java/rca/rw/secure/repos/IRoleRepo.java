package rca.rw.secure.repos;

import rca.rw.secure.enums.user.EUserRole;
import rca.rw.secure.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepo extends JpaRepository<Role, Long> {
    Optional<Role> findRoleByName(EUserRole name);
}

