package bookmanagement.controller;

import bookmanagement.services.security.JwtService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
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

    static {
        Stripe.apiKey = "sk_test_51ReswyIsUbiUMsbMzs7Hk754Nnc7YQA0o86uEF5iwz3TkKS63yCISRzKC9GUMkQqkcL5MkZRll7F08ioSQncuTEC0011aDShhu"; // Tu clave secreta de Stripe
    }

    @PostMapping("/create-checkout-session")
    public ResponseEntity<Map<String, String>> createCheckoutSession() throws StripeException {
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:4200/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("https://localhost:4200/cancel")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("eur")
                                                .setUnitAmount(500L) // Precio en céntimos: 5.00€
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

