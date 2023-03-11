package com.simplifysynergy.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.simplifysynergy.domain.InstitutionType} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InstitutionTypeDTO implements Serializable {

    private String id;

    @NotNull(message = "must not be null")
    private String code;

    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InstitutionTypeDTO)) {
            return false;
        }

        InstitutionTypeDTO institutionTypeDTO = (InstitutionTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, institutionTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InstitutionTypeDTO{" +
            "id='" + getId() + "'" +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
