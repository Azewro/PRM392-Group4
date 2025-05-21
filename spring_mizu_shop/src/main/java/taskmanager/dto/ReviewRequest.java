package taskmanager.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRequest {
    private Integer userId;
    private Integer productId;
    private Integer rating;
    private String comment;
}
