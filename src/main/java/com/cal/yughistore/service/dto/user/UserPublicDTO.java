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

    @Size(min = 4)
    private String firstName;

    @Size(min = 2)
    private String lastName;


    @NotBlank(message = "Email is mandatory")
    @Email
    private String email;

    private ShoppingCart shoppingCart = new ShoppingCart();

    private Role role;

    public static UserPublicDTO of(ApplicationUser user) {
        if(user == null){
           throw new IllegalArgumentException("User must not be null");
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

    public static ApplicationUserDTO toApplicationUserDTO(ApplicationUser user) {
        if(user == null){
            throw new IllegalArgumentException("User must not be null");
        }
        return ApplicationUserDTO.of(user);
    }

    public static UserPublicDTO toUserPublicDTO(ApplicationUserDTO user) {
        if(user == null){
            throw new IllegalArgumentException("User must not be null");
        }
        return UserPublicDTO.builder()
                .id(user.getId())
                .profilePictureUrl(user.getProfilePictureUrl())
                .userName(user.getUserName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
//                .shoppingCart(user.getShoppingCartId())
                .role(user.getRole())
                .build();
    }

}
