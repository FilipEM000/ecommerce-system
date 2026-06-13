package client.service.impl;

import client.dto.AddToCartRequest;
import client.entity.Cart;
import client.entity.Client;
import client.repository.ClientRepository;
import client.service.CartService;
import client.validator.CartValidator;
import exception.ClientNotFoundException;
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

import java.util.HashSet;

@AllArgsConstructor
public class CartServiceImpl implements CartService {
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
        Client client = findClientOrThrow(request.clientId());
        Product masterProduct = findProductOrThrow(request.productId());

        CartValidator.validateQuantityToAdd(client.getCart(), masterProduct, request.quantity());

        Product clonedProduct = masterProduct.cloneProduct();
        client.getCart().getProducts().merge(clonedProduct, request.quantity(), Integer::sum);
    }

    @Override
    public void addComputerToCart(AddToCartRequest request, ComputerConfiguration configuration) {
        Client client = findClientOrThrow(request.clientId());
        Product masterProduct = findProductOrThrow(request.productId());

        CartValidator.validateQuantityToAdd(client.getCart(), masterProduct, request.quantity());
        Computer masterComputer = CartValidator.validateAndCastToComputer(masterProduct);

        Computer configuredComputer = new Computer(masterComputer);
        configuredComputer.configure(
                ProcessorType.valueOf(configuration.processor()),
                Ram.valueOf(configuration.ram()
                ));

        client.getCart().getProducts().merge(configuredComputer, request.quantity(), Integer::sum);

    }

    @Override
    public void addSmartphoneToCart(AddToCartRequest request, SmartphoneConfiguration configuration) {
        Client client = findClientOrThrow(request.clientId());
        Product masterProduct = findProductOrThrow(request.productId());

        CartValidator.validateQuantityToAdd(client.getCart(), masterProduct, request.quantity());
        Smartphone masterSmartphone = CartValidator.validateAndCastToSmartphone(masterProduct);

        Smartphone configuredSmartphone = new Smartphone(masterSmartphone);
        configuredSmartphone.configure(Color.valueOf(
                        configuration.color()),
                BatteryCapacity.valueOf(configuration.battery()),
                new HashSet<>());

        client.getCart().getProducts().merge(configuredSmartphone, request.quantity(), Integer::sum);
    }

    private Client findClientOrThrow(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Nie znaleziono klienta o id " + clientId));
    }

    private Product findProductOrThrow(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Nie znaleziono produktu o id " + productId));
    }
}
