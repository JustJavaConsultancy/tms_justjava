package com.simplifysynergy.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.simplifysynergy.domain.ApprovalLine} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApprovalLineDTO implements Serializable {

    private String id;

    private Integer level;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApprovalLineDTO)) {
            return false;
        }

        ApprovalLineDTO approvalLineDTO = (ApprovalLineDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, approvalLineDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApprovalLineDTO{" +
            "id='" + getId() + "'" +
            ", level=" + getLevel() +
            "}";
    }
}
