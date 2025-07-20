package taskmanager.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import taskmanager.dto.CategoryDTO;
import taskmanager.dto.CreateCategoryRequest;
import taskmanager.mapper.CategoryMapper;
import taskmanager.model.Category;
import taskmanager.repository.CategoryRepository;
import taskmanager.service.CategoryService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepo;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepo.findAll().stream()
                .map(categoryMapper::toDTO)
                .toList();
    }

    @Override
    public CategoryDTO createCategory(CreateCategoryRequest request) {
        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .sortOrder(0)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Category saved = categoryRepo.save(category);
        return categoryMapper.toDTO(saved);
    }

    @Override
    public CategoryDTO updateCategory(Integer id, CreateCategoryRequest request) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));
        if (request.getImageUrl() != null && !request.getImageUrl().trim().isEmpty()) {
            category.setImageUrl(request.getImageUrl());
        }
        if (request.getIsActive() != null) {
            category.setIsActive(request.getIsActive());
        }
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setUpdatedAt(LocalDateTime.now());
        Category saved = categoryRepo.save(category);
        return categoryMapper.toDTO(saved);
    }

    @Override
    public void deleteCategory(Integer id) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));
        category.setIsActive(false);
        category.setUpdatedAt(LocalDateTime.now());
        categoryRepo.save(category);
    }
}
