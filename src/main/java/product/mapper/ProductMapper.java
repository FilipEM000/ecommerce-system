package product.mapper;

import product.dto.ProductDto;
import product.entity.Product;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProductMapper {
    public static ProductDto mapToDto(Product product) {
        return new ProductDto(product.getId(), product.getName(), product.getTotalPrice(), product.getProductType(), product.getDetails());
    }
}
