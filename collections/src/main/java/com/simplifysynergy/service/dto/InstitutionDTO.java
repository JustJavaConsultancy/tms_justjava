package com.simplifysynergy.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.simplifysynergy.domain.Institution} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InstitutionDTO implements Serializable {

    private String id;

    @NotNull(message = "must not be null")
    private String code;

    private String description;

    private ChartOfAccountDTO chartOfAccount;

    private ApprovalRouteDTO approvalRoute;

    private InstitutionTypeDTO institutionType;

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

    public ChartOfAccountDTO getChartOfAccount() {
        return chartOfAccount;
    }

    public void setChartOfAccount(ChartOfAccountDTO chartOfAccount) {
        this.chartOfAccount = chartOfAccount;
    }

    public ApprovalRouteDTO getApprovalRoute() {
        return approvalRoute;
    }

    public void setApprovalRoute(ApprovalRouteDTO approvalRoute) {
        this.approvalRoute = approvalRoute;
    }

    public InstitutionTypeDTO getInstitutionType() {
        return institutionType;
    }

    public void setInstitutionType(InstitutionTypeDTO institutionType) {
        this.institutionType = institutionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InstitutionDTO)) {
            return false;
        }

        InstitutionDTO institutionDTO = (InstitutionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, institutionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InstitutionDTO{" +
            "id='" + getId() + "'" +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", chartOfAccount=" + getChartOfAccount() +
            ", approvalRoute=" + getApprovalRoute() +
            ", institutionType=" + getInstitutionType() +
            "}";
    }
}
