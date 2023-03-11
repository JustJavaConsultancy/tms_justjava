package com.simplifysynergy.service.mapper;

import com.simplifysynergy.domain.LeftItem;
import com.simplifysynergy.domain.ReconciliationItem;
import com.simplifysynergy.domain.RightItem;
import com.simplifysynergy.service.dto.LeftItemDTO;
import com.simplifysynergy.service.dto.ReconciliationItemDTO;
import com.simplifysynergy.service.dto.RightItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReconciliationItem} and its DTO {@link ReconciliationItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReconciliationItemMapper extends EntityMapper<ReconciliationItemDTO, ReconciliationItem> {
    @Mapping(target = "leftItem", source = "leftItem", qualifiedByName = "leftItemId")
    @Mapping(target = "rightItem", source = "rightItem", qualifiedByName = "rightItemId")
    ReconciliationItemDTO toDto(ReconciliationItem s);

    @Named("leftItemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LeftItemDTO toDtoLeftItemId(LeftItem leftItem);

    @Named("rightItemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RightItemDTO toDtoRightItemId(RightItem rightItem);
}
