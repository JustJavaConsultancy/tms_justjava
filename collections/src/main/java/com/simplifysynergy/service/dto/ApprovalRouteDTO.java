package com.simplifysynergy.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.simplifysynergy.domain.ApprovalRoute} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApprovalRouteDTO implements Serializable {

    private String id;

    @NotNull(message = "must not be null")
    private String code;

    private String description;

    private RequestTypeDTO requestType;

    private ApprovalLineDTO approvalLine;

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

    public RequestTypeDTO getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestTypeDTO requestType) {
        this.requestType = requestType;
    }

    public ApprovalLineDTO getApprovalLine() {
        return approvalLine;
    }

    public void setApprovalLine(ApprovalLineDTO approvalLine) {
        this.approvalLine = approvalLine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApprovalRouteDTO)) {
            return false;
        }

        ApprovalRouteDTO approvalRouteDTO = (ApprovalRouteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, approvalRouteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApprovalRouteDTO{" +
            "id='" + getId() + "'" +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", requestType=" + getRequestType() +
            ", approvalLine=" + getApprovalLine() +
            "}";
    }
}
