package bookmanagement.config;

import bookmanagement.services.security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Excluir rutas públicas
        String uri = request.getRequestURI();
        if (uri.startsWith("/v1/token") ||
                uri.startsWith("/v1/checkBoxOfficeAvailable") ||
                uri.startsWith("/v1/create-checkout-session") ||
                uri.startsWith("/v1/confirm-payment")
                uri.startsWith("/v1/pickupLuggage")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (jwtService.validateToken(token)) {
                Boolean confirmPayment = jwtService.getConfirmPaymentFromToken(token);

                if (Boolean.TRUE.equals(confirmPayment)) {
                    String userId = jwtService.getUserIdFromToken(token);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    // Si el pago no fue confirmado, rechazar con 403
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("Pago no confirmado.");
                    return;
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token inválido.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

}

