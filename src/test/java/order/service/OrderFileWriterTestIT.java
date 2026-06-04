package order.service;

import client.entity.Client;
import order.entity.Order;
import order.service.impl.OrderFileWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import product.entity.computer.Computer;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderFileWriterTestIT {
    private OrderFileWriter orderFileWriter;
    private File tempFile;

    @BeforeEach
    void setUp(@TempDir Path tempDir) {
        tempFile = tempDir.resolve("test_orders.json").toFile();
        orderFileWriter = new OrderFileWriter(tempFile.getAbsolutePath());
    }

    @Test
    void shouldWriteOrderToJsonFile() throws IOException {
        Client client = new Client("Filip", "filip@wp.pl");
        Computer computer = new Computer("Dell", new BigDecimal("1000"), 5);
        client.getCart().getProducts().put(computer, 2);
        Order order = new Order(client, client.getCart().getProducts(), new BigDecimal("2000"));
        order.setId(1L);

        orderFileWriter.write(order);

        assertThat(tempFile.exists()).isTrue();
        String fileContent = Files.readString(tempFile.toPath());
        assertThat(fileContent).contains("\"clientName\" : \"Filip\"");
        assertThat(fileContent).contains("\"cost\" : 2000");
        assertThat(fileContent).contains("\"name\" : \"Dell\"");
        assertThat(fileContent).contains("\"quantity\" : 2");

    }
}
