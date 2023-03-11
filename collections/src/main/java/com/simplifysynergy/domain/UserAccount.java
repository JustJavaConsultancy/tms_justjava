package com.simplifysynergy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A UserAccount.
 */
@Document(collection = "user_account")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull(message = "must not be null")
    @Field("account_number")
    private String accountNumber;

    @NotNull(message = "must not be null")
    @Field("available_balance")
    private BigDecimal availableBalance;

    @NotNull(message = "must not be null")
    @Field("current_balance")
    private BigDecimal currentBalance;

    @NotNull(message = "must not be null")
    @Field("account_name")
    private String accountName;

    @NotNull(message = "must not be null")
    @Field("bank_code")
    private String bankCode;

    @NotNull(message = "must not be null")
    @Field("bank_name")
    private String bankName;

    @Field("institution")
    @JsonIgnoreProperties(value = { "chartOfAccount", "approvalRoute", "accounts", "institutionType" }, allowSetters = true)
    private Institution institution;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public UserAccount id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public UserAccount accountNumber(String accountNumber) {
        this.setAccountNumber(accountNumber);
        return this;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getAvailableBalance() {
        return this.availableBalance;
    }

    public UserAccount availableBalance(BigDecimal availableBalance) {
        this.setAvailableBalance(availableBalance);
        return this;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public BigDecimal getCurrentBalance() {
        return this.currentBalance;
    }

    public UserAccount currentBalance(BigDecimal currentBalance) {
        this.setCurrentBalance(currentBalance);
        return this;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public UserAccount accountName(String accountName) {
        this.setAccountName(accountName);
        return this;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getBankCode() {
        return this.bankCode;
    }

    public UserAccount bankCode(String bankCode) {
        this.setBankCode(bankCode);
        return this;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return this.bankName;
    }

    public UserAccount bankName(String bankName) {
        this.setBankName(bankName);
        return this;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Institution getInstitution() {
        return this.institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public UserAccount institution(Institution institution) {
        this.setInstitution(institution);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserAccount)) {
            return false;
        }
        return id != null && id.equals(((UserAccount) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAccount{" +
            "id=" + getId() +
            ", accountNumber='" + getAccountNumber() + "'" +
            ", availableBalance=" + getAvailableBalance() +
            ", currentBalance=" + getCurrentBalance() +
            ", accountName='" + getAccountName() + "'" +
            ", bankCode='" + getBankCode() + "'" +
            ", bankName='" + getBankName() + "'" +
            "}";
    }
}
