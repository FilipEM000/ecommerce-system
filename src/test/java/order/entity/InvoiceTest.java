package order.entity;

import client.entity.Client;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class InvoiceTest {

    @Test
    void shouldGenerateInvoiceNumberWithCurrentYearAndOrderId() {
        Client client = new Client("Filip", "filip@wp.pl");
        Order order = new Order(client, client.getCart().getProducts(), BigDecimal.TEN);
        order.setId(1L);

        Invoice invoice = new Invoice(order);

        assertThat(invoice.invoiceNumber()).isEqualTo("FV/" + LocalDate.now().getYear() + "/1");
        assertThat(invoice.order()).isEqualTo(order);
    }
}
