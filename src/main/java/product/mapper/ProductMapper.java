package product.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import product.dto.ProductDto;
import product.entity.Product;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProductMapper {
    public static ProductDto mapToDto(Product product) {
        return new ProductDto(product.getId(), product.getName(), product.getPrice(), product.getProductType(), product.getDetails());
    }
}
