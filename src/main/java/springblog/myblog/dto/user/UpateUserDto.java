package springblog.myblog.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class UpateUserDto {

    private Long id;
    private String username;
    @NotEmpty(message = "Password cannot be empty")
    private String password;
    private String email;
}
