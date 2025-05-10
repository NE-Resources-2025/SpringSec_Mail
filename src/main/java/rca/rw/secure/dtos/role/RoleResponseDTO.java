package rca.rw.secure.dtos.role;

import rca.rw.secure.models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoleResponseDTO {
    private Role role;
}
