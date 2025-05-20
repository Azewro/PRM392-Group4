package taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taskmanager.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
