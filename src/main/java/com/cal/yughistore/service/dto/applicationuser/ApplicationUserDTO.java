package com.cal.yughistore.service.dto.applicationuser;

import com.cal.yughistore.model.ShoppingCart;
import com.cal.yughistore.model.applicaitonuser.AdminUser;
import com.cal.yughistore.model.applicaitonuser.ApplicationUser;
import com.cal.yughistore.model.applicaitonuser.ClientUser;
import com.cal.yughistore.model.applicaitonuser.auth.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ApplicationUserDTO {
    private Long id;

    private String profilePictureUrl="";

    @NotBlank(message = "Username is mandatory")
    @Size(min = 4)
    private String userName;


    @Size(min = 4)
    private String firstName;


    @Size(min = 2)
    private String lastName;


    @NotBlank(message = "Email is mandatory")
    @Email private String email;

    @JsonIgnore
    private ShoppingCart shoppingCart = new ShoppingCart();

    @NotBlank(message = "Password is mandatory")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,50}$",
            message = "Password must be 8-50 characters long, contain at least one uppercase, " +
                    "one lowercase, one number, and one special character"
    )
    private String password;
    private Role role;

    @Builder
    public ApplicationUserDTO(Long id, String profilePictureUrl, String userName, String firstName, String lastName, ShoppingCart shoppingCart, String email, String password, Role role) {
        this.id = id;
        this.profilePictureUrl = profilePictureUrl;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.shoppingCart = shoppingCart;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public ApplicationUserDTO(ApplicationUser user) {
        this.id = user.getId();
        this.profilePictureUrl = user.getProfilePictureUrl();
        this.userName = user.getUserName();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.role = user.getRole();
    }

    public static ApplicationUserDTO of(ApplicationUser user) {
        return new ApplicationUserDTO(user);
    }

    public AdminUser toAdminUser(){
        return AdminUser.builder()
                .id(this.getId())
                .profilePictureUrl(this.getProfilePictureUrl())
                .email(this.getEmail())
                .password(this.getPassword())
                .build();
    }

    public ClientUser toClientUser(){
        return ClientUser.builder()
                .id(this.getId())
                .profilePictureUrl(this.getProfilePictureUrl())
                .firstName(this.getFirstName())
                .lastName(this.getLastName())
                .username(this.getUserName())
                .email(this.getEmail())
                .password(this.getPassword())
                .shoppingCart(this.getShoppingCart())
                .build();
    }


}
