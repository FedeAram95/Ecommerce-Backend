package com.backCommerce.service;

import com.backCommerce.dto.PaymentResponseDto;

public interface PayPalPaymentService {
    PaymentResponseDto createPayment(Long userId);
    String confirmPayment(String paymentId, String payerId, String accessToken);
}