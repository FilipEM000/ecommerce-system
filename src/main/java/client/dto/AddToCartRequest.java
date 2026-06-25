package client.dto;

public record AddToCartRequest(Long clientId, Long productId, Integer quantity) {
}
