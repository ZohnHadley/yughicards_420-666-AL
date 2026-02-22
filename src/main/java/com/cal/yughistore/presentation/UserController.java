package com.cal.yughistore.presentation;

import com.cal.yughistore.utils.JwtTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.*;

import com.cal.yughistore.security.exception.InvalidJwtTokenException;
import com.cal.yughistore.security.exception.UserNotFoundException;
import com.cal.yughistore.services.ApplicationUserService;
import com.cal.yughistore.services.AuthService;
import com.cal.yughistore.services.DTOs.util.*;
import com.cal.yughistore.services.DTOs.applicationuser.ApplicationUserDTO;
import com.cal.yughistore.services.DTOs.applicationuser.LoginDTO;
import com.cal.yughistore.services.DTOs.applicationuser.PasswordRequestDTO;
import com.cal.yughistore.services.DTOs.applicationuser.UserSettingsDTO;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

	private final AuthService authService;
	private final ApplicationUserService applicationUserService;

	@PostMapping("/signin")
	public ResponseEntity<JWTAuthResponseDTO> signIn(@RequestBody LoginDTO LoginDTO) {
		try {
			String accessToken = authService.userLogin(LoginDTO);
			return ResponseEntity.ok(new JWTAuthResponseDTO(accessToken));
		}
		catch (AuthenticationCredentialsNotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new JWTAuthResponseDTO("Veuillez fournir email et mot de passe."));
		}
		catch (LockedException e) {
			return ResponseEntity.status(HttpStatus.LOCKED)
					.body(new JWTAuthResponseDTO(e.getMessage()));
		}
		catch (BadCredentialsException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new JWTAuthResponseDTO("Email ou mot de passe incorrect."));
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new JWTAuthResponseDTO("Erreur interne lors de l'authentification."));
		}
	}


	@PostMapping("/password-reset/request")
	public ResponseEntity<String> requestPasswordReset(@RequestParam String email){
		try {
			authService.userPasswordResetRequest(email);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("Email de réinitialisation envoyé.");
		}
		catch (UserNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur introuvable.");
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erreur lors de la demande de réinitialisation.");
		}
	}

	@PostMapping("/password-reset/confirm")
	public ResponseEntity<String> confirmPasswordReset(@RequestBody PasswordRequestDTO PasswordRequestDTO) {
		try {
			authService.userPasswordReset(PasswordRequestDTO);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Mot de passe réinitialisé.");
		}
		catch (InvalidJwtTokenException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalide ou expiré.");
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la réinitialisation.");
		}
	}

	@GetMapping("/me")
	public ResponseEntity<ApplicationUserDTO> getMe(HttpServletRequest request){
		return ResponseEntity.accepted().contentType(MediaType.APPLICATION_JSON).body(
			applicationUserService.getMe(request.getHeader("Authorization")));
	}

    @PutMapping("/settings")
    public ResponseEntity<ApiSuccessResponseDTO<UserSettingsDTO>> updateMySettings(
            HttpServletRequest request,
            @RequestBody UserSettingsDTO dto
    ) {
        Long id = applicationUserService.getMe(JwtTokenUtils.getTokenFromRequest(request)).getId();
        UserSettingsDTO updated = applicationUserService.updateMySettings(id, dto);

        return ResponseEntity.ok(
                ApiSuccessResponseDTO.of(
                        "USER_SETTINGS_UPDATED",
                        "Les paramètres utilisateur ont été mis à jour avec succès.",
                        updated
                )
        );
    }

    @GetMapping("/settings")
    public ResponseEntity<ApiSuccessResponseDTO<UserSettingsDTO>> getMySettings(HttpServletRequest request) {
        Long id = applicationUserService.getMe(JwtTokenUtils.getTokenFromRequest(request)).getId();
        UserSettingsDTO settings = applicationUserService.getMySettings(id);

        return ResponseEntity.ok(
				ApiSuccessResponseDTO.of(
                        "USER_SETTINGS_FETCHED",
                        "Paramètres utilisateur récupérés avec succès.",
                        settings
                )
        );
    }
}
