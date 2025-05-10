package rca.rw.secure.dtos.user;

import rca.rw.secure.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDTO {
    private User user;
}
