package taskmanager.mapper.impl;

import taskmanager.dto.PromotionDTO;
import taskmanager.mapper.PromotionMapper;
import taskmanager.model.Promotion;

public class PromotionMapperImpl implements PromotionMapper {
    public PromotionMapperImpl() {
    }

    @Override
    public PromotionDTO toDTO(Promotion promotion) {
        if ( promotion == null ) {
            return null;
        }



        PromotionDTO.PromotionDTOBuilder promotionDTO = PromotionDTO.PromotionDTOBuilder.builder();

        promotionDTO.withId( promotion.getId() );
        promotionDTO.withCode( promotion.getCode() );
        promotionDTO.withDiscountPercent( promotion.getDiscountPercent() );
        promotionDTO.withStartDate( promotion.getStartDate() );
        promotionDTO.withEndDate( promotion.getEndDate() );
        promotionDTO.withMinOrderValue( promotion.getMinOrderValue() );
        promotionDTO.withIsActive( promotion.getIsActive() );

        return promotionDTO.build();
    }
}
