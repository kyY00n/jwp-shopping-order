package cart.domain.product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Product> findAll();

    Optional<Product> findById(Long productId);

    Long create(Product product);

    void update(Product product);

    void deleteById(Long productId);
}
