package cart.application;

import cart.domain.product.Product;
import cart.domain.product.ProductRepository;
import cart.dto.product.ProductRequest;
import cart.dto.product.ProductResponse;
import cart.exception.ProductException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;


    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(ProductResponse::of).collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException.NotFound(productId));
        return ProductResponse.of(product);
    }

    public Long createProduct(ProductRequest productRequest) {
        Product product = Product.of(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        return productRepository.create(product);
    }

    public void updateProduct(Long productId, ProductRequest productRequest) {
        Product product = Product.of(productId, productRequest.getName(), productRequest.getPrice(),
                productRequest.getImageUrl());
        productRepository.update(product);
    }

    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }
}