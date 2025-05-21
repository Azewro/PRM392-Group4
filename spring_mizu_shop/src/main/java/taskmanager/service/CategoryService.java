package taskmanager.service;

import taskmanager.dto.CategoryDTO;
import taskmanager.dto.CreateCategoryRequest;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getAllCategories();
    CategoryDTO createCategory(CreateCategoryRequest request);
    CategoryDTO updateCategory(Integer id, CreateCategoryRequest request);
    void deleteCategory(Integer id);
}
