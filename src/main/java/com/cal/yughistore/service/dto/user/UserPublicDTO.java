package com.cal.yughistore.service.dto.user;

import com.cal.yughistore.model.user.ShoppingCart;
import com.cal.yughistore.model.user.ApplicationUser;
import com.cal.yughistore.model.user.auth.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserPublicDTO {

    private Long id;

    private String profilePictureUrl="";

    @NotBlank(message = "Username is mandatory")
    @Size(min = 4)
    private String userName;

    @NotBlank(message = "First name is mandatory")
    @Size(min = 4)
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Size(min = 2)
    private String lastName;


    @NotBlank(message = "Email is mandatory")
    @Email
    private String email;

    private ShoppingCart shoppingCart = new ShoppingCart();

    private Role role;

    @Builder
    public UserPublicDTO(Long id, String profilePictureUrl, String userName, String firstName, String lastName, ShoppingCart shoppingCart, String email, Role role) {
        this.id = id;
        this.profilePictureUrl = profilePictureUrl;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.shoppingCart = shoppingCart;
        this.email = email;
        this.role = role;
    }

    public static UserPublicDTO of(ApplicationUser user) {
        if(user == null){
            return new UserPublicDTO();
        }
        return UserPublicDTO.builder()
                .id(user.getId())
                .profilePictureUrl(user.getProfilePictureUrl())
                .userName(user.getUserName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .shoppingCart(user.getShoppingCart())
                .role(user.getRole())
                .build();
    }

    public ApplicationUserDTO toApplicationUserDTO(){
        return ApplicationUserDTO.builder()
                .id(this.getId())
                .profilePictureUrl(this.getProfilePictureUrl())
                .userName(this.getUserName())
                .firstName(this.getFirstName())
                .lastName(this.getLastName())
                .email(this.getEmail())
                .shoppingCart(this.getShoppingCart())
                .role(this.getRole())
                .build();
    }
}
