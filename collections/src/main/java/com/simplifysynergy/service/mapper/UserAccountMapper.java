package com.simplifysynergy.service.mapper;

import com.simplifysynergy.domain.Institution;
import com.simplifysynergy.domain.UserAccount;
import com.simplifysynergy.service.dto.InstitutionDTO;
import com.simplifysynergy.service.dto.UserAccountDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserAccount} and its DTO {@link UserAccountDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserAccountMapper extends EntityMapper<UserAccountDTO, UserAccount> {
    @Mapping(target = "institution", source = "institution", qualifiedByName = "institutionId")
    UserAccountDTO toDto(UserAccount s);

    @Named("institutionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InstitutionDTO toDtoInstitutionId(Institution institution);
}
