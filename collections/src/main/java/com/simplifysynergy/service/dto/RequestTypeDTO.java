package com.simplifysynergy.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.simplifysynergy.domain.RequestType} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RequestTypeDTO implements Serializable {

    private String id;

    @NotNull(message = "must not be null")
    private String type;

    private String description;

    @NotNull(message = "must not be null")
    private String code;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RequestTypeDTO)) {
            return false;
        }

        RequestTypeDTO requestTypeDTO = (RequestTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, requestTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RequestTypeDTO{" +
            "id='" + getId() + "'" +
            ", type='" + getType() + "'" +
            ", description='" + getDescription() + "'" +
            ", code='" + getCode() + "'" +
            "}";
    }
}
