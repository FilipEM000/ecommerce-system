package order.repository;

import client.entity.Client;
import order.entity.Invoice;
import order.entity.Order;
import order.repository.impl.InMemoryInvoiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryInvoiceRepositoryTest {

    private InMemoryInvoiceRepository invoiceRepository;

    @BeforeEach
    void setUp() {
        invoiceRepository = new InMemoryInvoiceRepository();
    }

    @Test
    void shouldSaveInvoice() {
        Client client = new Client("Filip", "filip@wp.pl");
        Order order = new Order(client, client.getCart().getProducts(), BigDecimal.ZERO);
        Invoice invoice = new Invoice(order);
        String number = invoice.invoiceNumber();

        Invoice savedInvoice = invoiceRepository.save(invoice);

        assertThat(savedInvoice.invoiceNumber()).isEqualTo(number);
    }

    @Test
    void shouldReturnEmptyWhenInvoiceNotFound() {
        String number = "INV/00/00";

        Optional<Invoice> foundInvoice = invoiceRepository.findByNumber(number);

        assertThat(foundInvoice).isEmpty();
    }
}
