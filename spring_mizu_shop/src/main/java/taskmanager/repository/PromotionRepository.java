package taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taskmanager.model.Promotion;

public interface PromotionRepository extends JpaRepository<Promotion, Integer> {
    Promotion findByCodeAndIsActiveTrue(String code);
}
