package com.simplifysynergy.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A ApprovalRoute.
 */
@Document(collection = "approval_route")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApprovalRoute implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull(message = "must not be null")
    @Field("code")
    private String code;

    @Field("description")
    private String description;

    @Field("requestType")
    private RequestType requestType;

    @Field("approvalLine")
    private ApprovalLine approvalLine;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public ApprovalRoute id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public ApprovalRoute code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return this.description;
    }

    public ApprovalRoute description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RequestType getRequestType() {
        return this.requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public ApprovalRoute requestType(RequestType requestType) {
        this.setRequestType(requestType);
        return this;
    }

    public ApprovalLine getApprovalLine() {
        return this.approvalLine;
    }

    public void setApprovalLine(ApprovalLine approvalLine) {
        this.approvalLine = approvalLine;
    }

    public ApprovalRoute approvalLine(ApprovalLine approvalLine) {
        this.setApprovalLine(approvalLine);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApprovalRoute)) {
            return false;
        }
        return id != null && id.equals(((ApprovalRoute) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApprovalRoute{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
