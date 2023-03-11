package com.simplifysynergy.service.mapper;

import com.simplifysynergy.domain.RightItem;
import com.simplifysynergy.service.dto.RightItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RightItem} and its DTO {@link RightItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface RightItemMapper extends EntityMapper<RightItemDTO, RightItem> {}
