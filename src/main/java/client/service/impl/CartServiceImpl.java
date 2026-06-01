package client.service.impl;

import client.entity.Cart;
import client.entity.Client;
import client.repository.ClientRepository;
import client.service.CartService;
import exception.*;
import lombok.AllArgsConstructor;
import product.entity.Product;
import product.entity.computer.Computer;
import product.entity.computer.ProcessorType;
import product.entity.computer.Ram;
import product.entity.smartphone.BatteryCapacity;
import product.entity.smartphone.Color;
import product.entity.smartphone.Smartphone;
import product.repository.ProductRepository;

import java.util.HashSet;
import java.util.Map;

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
    public void addStandardProductToCart(Long clientId, Long productId, Integer quantity) {
        Client client = findClientOrThrow(clientId);
        Product masterProduct = findProductOrThrow(productId);
        validateQuantity(client.getCart(), masterProduct, quantity);
        Product clonedProduct = masterProduct.cloneProduct();

        client.getCart().getProducts().merge(clonedProduct, quantity, Integer::sum);
    }

    @Override
    public void addComputerToCart(Long clientId, Long productId, Integer quantity, String processor, String ram) {
        Client client = findClientOrThrow(clientId);
        Product masterProduct = findProductOrThrow(productId);
        validateQuantity(client.getCart(), masterProduct, quantity);

        if (masterProduct instanceof Computer masterComputer) {
            Computer configuredComputer = new Computer(masterComputer);
            configuredComputer.configure(ProcessorType.valueOf(processor), Ram.valueOf(ram));

            client.getCart().getProducts().merge(configuredComputer, quantity, Integer::sum);
        } else {
            throw new InvalidProductTypeException("Produkt nie jest komputerem");
        }
    }

    @Override
    public void addSmartphoneToCart(Long clientId, Long productId, Integer quantity, String color, String battery) {
        Client client = findClientOrThrow(clientId);
        Product masterProduct = findProductOrThrow(productId);
        validateQuantity(client.getCart(), masterProduct, quantity);

        if (masterProduct instanceof Smartphone masterSmartphone) {
            Smartphone configuredSmartphone = new Smartphone(masterSmartphone);
            configuredSmartphone.configure(Color.valueOf(color), BatteryCapacity.valueOf(battery), new HashSet<>());

            client.getCart().getProducts().merge(configuredSmartphone, quantity, Integer::sum);
        } else {
            throw new InvalidProductTypeException("Ten produkt nie jest smartfonem!");
        }
    }

    private Client findClientOrThrow(Long clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException("Nie znaleziono klienta o id " + clientId));
    }

    private Product findProductOrThrow(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Nie znaleziono produktu o id " + productId));
    }

    private void validateQuantity(Cart cart, Product masterProduct, Integer quantityToAdd) {
        int alreadyInCart = cart.getProducts().entrySet().stream()
                .filter(entry -> entry.getKey().getId().equals(masterProduct.getId()))
                .mapToInt(Map.Entry::getValue)
                .sum();

        if (quantityToAdd <= 0) {
            throw new InvalidQuantityException("Ilość dodawana do koszyka musi być większa niż zero!");
        }

        if (masterProduct.getQuantity() < (quantityToAdd + alreadyInCart)) {
            throw new NotEnoughQuantityInMagazineException(
                    "Nie masz wystarczającej ilości produktu na stanie. Dostępnych w magazynie: "
                            + masterProduct.getQuantity()
                            + ", w koszyku masz już: " + alreadyInCart
                            + ", próbujesz dodać: " + quantityToAdd);
        }
    }
}
