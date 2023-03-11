package com.simplifysynergy.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.simplifysynergy.domain.ChartOfAccount} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChartOfAccountDTO implements Serializable {

    private String id;

    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        if (!(o instanceof ChartOfAccountDTO)) {
            return false;
        }

        ChartOfAccountDTO chartOfAccountDTO = (ChartOfAccountDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, chartOfAccountDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChartOfAccountDTO{" +
            "id='" + getId() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
