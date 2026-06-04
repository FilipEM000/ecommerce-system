package order.service;

import client.entity.Client;
import order.entity.Invoice;
import order.entity.Order;
import order.repository.InvoiceRepository;
import order.service.impl.InvoiceGeneratorImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InvoiceGeneratorImplTest {

    @Mock
    InvoiceRepository invoiceRepository;

    @InjectMocks
    InvoiceGeneratorImpl invoiceGenerator;

    @Test
    void shouldGenerateAndSaveInvoice() {
        Client client = new Client("Filip", "filip@wp.pl");
        Order order = new Order(client, client.getCart().getProducts(), BigDecimal.TEN);
        when(invoiceRepository.save(any()))
                .thenReturn(new Invoice("FV/0000/00", order));

        Invoice generatedInvoice = invoiceGenerator.generateInvoice(order);

        ArgumentCaptor<Invoice> invoiceCaptor = ArgumentCaptor.forClass(Invoice.class);
        verify(invoiceRepository, times(1)).save(invoiceCaptor.capture());

        assertThat(invoiceCaptor.getValue().order())
                .isEqualTo(order);
        assertThat(generatedInvoice)
                .isEqualTo(invoiceCaptor.getValue());
    }
}
