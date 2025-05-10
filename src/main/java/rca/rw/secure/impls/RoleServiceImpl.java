package rca.rw.secure.impls;

import rca.rw.secure.enums.user.EUserRole;
import rca.rw.secure.exceptions.*;
import rca.rw.secure.dtos.response.ApiResponse;
import rca.rw.secure.dtos.role.CreateRoleDTO;
import rca.rw.secure.dtos.role.RoleResponseDTO;
import rca.rw.secure.dtos.role.RolesResponseDTO;
import rca.rw.secure.models.Role;
import rca.rw.secure.repos.IRoleRepo;
import rca.rw.secure.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final IRoleRepo roleRepo;

    @Override
    @Transactional
    public void createRole(EUserRole roleName) {
        Optional<Role> optionalRole = roleRepo.findRoleByName(roleName);
        if (optionalRole.isPresent()) {
            throw new BadRequestException("The role already exists");
        } else {
            Role role = new Role(roleName);
            try {
                roleRepo.save(role);
            } catch (Exception e) {
                throw new InternalServerErrorException(e.getMessage());
            }
        }
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse<RoleResponseDTO>> createRole(CreateRoleDTO createRoleDTO) {
        try {

            Optional<Role> optionalRole = roleRepo.findRoleByName(createRoleDTO.getName());
            if (optionalRole.isPresent()) {
                throw new ConflictException("The role already exists");
            } else {
                Role role = new Role(createRoleDTO.getName());
                roleRepo.save(role);
                return ApiResponse.success("Role created successfully", HttpStatus.CREATED, new RoleResponseDTO(role));
            }
        } catch (Exception exception) {
            System.err.println("Error creating role: " + exception.getMessage());
            throw new CustomException(exception);
        }
    }

    @Override
    public ResponseEntity<ApiResponse<RolesResponseDTO>> getRoles(Pageable pageable) {
        try {
            Page<Role> roles = roleRepo.findAll(pageable);
            return ApiResponse.success("Roles fetched successfully", HttpStatus.OK, new RolesResponseDTO(roles));
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    @Override
    public Role getRoleById(Long roleId) {
        return roleRepo.findById(roleId).orElseThrow(() -> new NotFoundException("The Role was not found"));
    }

    @Override
    public Role getRoleByName(EUserRole roleName) {
        return roleRepo.findRoleByName(roleName).orElseThrow(() -> new NotFoundException("The Role was not found"));
    }

    @Override
    public Role deleteRole(Long roleId) {
        Role role = roleRepo.findById(roleId).orElseThrow(() -> new NotFoundException("The role is not present"));
        try {
            roleRepo.deleteById(roleId);
            return role;
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Override
    public boolean isRolePresent(EUserRole roleName) {
        try {
            return roleRepo.findRoleByName(roleName).isPresent();
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
