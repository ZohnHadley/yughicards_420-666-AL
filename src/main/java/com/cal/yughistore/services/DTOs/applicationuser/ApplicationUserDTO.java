package com.cal.yughistore.services.DTOs.applicationuser;

import com.cal.yughistore.model.applicaitonuser.ApplicationUser;
import com.cal.yughistore.model.applicaitonuser.auth.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ApplicationUserDTO {
    private Long id;
    @NotBlank(message = "First name is mandatory")
    @Size(min = 4)
    private String firstName;
    @NotBlank(message = "Last name is mandatory")
    @Size(min = 2)
    @NotBlank(message = "Email is mandatory")
    private String lastName;
    @Email private String email;
    @NotBlank(message = "Password is mandatory")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,50}$",
            message = "Password must be 8-50 characters long, contain at least one uppercase, " +
                    "one lowercase, one number, and one special character"
    )
    private String password;
    private Role role;

    public ApplicationUserDTO(String firstName, String lastName, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public ApplicationUserDTO(ApplicationUser user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.role = user.getRole();
    }

    public static ApplicationUserDTO from(ApplicationUser user) {
        return new ApplicationUserDTO(user);
    }
}
