package com.backCommerce.controller;

import com.backCommerce.dto.PaymentResponseDto;
import com.backCommerce.service.PayPalAuthService;
import com.backCommerce.service.PayPalPaymentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paypal")

public class PayPalController {

    private final PayPalAuthService payPalAuthService;
    private final PayPalPaymentService payPalPaymentService;

    public PayPalController(PayPalAuthService payPalAuthService, PayPalPaymentService payPalPaymentService) {
        this.payPalAuthService = payPalAuthService;
        this.payPalPaymentService = payPalPaymentService;
    }

    @PostMapping("/payment/{userId}")
    public ResponseEntity<PaymentResponseDto> createPayment(@PathVariable Long userId) {
        PaymentResponseDto response = payPalPaymentService.createPayment(userId);
        return ResponseEntity.ok(response);
    }

    // Esta URL maneja el éxito del pago momentaneamente
    // En un entorno de producción, deberíamos usar una url de retorno asociada al frontend
    @GetMapping("/success")
    public String success(@RequestParam("paymentId") String paymentId,
    @RequestParam("PayerID") String payerId) {
        String accessToken = payPalAuthService.getAccessToken();
        String result = payPalPaymentService.confirmPayment(paymentId, payerId, accessToken);
        return result;
    }

    // Esta URL maneja el cancelamiento del pago
    @GetMapping("/cancel")
    public String cancel() {
        return "El pago fue cancelado.";
    }
}
