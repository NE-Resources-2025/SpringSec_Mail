package rca.rw.secure.dtos.user;

import rca.rw.secure.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
public class UsersResponseDTO {
    Page<User> users;
}
