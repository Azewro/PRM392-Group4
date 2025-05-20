package taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taskmanager.model.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByCategoryIdAndIsActiveTrue(Integer categoryId);
    List<Product> findByIsActiveTrue();
}
