package bookmanagement.controller;

import bookmanagement.config.StripeProperties;
import bookmanagement.services.security.JwtService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class PaymentController {

    private final JwtService jwtService;
    private final StripeProperties stripeProperties;

    @Value("${application.security.allowed-origins}")
    private String appUrl;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeProperties.getApiKey();
    }

    @PostMapping("/create-checkout-session")
    public ResponseEntity<Map<String, String>> createCheckoutSession(@RequestBody Map<String, Object> data) throws StripeException {
        // Obtener el importe enviado desde el frontend
        Integer amount = (Integer) data.get("amount");

        // Validar importe mínimo, si quieres
        if (amount == null || amount <= 0) {
            return ResponseEntity.badRequest().body(Map.of("error", "Importe inválido"));
        }

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(appUrl.concat("/success?session_id={CHECKOUT_SESSION_ID}"))
                .setCancelUrl(appUrl.concat("/cancel"))
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("eur")
                                                .setUnitAmount(Long.valueOf(Math.round(amount * 100))) // importe en céntimos, viene del frontend
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Reserva de taquilla")
                                                                .build())
                                                .build())
                                .setQuantity(1L)
                                .build())
                .build();

        Session session = Session.create(params);
        Map<String, String> response = new HashMap<>();
        response.put("id", session.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/confirm-payment")
    public ResponseEntity<?> confirmPayment(@RequestBody Map<String, String> body) {
        String sessionId = body.get("sessionId");
        try {
            // Obtiene los detalles de la sesión de Stripe
            Session session = Session.retrieve(sessionId);

            if ("complete".equals(session.getStatus())) {
                // Aquí puedes marcar la reserva como pagada y continuar
                // Por ejemplo, registrar la reserva en la base de datos

                return ResponseEntity.ok(Map.of("success", "true"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El pago no fue completado");
            }

        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al verificar el pago: " + e.getMessage());
        }
    }
}

