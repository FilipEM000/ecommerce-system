import cli.ConsoleApp;
import client.repository.ClientRepository;
import client.repository.impl.InMemoryClientRepository;
import client.service.CartService;
import client.service.ClientService;
import client.service.impl.CartServiceImpl;
import client.service.impl.ClientServiceImpl;
import order.repository.InvoiceRepository;
import order.repository.OrderRepository;
import order.repository.impl.InMemoryInvoiceRepository;
import order.repository.impl.InMemoryOrderRepository;
import order.service.InvoiceGenerator;
import order.service.OrderService;
import order.service.impl.InvoiceGeneratorImpl;
import order.service.impl.OrderFileWriter;
import order.service.impl.OrderServiceImpl;
import product.entity.Electronics;
import product.entity.Product;
import product.entity.computer.Computer;
import product.entity.smartphone.Smartphone;
import product.repository.ProductRepository;
import product.repository.impl.InMemoryProductRepository;
import product.service.ProductService;
import product.service.impl.ProductServiceImpl;

import java.math.BigDecimal;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ClientRepository clientRepository = new InMemoryClientRepository();
        InvoiceRepository invoiceRepository = new InMemoryInvoiceRepository();
        OrderRepository orderRepository = new InMemoryOrderRepository();
        ProductRepository productRepository = new InMemoryProductRepository();

        CartService cartService = new CartServiceImpl(productRepository, clientRepository);
        ClientService clientService = new ClientServiceImpl(clientRepository);
        OrderFileWriter orderFileWriter = new OrderFileWriter("orders.json");
        InvoiceGenerator invoiceGenerator = new InvoiceGeneratorImpl(invoiceRepository);
        OrderService orderService = new OrderServiceImpl(orderRepository, clientRepository, productRepository, cartService, invoiceGenerator, orderFileWriter);
        ProductService productService = new ProductServiceImpl(productRepository);

        ConsoleApp consoleApp = new ConsoleApp(cartService, clientService, invoiceGenerator, orderService, productService);

        List<Product> products = List.of(
                new Computer("Dell Inspiron 15", new BigDecimal("2999.99"), 10),
                new Computer("Lenovo ThinkPad E14", new BigDecimal("4299.99"), 5),
                new Smartphone("Samsung Galaxy S24", new BigDecimal("3999.99"), 15),
                new Smartphone("iPhone 15", new BigDecimal("4999.99"), 8),
                new Electronics("Logitech MX Master 3S", new BigDecimal("399.99"), 20),
                new Electronics("Samsung Odyssey G5", new BigDecimal("1299.99"), 7),
                new Electronics("Sony WH-1000XM5", new BigDecimal("1499.99"), 12)
        );
        products.forEach(productService::addProduct);

        consoleApp.run();
    }
}
