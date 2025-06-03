package store.product;

import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @CacheEvict(value = "products", allEntries = true)
    public Product create(Product product) {
        return productRepository.save(new ProductModel(product)).to();
    }

    @Cacheable(value = "products", key = "#id")
    public Product findById(String id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"))
            .to();
    }

    @Cacheable(value = "products", key = "'all'")
    public List<Product> findAll() {
        return StreamSupport.stream(productRepository.findAll().spliterator(), false)
            .map(ProductModel::to)
            .toList();
    }

    @CacheEvict(value = "products", allEntries = true)
    public void deleteById(String id) {
        if (!productRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        productRepository.deleteById(id);
    }
}