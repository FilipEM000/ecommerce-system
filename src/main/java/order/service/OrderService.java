package order.service;

import exception.EmptyCartException;
import exception.InvalidPromoCodeException;
import exception.NotEnoughQuantityInMagazineException;
import order.dto.OrderDto;

public interface OrderService {
    /**
     * Places an order for the specified client based on the current contents of their cart.
     * Applies any given promotional code, reduces product stock, and generates an invoice.
     *
     * @param clientId  The unique ID of the client placing the order.
     * @param promoCode An optional promotional code to apply a discount. Can be null or empty.
     * @return {@link OrderDto} containing the final order details, final cost, and invoice number.
     * @throws EmptyCartException                   if the client's cart has no products.
     * @throws NotEnoughQuantityInMagazineException if any product in the cart exceeds available stock.
     * @throws InvalidPromoCodeException            if the provided promo code does not exist.
     */
    OrderDto placeOrder(Long clientId, String promoCode);
}
