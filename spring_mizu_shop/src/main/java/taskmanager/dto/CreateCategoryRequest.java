package taskmanager.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCategoryRequest {
    private String name;
    private String description;
    private String imageUrl;
    private Boolean isActive;
}
