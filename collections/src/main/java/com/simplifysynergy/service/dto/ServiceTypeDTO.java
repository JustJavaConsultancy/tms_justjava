package com.simplifysynergy.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.simplifysynergy.domain.ServiceType} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServiceTypeDTO implements Serializable {

    private String id;

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
        if (!(o instanceof ServiceTypeDTO)) {
            return false;
        }

        ServiceTypeDTO serviceTypeDTO = (ServiceTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, serviceTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceTypeDTO{" +
            "id='" + getId() + "'" +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
