package store.product;

import java.util.List;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product create(Product product) {
        return productRepository.save(new ProductModel(product)).to();
    }

    public Product findById(String id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"))
            .to();
    }

    public List<Product> findAll() {
        return StreamSupport.stream(productRepository.findAll().spliterator(), false)
            .map(ProductModel::to)
            .toList();
    }

    public void deleteById(String id) {
        if (!productRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        productRepository.deleteById(id);
    }
    
}