package taskmanager.service;

import taskmanager.dto.CreateProductRequest;
import taskmanager.dto.ProductResponse;

import java.util.List;

public interface ProductService {
    List<ProductResponse> getAllProducts();
    ProductResponse getProductById(Integer id);
    ProductResponse createProduct(CreateProductRequest request);
    ProductResponse updateProduct(Integer id, CreateProductRequest request);
    void deleteProduct(Integer id);
    List<ProductResponse> getProductsByCategory(Integer categoryId);
    List<ProductResponse> getProductsByName(String name);
}
