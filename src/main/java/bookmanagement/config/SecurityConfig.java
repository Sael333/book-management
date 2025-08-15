package bookmanagement.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.List;

@EnableWebSecurity
@EnableWebMvc
@Configuration
public class SecurityConfig implements WebMvcConfigurer{
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Configura las reglas de seguridad
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(new AntPathRequestMatcher("/v1/create-checkout-session")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/v1/token")).permitAll()// Permitir acceso sin autenticación a /v1/process-payment
                        .requestMatchers(new AntPathRequestMatcher("/v1/checkBoxOfficeAvailable")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/v1/confirm-payment")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/v1/pickupLuggage")).permitAll()
                        .anyRequest().authenticated()  // Requiere autenticación para otros endpoints
                )
                .cors(Customizer.withDefaults())  // Habilitar CORS
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/v1/**"))  // Deshabilitar CSRF solo para las rutas de /v1/**
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);  // Añadir filtro de autenticación JWT antes de la autenticación estándar
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("https://rooms-locker-dev.onrender.com"));  // Configura los orígenes permitidos
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));  // Métodos permitidos
        corsConfiguration.setAllowedHeaders(List.of("*"));  // Permite todas las cabeceras
        corsConfiguration.setAllowCredentials(true);  // Permite el uso de credenciales

        // Configura CORS para todas las rutas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}
