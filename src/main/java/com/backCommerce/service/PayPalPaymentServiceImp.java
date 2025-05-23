package com.backCommerce.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.backCommerce.dto.PaymentResponseDto;
import com.backCommerce.exception.EmptyCartException;
import com.backCommerce.exception.ShoppingCartNotFoundException;
import com.backCommerce.exception.UserNotFoundException;
import com.backCommerce.model.Payment;
import com.backCommerce.model.ShoppingCart;
import com.backCommerce.model.User;
import com.backCommerce.model.enums.PaymentStatus;
import com.backCommerce.repository.PaymentRepository;
import com.backCommerce.repository.ShoppingCartRepository;
import com.backCommerce.repository.UserRepository;

@Service
public class PayPalPaymentServiceImp implements PayPalPaymentService {

    @Value("${paypal.base-url}")
    private String baseUrl;

    @Value("${paypal.return-url}")
    private String returnUrl;

    @Value("${paypal.cancel-url}")
    private String cancelUrl;

    private final RestTemplate restTemplate;
    private final ShoppingCartRepository shoppingCartRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final PayPalAuthService payPalAuthService;

    public PayPalPaymentServiceImp(
        RestTemplate restTemplate,
        ShoppingCartRepository shoppingCartRepository,
        PaymentRepository paymentRepository,
        UserRepository userRepository,
        PayPalAuthService payPalAuthService
    ) {
        this.restTemplate = restTemplate;
        this.shoppingCartRepository = shoppingCartRepository;
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.payPalAuthService = payPalAuthService;
    }

    @Override
    public PaymentResponseDto createPayment(Long userId) {        
        ShoppingCart cart = shoppingCartRepository.findByUserIdAndPurchasedAtIsNull(userId).orElseThrow(() ->
            new ShoppingCartNotFoundException("El usuario no tiene un carrito activo")
        );

        Double total = shoppingCartRepository.calculateTotalForActiveCart(userId).orElseThrow(() ->
            new EmptyCartException("No se encontraron productos en el carrito activo")
        );

        if (total <= 0) {
            throw new EmptyCartException("El total de productos del carrito debe ser mayor que 0");
        }

        Payment payment = savePayment(cart, total);
        Map<String, Object> paymentRequest = buildPaymentRequest(cart.getId(), payment.getId(), total);

        try {
            ResponseEntity<Map> response;
            String accessToken = payPalAuthService.getAccessToken();
            response = sendPaymentRequest(accessToken, paymentRequest);

            if (response.getStatusCode() != HttpStatus.CREATED) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al crear el pago con PayPal");
            }

            Map<String, Object> responseBody = response.getBody();
            if (responseBody == null || !responseBody.containsKey("id")) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Respuesta inválida desde PayPal");
            }

            String paypalPaymentId = responseBody.get("id").toString();

            String redirectUrl = extractApprovalUrl(responseBody).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se recibió URL de aprobación de PayPal")
            );

            payment.setPaypalPaymentId(paypalPaymentId);
            paymentRepository.save(payment);
            return new PaymentResponseDto(cart.getId(), userId, total, redirectUrl);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Fallo al comunicarse con PayPal");
        }
    }

    @Override
    public String confirmPayment(String paymentId, String payerId, String accessToken) {
        String url = baseUrl + "/v1/payments/payment/" + paymentId + "/execute";

        Map<String, Object> requestBody = Map.of("payer_id", payerId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response;
        try {
            response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al confirmar el pago.");
        }

        if (response.getStatusCode() == HttpStatus.OK) {
            return "Pago confirmado con éxito.";
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Fallo al confirmar el pago. Código: " + response.getStatusCode());
        }
    }

    private Map<String, Object> buildPaymentRequest(Long cartId, Long paymentId, Double total) {
        Map<String, Object> amount = Map.of("total", total, "currency", "USD");
        Map<String, Object> transaction = Map.of(
            "amount", amount,
            "custom", "externalReference:" + cartId + "-" + paymentId
        );
        Map<String, Object> payer = Map.of("payment_method", "paypal");
        Map<String, Object> redirectUrls = Map.of(
            "return_url", returnUrl,
            "cancel_url", cancelUrl
        );

        return Map.of(
            "intent", "sale",
            "transactions", new Map[]{ transaction },
            "payer", payer,
            "redirect_urls", redirectUrls
        );
    }

    private ResponseEntity<Map> sendPaymentRequest(String accessToken, Map<String, Object> request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);
        return restTemplate.exchange(baseUrl + "/v1/payments/payment", HttpMethod.POST, entity, Map.class);
    }

    private Optional<String> extractApprovalUrl(Map<String, Object> responseBody) {
        List<Map<String, Object>> links = (List<Map<String, Object>>) responseBody.get("links");
        return links.stream()
            .filter(link -> "approval_url".equals(link.get("rel")))
            .map(link -> (String) link.get("href"))
            .findFirst();
    }

    private Payment savePayment(ShoppingCart cart, Double total) {
        Payment existingPaymentOpt = paymentRepository.findByShoppingCartAndStatus(cart, PaymentStatus.PENDING);

        if (existingPaymentOpt != null) {
            Payment existingPayment = existingPaymentOpt;
            existingPayment.setUpdatedAt(LocalDateTime.now());
            paymentRepository.save(existingPayment);
        } else {
        	existingPaymentOpt = new Payment();
        	existingPaymentOpt.setStatus(PaymentStatus.PENDING);
        	existingPaymentOpt.setAmount(total);
        	existingPaymentOpt.setShoppingCart(cart);
        	existingPaymentOpt.setCreatedAt(LocalDateTime.now());
            paymentRepository.save(existingPaymentOpt);
        }
        return existingPaymentOpt;
    }
}
