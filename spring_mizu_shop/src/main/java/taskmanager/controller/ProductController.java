package taskmanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taskmanager.dto.CreateProductRequest;
import taskmanager.dto.ProductResponse;
import taskmanager.service.ProductService;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestPart;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping(consumes = {"multipart/form-data", "application/json"})
    public ResponseEntity<ProductResponse> createProduct(
            @RequestPart(value = "product", required = false) CreateProductRequest request,
            @RequestPart(value = "imageUrl", required = false) MultipartFile imageFile,
            @RequestBody(required = false) CreateProductRequest jsonRequest) throws Exception {
        if (request == null && jsonRequest != null) request = jsonRequest;
        if (imageFile != null && !imageFile.isEmpty()) {
            String base64 = java.util.Base64.getEncoder().encodeToString(imageFile.getBytes());
            request.setImageUrl(base64);
        }
        return new ResponseEntity<>(productService.createProduct(request), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data", "application/json"})
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Integer id,
            @RequestPart(value = "product", required = false) CreateProductRequest request,
            @RequestPart(value = "imageUrl", required = false) MultipartFile imageFile,
            @RequestBody(required = false) CreateProductRequest jsonRequest) throws Exception {
        if (request == null && jsonRequest != null) request = jsonRequest;
        if (imageFile != null && !imageFile.isEmpty()) {
            String base64 = java.util.Base64.getEncoder().encodeToString(imageFile.getBytes());
            request.setImageUrl(base64);
        }
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
