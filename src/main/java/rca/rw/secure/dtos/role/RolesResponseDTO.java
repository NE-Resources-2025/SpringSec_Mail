package rca.rw.secure.dtos.role;

import rca.rw.secure.models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
public class RolesResponseDTO {
    private Page<Role> roles;
}
