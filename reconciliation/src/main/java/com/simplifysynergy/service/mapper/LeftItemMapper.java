package com.simplifysynergy.service.mapper;

import com.simplifysynergy.domain.LeftItem;
import com.simplifysynergy.service.dto.LeftItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LeftItem} and its DTO {@link LeftItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface LeftItemMapper extends EntityMapper<LeftItemDTO, LeftItem> {}
