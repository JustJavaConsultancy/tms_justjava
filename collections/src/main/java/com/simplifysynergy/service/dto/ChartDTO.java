package com.simplifysynergy.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.simplifysynergy.domain.Chart} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChartDTO implements Serializable {

    private String id;

    @NotNull(message = "must not be null")
    private String code;

    private String description;

    private BigDecimal balance;

    private ChartOfAccountDTO chartOfAccount;

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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public ChartOfAccountDTO getChartOfAccount() {
        return chartOfAccount;
    }

    public void setChartOfAccount(ChartOfAccountDTO chartOfAccount) {
        this.chartOfAccount = chartOfAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChartDTO)) {
            return false;
        }

        ChartDTO chartDTO = (ChartDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, chartDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChartDTO{" +
            "id='" + getId() + "'" +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", balance=" + getBalance() +
            ", chartOfAccount=" + getChartOfAccount() +
            "}";
    }
}
