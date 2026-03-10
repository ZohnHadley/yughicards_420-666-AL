package com.cal.yughistore.security;

import com.cal.yughistore.model.user.ApplicationUser;
import com.cal.yughistore.repository.user.ApplicationUserRepository;
import com.cal.yughistore.security.exceptions.UserNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider tokenProvider;
    private final ApplicationUserRepository applicationUserRepository;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, ApplicationUserRepository applicationUserRepository) {
        this.tokenProvider = tokenProvider;
        this.applicationUserRepository = applicationUserRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String path = request.getServletPath();

        if (path.equals("/api/v1/user/signup") || path.equals("/api/v1/user/signin")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getJWTFromRequest(request);

        if (StringUtils.hasText(token)) {
            System.out.println("Request path: " + request.getServletPath());
            System.out.println("Authorization: " + request.getHeader("Authorization"));

            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            try {
                tokenProvider.validateToken(token);
                String email = tokenProvider.getEmailFromJWT(token);

                ApplicationUser user = applicationUserRepository.findApplicationUserByEmail(email)
                        .orElseThrow(() -> new UserNotFoundException("Étudiant introuvable avec email " + email));

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        user.getEmail(), null, user.getAuthorities()
                );

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            } catch (Exception e) {
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }

    private String getJWTFromRequest(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

}
