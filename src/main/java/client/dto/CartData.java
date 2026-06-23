package client.dto;

import client.entity.Client;
import product.entity.Product;

public record CartData(Client client, Product masterProduct) {
}
