package com.backCommerce.service;

import com.backCommerce.config.PayPalConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Map;

@Service
public class PayPalAuthServiceImp implements PayPalAuthService {

    private final PayPalConfig payPalConfig;
    private final RestTemplate restTemplate;

    @Autowired
    public PayPalAuthServiceImp(PayPalConfig payPalConfig) {
        this.payPalConfig = payPalConfig;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public String getAccessToken() {
        String credentials = payPalConfig.getClientId() + ":" + payPalConfig.getClientSecret();
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Basic " + encodedCredentials);

        HttpEntity<String> request = new HttpEntity<>("grant_type=client_credentials", headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                payPalConfig.getBaseUrl() + "/v1/oauth2/token",
                HttpMethod.POST,
                request,
                Map.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> body = response.getBody();
            return (String) body.get("access_token");
        } else {
            throw new RuntimeException("No se pudo obtener el access_token de PayPal. Código: " + response.getStatusCode());
        }
    }
}