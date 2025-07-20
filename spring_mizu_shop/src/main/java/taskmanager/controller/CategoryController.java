package taskmanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import taskmanager.dto.CategoryDTO;
import taskmanager.dto.CreateCategoryRequest;
import taskmanager.service.CategoryService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestPart;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAll() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<CategoryDTO> create(
            @RequestPart("category") CreateCategoryRequest request,
            @RequestPart(value = "imageUrl", required = false) MultipartFile imageFile) throws Exception {
        // Nếu Android gửi file ảnh gốc thì encode, còn nếu gửi sẵn Base64 string thì giữ nguyên
        if (imageFile != null && !imageFile.isEmpty()) {
            String base64 = java.util.Base64.getEncoder().encodeToString(imageFile.getBytes());
            request.setImageUrl(base64);
        }
        // Nếu imageFile null, giữ nguyên request.getImageUrl() (Android gửi sẵn Base64 string)
        return new ResponseEntity<>(categoryService.createCategory(request), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<CategoryDTO> update(
            @PathVariable Integer id,
            @RequestPart("category") CreateCategoryRequest request,
            @RequestPart(value = "imageUrl", required = false) MultipartFile imageFile) throws Exception {
        if (imageFile != null && !imageFile.isEmpty()) {
            String base64 = java.util.Base64.getEncoder().encodeToString(imageFile.getBytes());
            request.setImageUrl(base64);
        }
        // Nếu imageFile null, giữ nguyên request.getImageUrl() (Android gửi sẵn Base64 string)
        return ResponseEntity.ok(categoryService.updateCategory(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
