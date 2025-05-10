package rca.rw.secure.services;

import rca.rw.secure.dtos.response.ApiResponse;
import rca.rw.secure.dtos.role.CreateRoleDTO;
import rca.rw.secure.dtos.role.RoleResponseDTO;
import rca.rw.secure.dtos.role.RolesResponseDTO;
import rca.rw.secure.enums.user.EUserRole;
import rca.rw.secure.models.Role;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface RoleService {
    public Role getRoleById(Long roleId);

    public Role getRoleByName(EUserRole roleName);

    public void createRole(EUserRole roleName);

    public ResponseEntity<ApiResponse<RoleResponseDTO>> createRole(CreateRoleDTO createRoleDTO);

    public ResponseEntity<ApiResponse<RolesResponseDTO>> getRoles(Pageable pageable);

    public Role deleteRole(Long roleId);

    public boolean isRolePresent(EUserRole roleName);
}
