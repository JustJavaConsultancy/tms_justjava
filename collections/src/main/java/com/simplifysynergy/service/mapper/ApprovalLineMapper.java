package com.simplifysynergy.service.mapper;

import com.simplifysynergy.domain.ApprovalLine;
import com.simplifysynergy.service.dto.ApprovalLineDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ApprovalLine} and its DTO {@link ApprovalLineDTO}.
 */
@Mapper(componentModel = "spring")
public interface ApprovalLineMapper extends EntityMapper<ApprovalLineDTO, ApprovalLine> {}
