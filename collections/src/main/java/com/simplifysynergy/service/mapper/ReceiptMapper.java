package com.simplifysynergy.service.mapper;

import com.simplifysynergy.domain.Receipt;
import com.simplifysynergy.service.dto.ReceiptDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Receipt} and its DTO {@link ReceiptDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReceiptMapper extends EntityMapper<ReceiptDTO, Receipt> {}
