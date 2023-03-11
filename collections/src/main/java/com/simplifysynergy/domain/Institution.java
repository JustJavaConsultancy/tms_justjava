package com.simplifysynergy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Institution.
 */
@Document(collection = "institution")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Institution implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull(message = "must not be null")
    @Field("code")
    private String code;

    @Field("description")
    private String description;

    @Field("chartOfAccount")
    private ChartOfAccount chartOfAccount;

    @Field("approvalRoute")
    private ApprovalRoute approvalRoute;

    @Field("accounts")
    @JsonIgnoreProperties(value = { "institution" }, allowSetters = true)
    private Set<UserAccount> accounts = new HashSet<>();

    @Field("institutionType")
    private InstitutionType institutionType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Institution id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Institution code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return this.description;
    }

    public Institution description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ChartOfAccount getChartOfAccount() {
        return this.chartOfAccount;
    }

    public void setChartOfAccount(ChartOfAccount chartOfAccount) {
        this.chartOfAccount = chartOfAccount;
    }

    public Institution chartOfAccount(ChartOfAccount chartOfAccount) {
        this.setChartOfAccount(chartOfAccount);
        return this;
    }

    public ApprovalRoute getApprovalRoute() {
        return this.approvalRoute;
    }

    public void setApprovalRoute(ApprovalRoute approvalRoute) {
        this.approvalRoute = approvalRoute;
    }

    public Institution approvalRoute(ApprovalRoute approvalRoute) {
        this.setApprovalRoute(approvalRoute);
        return this;
    }

    public Set<UserAccount> getAccounts() {
        return this.accounts;
    }

    public void setAccounts(Set<UserAccount> userAccounts) {
        if (this.accounts != null) {
            this.accounts.forEach(i -> i.setInstitution(null));
        }
        if (userAccounts != null) {
            userAccounts.forEach(i -> i.setInstitution(this));
        }
        this.accounts = userAccounts;
    }

    public Institution accounts(Set<UserAccount> userAccounts) {
        this.setAccounts(userAccounts);
        return this;
    }

    public Institution addAccounts(UserAccount userAccount) {
        this.accounts.add(userAccount);
        userAccount.setInstitution(this);
        return this;
    }

    public Institution removeAccounts(UserAccount userAccount) {
        this.accounts.remove(userAccount);
        userAccount.setInstitution(null);
        return this;
    }

    public InstitutionType getInstitutionType() {
        return this.institutionType;
    }

    public void setInstitutionType(InstitutionType institutionType) {
        this.institutionType = institutionType;
    }

    public Institution institutionType(InstitutionType institutionType) {
        this.setInstitutionType(institutionType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Institution)) {
            return false;
        }
        return id != null && id.equals(((Institution) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Institution{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
