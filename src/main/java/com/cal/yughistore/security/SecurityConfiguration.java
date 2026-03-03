package com.cal.yughistore.security;

import com.cal.yughistore.model.applicaitonuser.auth.Role;
import com.cal.yughistore.repository.user.ApplicationUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtTokenProvider jwtTokenProvider;
    private final ApplicationUserRepository applicationUserRepository;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;

    private static final String YUGHIO_CARD_DATA_PATH = "/api/v1/**";
    private static final String YUGHIO_CARD_SINGLE_PATH = "/api/v1/get-card/**";

    private static final String USER_PATH = "/api/v1/user/**";
    private static final String USER_PASSWORD_RESET_PATH = "/api/v1/user/password-reset/**";
    private static final String USER_SHOPPING_CART_PATH = "/api/v1/cart/**";
    private static final String ADMIN_PATH = "/api/v1/admin/**";
    private static final String CLIENT_PATH = "/api/v1/client/**";

    // Swagger/OpenAPI paths
    private static final String SWAGGER_UI_PATH = "/swagger-ui/**";
    private static final String SWAGGER_UI_HTML_PATH = "/swagger-ui.html";
    private static final String API_DOCS_PATH = "/v3/api-docs/**";
    private static final String SWAGGER_RESOURCES_PATH = "/swagger-resources/**";
    private static final String SWAGGER_CONFIG_PATH = "/swagger-ui/index.html";
    private static final String WEBJARS_PATH = "/webjars/**";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                                // Swagger/OpenAPI - Public
                                .requestMatchers(
                                        SWAGGER_UI_PATH,
                                        SWAGGER_UI_HTML_PATH,
                                        API_DOCS_PATH,
                                        SWAGGER_RESOURCES_PATH,
                                        SWAGGER_CONFIG_PATH,
                                        WEBJARS_PATH
                                ).permitAll()

                                // User endpoints
                                .requestMatchers(USER_PATH).permitAll()
                                .requestMatchers(HttpMethod.POST, USER_PASSWORD_RESET_PATH).hasAnyAuthority(Role.CLIENT.name())
                                .requestMatchers(HttpMethod.GET, USER_SHOPPING_CART_PATH).permitAll()
                                // Yu-Gi-Oh cards endpoints
//                        .requestMatchers(YUGHIO_CARD_DATA_PATH).permitAll()
                                .requestMatchers(HttpMethod.GET, YUGHIO_CARD_DATA_PATH).permitAll()
                                .requestMatchers(YUGHIO_CARD_SINGLE_PATH).permitAll()

                                // Tout le reste nécessite auth
                                .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions(f -> f.disable())) // for h2-console
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(configurer -> configurer.authenticationEntryPoint(authenticationEntryPoint));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // ⚡ IMPORTANT : port exact de votre frontend
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));

        // Autoriser tous les principaux methods
        configuration.setAllowedMethods(Arrays.asList(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.OPTIONS.name()
        ));

        // Autoriser headers utilisés par le frontend
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Cache-Control",
                "Content-Type",
                "Accept",
                "X-Requested-With"
        ));

        // Permet JWT / credentials
        configuration.setAllowCredentials(true);

        // Expose headers si nécessaire
        // configuration.setExposedHeaders(List.of("Custom-Header"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Appliquer à tous les endpoints
        return source;
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        return new JwtAuthenticationFilter(jwtTokenProvider, applicationUserRepository);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}