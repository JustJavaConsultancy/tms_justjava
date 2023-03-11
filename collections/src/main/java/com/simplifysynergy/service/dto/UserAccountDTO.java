package com.simplifysynergy.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.simplifysynergy.domain.UserAccount} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserAccountDTO implements Serializable {

    private String id;

    @NotNull(message = "must not be null")
    private String accountNumber;

    @NotNull(message = "must not be null")
    private BigDecimal availableBalance;

    @NotNull(message = "must not be null")
    private BigDecimal currentBalance;

    @NotNull(message = "must not be null")
    private String accountName;

    @NotNull(message = "must not be null")
    private String bankCode;

    @NotNull(message = "must not be null")
    private String bankName;

    private InstitutionDTO institution;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public InstitutionDTO getInstitution() {
        return institution;
    }

    public void setInstitution(InstitutionDTO institution) {
        this.institution = institution;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserAccountDTO)) {
            return false;
        }

        UserAccountDTO userAccountDTO = (UserAccountDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userAccountDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAccountDTO{" +
            "id='" + getId() + "'" +
            ", accountNumber='" + getAccountNumber() + "'" +
            ", availableBalance=" + getAvailableBalance() +
            ", currentBalance=" + getCurrentBalance() +
            ", accountName='" + getAccountName() + "'" +
            ", bankCode='" + getBankCode() + "'" +
            ", bankName='" + getBankName() + "'" +
            ", institution=" + getInstitution() +
            "}";
    }
}
