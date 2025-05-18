package rca.rw.secure.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import rca.rw.secure.dtos.resource.CreateResourceDTO;
import rca.rw.secure.dtos.resource.ResourceResponseDTO;
import rca.rw.secure.dtos.resource.ResourcesResponseDTO;
import rca.rw.secure.dtos.resource.UpdateResourceDTO;
import rca.rw.secure.dtos.response.ApiResponse;
import rca.rw.secure.models.Resource;
import rca.rw.secure.services.ResourceService;
import rca.rw.secure.utils.Constants;

import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE})
@RequestMapping("/api/v1/resources")
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class ResourceController {

    private final ResourceService resourceService;

    @PostMapping("/create-resource")
    public ResponseEntity<ApiResponse<ResourceResponseDTO>> createResource(
            @Valid @RequestBody CreateResourceDTO createResourceDTO,
            Authentication authentication) {
        String username = authentication.getName();
        Resource resource = resourceService.createResource(createResourceDTO, username);
        ResourceResponseDTO responseDTO = mapToResponseDTO(resource);
        return ApiResponse.success("Resource created successfully", HttpStatus.CREATED, responseDTO);
    }

    @PatchMapping("/update-resource/{id}")
    public ResponseEntity<ApiResponse<ResourceResponseDTO>> updateResource(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateResourceDTO updateResourceDTO,
            Authentication authentication) {
        String username = authentication.getName();
        Resource resource = resourceService.updateResource(id, updateResourceDTO, username);
        ResourceResponseDTO responseDTO = mapToResponseDTO(resource);
        return ApiResponse.success("Resource updated successfully", HttpStatus.OK, responseDTO);
    }

    @DeleteMapping("/delete-resource/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteResource(
            @PathVariable UUID id,
            Authentication authentication) {
        String username = authentication.getName();
        resourceService.deleteResource(id, username);
        return ApiResponse.success("Resource deleted successfully", HttpStatus.OK, null);
    }

    @GetMapping("/get-resource/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<ResourceResponseDTO>> getResourceById(@PathVariable UUID id) {
        Resource resource = resourceService.getResourceById(id);
        ResourceResponseDTO responseDTO = mapToResponseDTO(resource);
        return ApiResponse.success("Resource retrieved successfully", HttpStatus.OK, responseDTO);
    }

    @GetMapping("/get-resources")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<ResourcesResponseDTO>> getAllResources(
            @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = Constants.DEFAULT_PAGE_SIZE) int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Resource> resourcePage = resourceService.getAllResources(pageable);
        Page<ResourceResponseDTO> responsePage = resourcePage.map(this::mapToResponseDTO);
        ResourcesResponseDTO responseDTO = new ResourcesResponseDTO(responsePage);
        return ApiResponse.success("Resources retrieved successfully", HttpStatus.OK, responseDTO);
    }

    @GetMapping("/search-resources")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<ResourcesResponseDTO>> searchResources(
            @RequestParam String searchKey,
            @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = Constants.DEFAULT_PAGE_SIZE) int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Resource> resourcePage = resourceService.searchResources(searchKey, pageable);
        Page<ResourceResponseDTO> responsePage = resourcePage.map(this::mapToResponseDTO);
        ResourcesResponseDTO responseDTO = new ResourcesResponseDTO(responsePage);
        return ApiResponse.success("Resources searched successfully", HttpStatus.OK, responseDTO);
    }

    private ResourceResponseDTO mapToResponseDTO(Resource resource) {
        ResourceResponseDTO dto = new ResourceResponseDTO();
        dto.setId(resource.getId());
        dto.setName(resource.getName());
        dto.setDescription(resource.getDescription());
        dto.setCreatedAt(resource.getCreatedAt());
        dto.setUpdatedAt(resource.getUpdatedAt());
        return dto;
    }
}