package com.backCommerce.dto;

public class PaymentResponseDto {
    private Data data;
    private String redirectUrl;

    public PaymentResponseDto(Long cartId, Long userId, Double total, String redirectUrl) {
        this.data = new Data(cartId, userId, total);
        this.redirectUrl = redirectUrl;
    }

    public Data getData() { return data; }
    public String getRedirectUrl() { return redirectUrl; }

    public static class Data {
        private Long cartId;
        private Long userId;
        private Double total;

        public Data(Long cartId, Long userId, Double total) {
            this.cartId = cartId;
            this.userId = userId;
            this.total = total;
        }

        public Long getCartId() { return cartId; }
        public Long getUserId() { return userId; }
        public Double getTotal() { return total; }
    }
}
