package client.service.impl;

import client.dto.AddToCartRequest;
import client.dto.CartData;
import client.entity.Cart;
import client.entity.Client;
import client.repository.ClientRepository;
import client.service.CartService;
import client.validator.CartValidator;
import exception.ClientNotFoundException;
import exception.InvalidProductTypeException;
import exception.ProductNotFoundException;
import lombok.AllArgsConstructor;
import product.dto.ComputerConfiguration;
import product.dto.SmartphoneConfiguration;
import product.entity.Product;
import product.entity.computer.Computer;
import product.entity.computer.ProcessorType;
import product.entity.computer.Ram;
import product.entity.smartphone.BatteryCapacity;
import product.entity.smartphone.Color;
import product.entity.smartphone.Smartphone;
import product.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.HashSet;

@AllArgsConstructor
public final class CartServiceImpl implements CartService {
    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;

    @Override
    public Cart getAllProductsInCart(Long clientId) {
        return findClientOrThrow(clientId).getCart();
    }

    @Override
    public void clearCart(Long clientId) {
        Cart cart = getAllProductsInCart(clientId);
        cart.getProducts().clear();
    }

    @Override
    public void addStandardProductToCart(AddToCartRequest request) {
        CartData data = validateAndGetCartData(request);

        Product clonedProduct = data.masterProduct().cloneProduct();
        data.client().getCart().addProduct(clonedProduct, request.quantity());
    }

    @Override
    public void addComputerToCart(AddToCartRequest request, ComputerConfiguration configuration) {
        CartData data = validateAndGetCartData(request);

        Computer masterComputer = castToComputer(data.masterProduct());

        Computer configuredComputer = new Computer(masterComputer);
        configuredComputer.configure(
                ProcessorType.valueOf(configuration.processor()),
                Ram.valueOf(configuration.ram()
                ));

        data.client().getCart().addProduct(configuredComputer, request.quantity());
    }

    @Override
    public void addSmartphoneToCart(AddToCartRequest request, SmartphoneConfiguration configuration) {
        CartData data = validateAndGetCartData(request);
        Smartphone masterSmartphone = castToSmartphone(data.masterProduct());

        Smartphone configuredSmartphone = new Smartphone(masterSmartphone);
        configuredSmartphone.configure(Color.valueOf(
                        configuration.color()),
                BatteryCapacity.valueOf(configuration.battery()),
                new HashSet<>());

        data.client().getCart().addProduct(configuredSmartphone, request.quantity());
    }

    @Override
    public BigDecimal getCartTotalPrice(Long clientId) {
        Cart cart = getAllProductsInCart(clientId);
        return cart.getProducts().entrySet().stream()
                .map(entry -> entry.getKey().getPrice().multiply(BigDecimal.valueOf(entry.getValue())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Client findClientOrThrow(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Nie znaleziono klienta o id " + clientId));
    }

    private Product findProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Nie znaleziono produktu o id " + productId));
    }

    private CartData validateAndGetCartData(AddToCartRequest request) {
        Client client = findClientOrThrow(request.clientId());
        Product masterProduct = findProduct(request.productId());
        CartValidator.validateQuantityToAdd(client.getCart(), masterProduct, request.quantity());

        return new CartData(client, masterProduct);
    }

    private Computer castToComputer(Product product) {
        if (product instanceof Computer computer) {
            return computer;
        }
        throw new InvalidProductTypeException("Produkt nie jest komputerem");
    }

    private Smartphone castToSmartphone(Product product) {
        if (product instanceof Smartphone smartphone) {
            return smartphone;
        }
        throw new InvalidProductTypeException("Ten produkt nie jest smartfonem!");
    }
}
